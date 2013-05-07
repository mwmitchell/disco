(ns disco.q
  (:require [clojure.string :as string]))

(defn present? [s]
  (when s (not (string/blank? (str s)))))

(defmacro whenp [v & forms]
  `(when (present? ~v) ~@forms))

(defn ->num [x]
  (if (number? x)
    x
    (when (and (present? x) (re-find #"[0-9]+" x))
      (try
        (num (read-string x))
        (catch java.lang.NumberFormatException e nil)))))

(def ^{:dynamic true} *field-map* nil)

(defmacro with-fields [mapping & forms]
  `(binding [*field-map* ~mapping]
     ~@forms))

(def cds (partial string/join ","))
(def sds (partial string/join " "))
(defn- swrap [q s] (str q s q))
(def squote (partial swrap "'"))
(def dquote (partial swrap "\""))

(defn- de-keyword [x]
  (or (and (keyword? x) (name x)) x))

(defn map-field [f]
  (de-keyword (get *field-map* f f)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(letfn [(x-append [ch q v]
          {:pre [(or (nil? v) (number? v))]}
          (when (present? q)
            (or (and v (str (de-keyword q) ch v)) q)))]
  (def boost (partial x-append "^"))
  (def slop (partial x-append "~"))
  (def fuzz (partial x-append "~")))

(defn group [x]
  (whenp x (str "(" x ")")))

(defn phrase [q & {s :slop}]
  (whenp q (slop (dquote q) s)))

(defn term [q & {f :fuzz}]
  (whenp q (fuzz q f)))

(let [types {:or " OR " :and " AND "}]
  (letfn [(op [t & qs]
            (not-empty (group (string/join (types t) (filterv present? qs)))))]
    (def any-of (partial op :or))
    (def all-of (partial op :and))))

(defn fquery [field q]
  {:pre [(or (string? field) (keyword field))]}
  (whenp q (str (map-field field) ":" (group q))))

(defn mfquery [m]
  (map #(fquery (first %) (last %)) m))

(defn flist [& fields]
  (cds (map #(if (sequential? %)
               (boost (map-field (first %)) (last %))
               (map-field %)) fields)))

(defn srt [& sorts]
  (cds (keep (fn [[f d]] (when (and f d) (str (map-field f) " " (de-keyword d)))) sorts)))

(defn within [f t]
  (when (or f t)
    (str "[" (or f "*") " TO " (or t "*") "]")))

(defn gt= [x] (whenp x (within (+ x 1) "*")))

(defn gt [x] (whenp x (within (+ x 1) "*")))

(defn gt= [x] (whenp x (within x "*")))

(defn lt [x] (whenp x (within "*" (- x 1))))

(defn lt= [x] (whenp x (within "*" x)))

(defn must [s] (whenp s (str "+" s)))

(defn must-not [s] (whenp s (str "-"  s)))

(defn none-of [& x]
  (must-not (apply any-of x)))

(def everything "[* TO *]")

(def nothing "-[* TO *]")

(letfn [(eq-pairs [[k v]]
          (str (name k) "=" (squote (de-keyword v))))
        (lparams-args [m]
          (when-let [s (not-empty (sds (map eq-pairs m)))]
            (str " " s)))]

  (defn lparams [ptype opts & [q]]
    {:pre [(keyword? ptype)]}
    (str "{!" (name ptype) (lparams-args opts) "}" q)))

(defn fun [fname & args]
  {pre [(keyword? fname)]}
  (let [values (cds (map de-keyword args))]
    (when (seq values)
      (str (name fname) (group values)))))
