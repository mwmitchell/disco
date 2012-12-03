(ns disco.q
  (:require [clojure.string :as string]
            ;;[clj-time.core :as time]
            ;;[clj-time.format :as timef]
            ))

(def ^{:dynamic true} *field-map* nil)

(defmacro with-fields [mapping & forms]
  `(binding [*field-map* ~mapping]
     ~@forms))

;; [* TO NOW]
;; [1976-03-06T23:59:59.999Z TO *]
;; [1995-12-31T23:59:59.999Z TO 2007-03-06T00:00:00Z]
;; 1976-03-06T23:59:59.999Z/YEAR
;; [NOW-1YEAR/DAY TO NOW/DAY+1DAY]

;; (within (time/minus (time/now) (time/days 7))
;;         (time/plus (time/now) (time/days 7)))

(defn de-keyword [x]
  (or (and (keyword? x) (name x)) x))

(defn map-field [f]
  (de-keyword (get *field-map* f f)))

(letfn [(x-append [ch q v]
          {:pre [(or (nil? v) (number? v))]}
          (or (and v (str (de-keyword q) ch v)) q))]

  (def boost (partial x-append "^"))

  (def slop (partial x-append "~")))

(defn phrase [q]
  (str "\"" q "\""))

(defn group [x]
  (str "(" x ")"))

(let [types {:or " OR " :and " AND "}]

  (defn op [t & qs]
    {:pre [(types t) (sequential? qs)]}
    (string/join (types t) qs)))

(def any-of (partial op :or))

(def all-of (partial op :and))

(defn query [q & {p :phrase s :slop b :boost :as opts}]
  (boost (group (if (or p s) (slop (str "\"" q "\"") s) q)) b))

(defn fquery [field q & opts]
  {:pre [(or (string? field) (keyword field))]}
  (str (map-field field) ":" (apply query q opts)))

(defn flist [& fields]
  (string/join "," (map #(if (sequential? %)
                           (boost (map-field (first %)) (last %))
                           (map-field %)) fields)))

(defn srt [& sorts]
  (string/join "," (map (fn [[f d]] (str (map-field f) " " (de-keyword d))) sorts)))

(defn within [f t]
  (str "[" (or f "*") " TO " (or t "*") "]"))

(defn gt [x] (within (+ x 1) "*"))

(defn gt= [x] (within x "*"))

(defn lt [x] (within "*" (- x 1)))

(defn lt= [x] (within "*" x))

(defn must [s] (str "+" s))

(defn must-not [s] (str "-"  s))

(defn none-of [& x]
  (must-not (group (apply any-of x))))

(letfn [(eq-pairs [[k v]]
          (str (name k) "=" (str "'" (de-keyword v) "'")))
        (lparams-args [m]
          (when-let [s (not-empty (string/join " " (map eq-pairs m)))]
            (str " " s)))]

  (defn lparams [ptype opts & [q]]
    {:pre [(keyword? ptype)]}
    (str "{!" (name ptype) (lparams-args opts) "}" q)))

(letfn [(func-args [args] (string/join "," (map de-keyword args)))]

  (defn fun [fname & args]
    {pre [(keyword? fname)]}
    (str (name fname) "(" (func-args args) ")")))