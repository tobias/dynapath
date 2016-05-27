(defproject org.tcrawley/dynapath "0.3.0-SNAPSHOT"
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

  :profiles {:dev
             {:dependencies [[org.clojure/clojure "1.4.0"]]}})
