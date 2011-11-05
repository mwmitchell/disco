# fire

A Clojure DSL for Solr.

## My sloppy notes...

{:name "sam" :_boost 10 :_slop 5}

{:name "sam"}

;; name:samiam AND age:[* TO 10] AND (title:sir* OR title:king*)
[:and {:name "samiam"
       :age [:lt 10]
       :title [:or [:starts-with "sir" "king"]}

;; {!frange l=0 u=2.2}log(sum(user_ranking, editor_ranking))
[:!frange {:l 0 :u 2.2}
 [:log-> [:sum-> :user_ranking :editor_ranking]]]

;; {!frange l=martin u=rowling}author_last_name
[:!frange {:l "martin" :u "rowling"}
 :author_last_name]

[:!boost {:b [:log-> :popularity] "foo"}]

[:!boost {:b [:recip->
              [:ms-> "NOW" :my_date_field]
              "3.16e-11" "1" "2"]}]

[:!field {:f :myfield} "Foo Bar"]

[:!prefix {:f :my_field} "foo"]

[:raw! {:f :my_field} "Foo Bar"]

[:!term {:f :weight} 1.5]

[:range 10]
[:range nil 10]

{:_val_ [:recip->
         [:hsin 0.61 -1.56 :lat :lng 3963.205]
         1 1 10]}

;; name:(samiam*)
(meta {:name [:starts-with "samiam"]})

;; _query_:"type:poems"
{:_query_ {:type "poems"}}

;; text:"roses are red" AND _query_:"type:poems"
[:and {:text [:exactly "roses are red"]}
 {:_query_ {:type "poems"}}]

;; text:hi AND _query_:"{!dismax qf=title pf=title}how now brown cow"
[:and
 {:text "hi"}
 {:_query_ [:!dismax {:qf :title :pf :title} "how now brown cow"]}]

;; {!func}recip(rord(date),1,1000,1000)
[:!func [:recip-> [:rord-> [:date->] 1 1000 1000]]]

;; anything that starts-with ! can accept an option hash map,
;; and an ending string:
;; [:!dismax {:qf :title} "testing"]

;; :_query_ is special, its value must be escaped
;; -_query_:"type:poem"
[:not {:_query_ {:type "poem"}}]


 [:or
  [:exactly "this"]
  [:not
   [:field :price [:between 10 100]]]
  [:or "this" "that"
   [:not [:and
          [:field :name [:contains "samiam"]]
          [:field :price [:lt 10]]]]]]

## License

Copyright (C) 2011 Matt Mitchell

Distributed under the Eclipse Public License, the same as Clojure.
