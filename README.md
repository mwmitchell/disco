# disco

Simple Solr utilities functions in your favorite programming language.

# use
project.clj:

  [disco "1.0.0-SNAPSHOT"]


code:

  (require '[disco.q :as q])
  (all-of (fquery :text "hi")
          (fquery :_query_
                  (phrase (lparams :dismax {:qf :title :pf :title :v :$qq}))))

Checkout the tests for more samples...

## License

Copyright (C) 2011 Matt Mitchell

Distributed under the Eclipse Public License, the same as Clojure.