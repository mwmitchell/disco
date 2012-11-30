(defproject disco "1.0.0-SNAPSHOT"
  :description "Disco - Solr toolbox"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [clj-time "0.3.1"]]
  :profiles {:dev {:dependencies [[midje "1.4.0"]
                                  [bultitude "0.2.0"]]}})
