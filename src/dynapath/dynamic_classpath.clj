(ns dynapath.dynamic-classpath
  "Provides the implementation of the DynamicClasspath protocol.")

(defprotocol DynamicClasspath
  (can-read? [cl]
    "Must return true if classpath-urls is implemented.")
  (can-add? [cl]
    "Must return true if add-classpath-url is implemented.")
  (classpath-urls [cl]
    "Returns a seq of the given ClassLoader's URLs.")
  (add-classpath-url [cl url]
    "Adds the url to the classpath of the given ClassLoader."))

(def ^{:doc "A map that provides implementations of can-read? and can-add? that return true.
Useful as a base for a DynamicClasspath implementation."}
  base-readable-addable-classpath
  {:can-add? (constantly true)
   :can-read? (constantly true)})
