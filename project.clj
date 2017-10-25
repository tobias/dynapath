(defproject org.tcrawley/dynapath "1.0.0"
  :description "An abstraction for modifiable/readable class loaders."
  :url "https://github.com/tobias/dynapath"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :deploy-repositories {"releases"
                        {:url "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
                         :creds :gpg}
                        "snapshots"
                        {:url "https://oss.sonatype.org/content/repositories/snapshots/"
                         :creds :gpg}}
  :scm {:url "git@github.com:tobias/dynapath.git"}
  :pom-addition [:developers [:developer
                              [:name "Toby Crawley"]
                              [:url "https://github.com/tobias/"]
                              [:email "toby@tcrawley.org"]
                              [:timezone "-5"]]]
  :aliases {"test-all" ["with-profile" "+1.4:+1.5:+1.6:+1.7:+1.8:+1.9" "test"]}
  :profiles {:dev
             {:dependencies [[org.clojure/clojure "1.8.0"]]}
             :1.4
             {:dependencies [[org.clojure/clojure "1.4.0"]]}
             :1.5
             {:dependencies [[org.clojure/clojure "1.5.1"]]}
             :1.6
             {:dependencies [[org.clojure/clojure "1.6.0"]]}
             :1.7
             {:dependencies [[org.clojure/clojure "1.7.0"]]}
             :1.8
             {:dependencies [[org.clojure/clojure "1.8.0"]]}
             :1.9
             {:dependencies [[org.clojure/clojure "1.9.0-beta2"]]}})
