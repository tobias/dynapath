(ns dynapath.defaults-test
  (:use midje.sweet
        dynapath.defaults
        dynapath.dynamic-classpath)
  (:import (java.net URL URLClassLoader)
           clojure.lang.DynamicClassLoader))

(deftype Frobble [])

(let [url-cl (URLClassLoader. (make-array URL 0) nil)
      dyn-cl (DynamicClassLoader.)]
  
  (fact "DynamicClassLoader should be extended"
    (satisfies? DynamicClasspath dyn-cl) => true)

  (fact "URLClassLoader should be extended"
    (satisfies? DynamicClasspath url-cl) => true)

  (fact "add-classpath-url/get-classpath-urls should work for a URLClassLoader"
    (let [url (URL. "http://ham.biscuit")]
      (add-classpath-url url-cl url)
      (classpath-urls url-cl) => [url]))

  (fact "add-classpath-url/get-classpath-urls should work for a DynamicClassLoader"
    (let [url (URL. "http://ham.biscuit")]
      (add-classpath-url dyn-cl url)
      (classpath-urls dyn-cl) => [url])))



