(ns dynapath.defaults
  "Provides default DynamicClasspath implementations for DynamicClassLoader and URLClassLoader."
  (:use dynapath.dynamic-classpath)
  (:import clojure.lang.DynamicClassLoader
           (java.net URL URLClassLoader)))

(defmacro when-resolves
  [sym & body]
  `(when (try
           (resolve ~sym)
           (catch Exception _#))
     (eval '(do ~@body))))

(def ^:private base-url-classloader
  (assoc base-readable-addable-classpath
    :classpath-urls #(seq (.getURLs ^URLClassLoader %))))

(when-not (extends? DynamicClasspath URLClassLoader)
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
      :add-classpath-url (fn [^DynamicClassLoader cl url]
                           (.addURL cl url))))

  ;; on < java 9, the boot classloader is a URLClassLoader, but
  ;; modifying it can have dire consequences
  (when-resolves 'sun.misc.Launcher$ExtClassLoader
    (extend sun.misc.Launcher$ExtClassLoader
      DynamicClasspath
      (assoc base-url-classloader
        :can-add? (constantly false)))))

