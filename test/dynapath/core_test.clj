(ns dynapath.core-test
  (:use midje.sweet
        dynapath.core)
  (:import (java.net URL URLClassLoader)
           clojure.lang.DynamicClassLoader))

(deftype Frobble [])

(let [url-cl (URLClassLoader. (make-array URL 0) nil)
      dyn-cl (DynamicClassLoader.)]
  
  (fact "DynamicClassLoader should be extended"
    (satisfies? DynamicClasspath dyn-cl) => true)

  (fact "URLClassLoader should be extended"
    (satisfies? DynamicClasspath url-cl) => true)

  (fact "add-classpath-url/classpath-urls should work for a URLClassLoader"
    (let [url (URL. "http://ham.biscuit")]
      (add-classpath-url url-cl url)
      (classpath-urls url-cl) => [url]))

  (fact "add-classpath-url/classpath-urls should work for a DynamicClassLoader"
    (let [url (URL. "http://ham.biscuit")]
      (add-classpath-url dyn-cl url)
      (classpath-urls dyn-cl) => [url])))

(fact "addable-classpath? should work"
  (let [frobble (Frobble.)]
    (addable-classpath? frobble) => false
    (extend-type Frobble
      DynamicClasspath
      (can-add? [_] false))
    (addable-classpath? frobble) => false
    (extend-type Frobble
      DynamicClasspath
      (can-add? [_] true))
    (addable-classpath? frobble) => true))

(fact "readable-classpath? should work"
  (let [frobble (Frobble.)]
   (extend-type Frobble
      DynamicClasspath
      (can-read? [_] false))
    (readable-classpath? frobble) => false
    (extend-type Frobble
      DynamicClasspath
      (can-read? [_] true))
    (readable-classpath? frobble) => true))

