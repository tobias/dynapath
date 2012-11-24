(ns dynapath.util-test
  (:use midje.sweet
        dynapath.util
        [dynapath.dynamic-classpath :only [DynamicClasspath]])
  (:import (java.net URL URLClassLoader)))

(deftype Frobble [])

(let [urls [(URL. "http://ham.biscuit")]
      all-urls (conj urls (URL. "http://gravy.biscuit"))
      url-cl (URLClassLoader. (into-array urls) nil)
      basic-cl (proxy [ClassLoader] [])]
  
  (fact "classpath-urls should work for a readable classloader"
    (classpath-urls url-cl) => urls)

  (fact "classpath-urls should work for a non-readable classloader"
    (classpath-urls basic-cl) => nil)

  (fact "all-classpath-urls should work for a parent with the urls"
    (all-classpath-urls (proxy [ClassLoader] [url-cl])) => urls)

  (fact "all-classpath-urls should order urls properly"
    (all-classpath-urls (URLClassLoader. (into-array [(last all-urls)]) url-cl)) => all-urls)

  (fact "all-classpath-urls should use the baseLoader when called with a zero arity"
    (add-classpath-url (clojure.lang.RT/baseLoader) (first urls))
    (last (all-classpath-urls)) => (first urls))
  
  (fact "add-classpath-url should work for an addable classpath"
    (add-classpath-url url-cl (last all-urls)) => true
    (classpath-urls url-cl) => all-urls)

  (fact "add-classpath-url should work for an non-addable classpath"
    (add-classpath-url basic-cl (last all-urls)) => nil
    (classpath-urls basic-cl) => nil))

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

