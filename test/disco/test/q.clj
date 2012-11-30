(ns disco.test.q
  (:use [clojure.test]
        [midje.sweet]
        [disco.q]))

(facts "boost"
  (boost ..q.. nil) => ..q..
  (boost ..q.. 10) => "..q..^10")

(facts "slop"
  (slop ..q.. nil) => ..q..
  (slop ..q.. 10) => "..q..~10")

(facts "group"
  (group ..q..) => "(..q..)")

(facts "phrase"
  (phrase ..q..) => "\"..q..\"")

(facts "any-of"
  (any-of [1 2 3]) => "1 OR 2 OR 3")

(facts "all-of"
  (all-of [1 2 3]) => "1 AND 2 AND 3")

(facts "query"
  (query ..q..) => "(..q..)"
  (query ..q.. :phrase true) => "(\"..q..\")"
  (query ..q.. :boost 1) => "(..q..)^1"
  (query ..q.. :slop 1) => "(\"..q..\"~1)")

(facts "fquery"
  (fquery :field ..q..) => "field:(..q..)"
  (fquery :field ..q.. :phrase true) => "field:(\"..q..\")"
  (fquery :field ..q.. :boost 1) => "field:(..q..)^1"
  (fquery :field ..q.. :slop 1) => "field:(\"..q..\"~1)")

(facts "fields"
  (fields :a :b :c) => "a,b,c"
  (fields [:a 1] [:b 2] [:c 3]) => "a^1,b^2,c^3")

(facts "within"
  (within 1 10) => "[1 TO 10]"
  (within nil 10) => "[* TO 10]"
  (within 1 nil) => "[1 TO *]")

(facts "gt and gt="
  (gt 10) => "[11 TO *]"
  (gt= 10) => "[10 TO *]")

(facts "lt and lt="
  (lt 10) => "[* TO 9]"
  (lt= 10) => "[* TO 10]")

(facts "must"
  (must ..q..) => "+..q..")

(facts "must-not"
  (must-not ..q..) => "-..q..")

(facts "none-of"
  (none-of [1 2 3]) => "-(1 OR 2 OR 3)")

(facts "lparams"
  (lparams :dismax {:qf "title,summary"} ..q..) => "{!dismax qf='title,summary'}..q..")

(facts "fun"
  (func :sum 1 2 3) => "sum(1,2,3)")