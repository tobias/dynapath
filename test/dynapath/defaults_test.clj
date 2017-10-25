(ns dynapath.defaults-test
  (:use clojure.test
        dynapath.defaults
        dynapath.dynamic-classpath)
  (:import (java.net URL URLClassLoader)
           clojure.lang.DynamicClassLoader))

(deftype Frobble [])

(let [foo-url (URL. "http://foo.bar")
      url-cl  (URLClassLoader. (into-array [foo-url]) nil)
      dyn-cl  (DynamicClassLoader.)]
  
  (deftest DynamicClassLoader-should-be-extended
    (is (satisfies? DynamicClasspath dyn-cl)))

  (deftest URLClassLoader-should-be-extended
    (is (satisfies? DynamicClasspath url-cl)))

  (deftest classpath-urls-should-work-for-a-URLClassLoader
    (is (= [foo-url] (classpath-urls url-cl))))

  (deftest add-classpath-url-get-classpath-urls-should-work-for-a-DynamicClassLoader
    (let [url (URL. "http://ham.biscuit")]
      (add-classpath-url dyn-cl url)
      (is (= [url] (classpath-urls dyn-cl))))))



