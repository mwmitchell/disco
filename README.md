# disco

Solr utility functions, in everyone's favorite programming language.

## getting started

For leiningen projects, in your project.clj :dependencies:

```clojure
[disco "1.0.0-SNAPSHOT"]
```

## example queries

```clojure
(require '[disco.q :as q])

(all-of (fquery :text "hi")
        (fquery :_query_
                (phrase (lparams :dismax {:qf :title :pf :title :v :$qq}))))
```

Checkout the tests for more samples...

## License

Copyright (C) 2013 Matt Mitchell

Distributed under the Eclipse Public License, the same as Clojure.
