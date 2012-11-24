(ns dynapath.util
  "Abstracts the getURLs and addURL functionality of URLClassLoader to a protocol."
  (:require [dynapath.dynamic-classpath :as dc]
            dynapath.defaults)) ;; trigger the default implementations

(defn addable-classpath?
  "Returns true if the given ClassLoader provides add-claspath-url."
  [cl]
  (and (satisfies? dc/DynamicClasspath cl)
       (dc/can-add? cl)))

(defn readable-classpath?
  "Returns true if the given ClassLoader provides classpath-urls."
  [cl]
  (and (satisfies? dc/DynamicClasspath cl)
       (dc/can-read? cl)))

(defn classpath-urls
  "Returns the URLs for the given ClassLoader, or nil if the ClassLoader is not readable."
  [cl]
  (if (readable-classpath? cl)
    (dc/classpath-urls cl)))

(defn all-classpath-urls
  "Walks up the parentage chain for a ClassLoader, concatenating any URLs it retrieves.
If no ClassLoader is provided, RT/baseLoader is assumed."
  ([]
     (all-classpath-urls (clojure.lang.RT/baseLoader)))
  ([cl]
      (->> (iterate #(.getParent %) cl)
           (take-while identity)
           reverse
           (mapcat classpath-urls)
           distinct)))

(defn add-classpath-url
  "Attempts to add a url to the given ClassLoader, returning true on success.
If the ClassLoader is not addable, does nothing and returns nil."
  [cl url]
  (when (addable-classpath? cl)
    (dc/add-classpath-url cl url)
    true))



