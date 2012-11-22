(defproject dynapath "0.2.0"
  :description "An abstraction for modifiable/readable class loaders."
  :url "https://github.com/tobias/dynapath"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :profiles {:dev
             {:dependencies [[org.clojure/clojure "1.4.0"]
                             [lein-midje          "2.0.0-SNAPSHOT"]
                             [midje               "1.4.0"]]}})
