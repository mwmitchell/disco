# disco

Simple Solr utilities functions in your favorite programming language.

# use
project.clj:

  [disco "1.0.0-SNAPSHOT"]


code:

  (require '[disco.q :as q])
  (q/query "testing" :slop 2)

Checkout the tests for more samples...

## License

Copyright (C) 2011 Matt Mitchell

Distributed under the Eclipse Public License, the same as Clojure.