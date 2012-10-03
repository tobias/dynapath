(ns dynapath.core
  "Abstracts the getURLs and addURL functionality of URLClassLoader to a protocol."
  (:import clojure.lang.DynamicClassLoader
           (java.net URL URLClassLoader)))

(defprotocol DynamicClasspath
  (can-read? [cl]
    "Must return true if classpath-urls is implemented.")
  (can-add? [cl]
    "Must return true if add-classpath-url is implemented.")
  (classpath-urls [cl]
    "Returns a seq of the given ClassLoader's URLs.")
  (add-classpath-url [cl url]
    "Adds the url to the classpath of the given ClassLoader."))

(defn addable-classpath?
  "Returns true if the given ClassLoader provides add-claspath-url."
  [cl]
  (and (satisfies? DynamicClasspath cl)
       (can-add? cl)))

(defn readable-classpath?
  "Returns true if the given ClassLoader provides classpath-urls."
  [cl]
  (and (satisfies? DynamicClasspath cl)
       (can-read? cl)))

(def base-readable-addable-classpath
  "A map that provides implementations of can-read? and can-add? that return true.
Useful as a base for a DynamicClasspath implementation."
  {:can-add? (constantly true)
   :can-read? (constantly true)})

(let [base-url-classloader (assoc base-readable-addable-classpath
                             :classpath-urls #(seq (.getURLs %)))]
  (extend URLClassLoader
    DynamicClasspath
    (assoc base-url-classloader
      :add-classpath-url (fn [cl url]
                           (-> URLClassLoader
                               (.getDeclaredMethod "addURL" (into-array Class [URL]))
                               (doto (.setAccessible true))
                               (.invoke cl (into-array URL [url]))))))

  (extend DynamicClassLoader
    DynamicClasspath
    (assoc base-url-classloader
      :add-classpath-url (fn [cl url]
                           (.addURL cl url)))))
