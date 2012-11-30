# disco

Simple Solr utilities for your favorite language.

# use
project.clj: [disco "1.0.0-SNAPSHOT"]

(require '[disco.q :as q])

(q/query "testing" :slop 2)

## License

Copyright (C) 2011 Matt Mitchell

Distributed under the Eclipse Public License, the same as Clojure.