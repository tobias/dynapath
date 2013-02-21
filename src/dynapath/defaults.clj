(ns dynapath.defaults
  "Provides default DynamicClasspath implementations for DynamicClassLoader and URLClassLoader."
  (:use dynapath.dynamic-classpath)
  (:import clojure.lang.DynamicClassLoader
           (java.net URL URLClassLoader)))

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

(defmacro when-resolves
  [sym & body]
  (when (resolve sym)
    `(do ~@body)))

(when-resolves sun.misc.Launcher
   (extend-type sun.misc.Launcher$ExtClassLoader
   DynamicClasspath
   (can-add? [_] false)))
