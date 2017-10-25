(ns dynapath.defaults
  "Provides default DynamicClasspath implementations for DynamicClassLoader and URLClassLoader."
  (:require [dynapath.dynamic-classpath :as dc])
  (:import clojure.lang.DynamicClassLoader
           java.net.URLClassLoader))

(let [base-url-classloader (assoc dc/base-readable-addable-classpath
                                  :classpath-urls #(seq (.getURLs ^URLClassLoader %)))]
  (when-not (extends? dc/DynamicClasspath URLClassLoader)
    (extend URLClassLoader
      dc/DynamicClasspath
      (assoc base-url-classloader
             :can-add? (constantly false)))

    (extend DynamicClassLoader
      dc/DynamicClasspath
      (assoc base-url-classloader
             :add-classpath-url (fn [^DynamicClassLoader cl url]
                                  (.addURL cl url))))))

