# dynapath

dynapath provides a protocol and util functions for class loaders that
make their effective classpaths readable and/or modifiable.

## Rationale 

Clojure uses a `clojure.lang.DynamicClassLoader` by default (an
extension of `java.net.URLClassLoader`), which provides `.getURLs` for
reading the effective classpath and `.addURL` for modifying it. It's
common for projects that need to read or modify the effective
classpath to assume that a `URLClassLoader` is always available. But
in some environments, the available class loader may not be a
`URLClassLoader`, and may not be readable or modifiable.

Some projects (notably `pomegranate`) handle this by providing a
protocol that can be implemented for other class loaders that may
provide similar functionality.

dynapath provides a protocol that is based on an extraction of
pomegranate's protocol, and is intended to be a standard way for
accessing or modifying the effective classpath. Using dynapath in your
library instead of assuming a class loader or implementing your own
protocol provides the following benefits:

* Your library can work with any modifiable/readable class loader
  without any changes
* Any project that has already implemented `DynamicClasspath` for
  whatever esoteric class loader they are using will not need any
  other changes to use your library as well

## Usage

Add it as a dependency:

    (defproject einzelfahrausweis "1.2.3"
      :description "Yet another hard to pronounce clojure project"
      :dependencies [[dynapath "0.1.0"]])
    
If you need to access or modify the effective classpath:

    (require '[dynapath.core :as dp])
    
    (if (dp/readable-classpath? a-classloader)
      (classpath-urls a-classloader))
      
    (if (dp/addable-classpath? a-classloader)
      (add-classpath-url a-classloader a-url))  
      
Loading the `dynapath.core` namespace will automatically implement 
`classpath-urls` and `add-classpath-url` for `URLClassLoader` and 
`DynamicClassLoader`.

If you need to implement `DynamicClasspath`:

    (require '[dynapath.core :as dp])
    
    (extend-type AReadableButNotModfiableClassLoader
      dp/DynamicClasspath
      (can-read? [_] true)
      (can-add? [_] false)
      (classpath-urls [cl] (seq ...)))

    (extend AReadableAndModifiableClassLoader
      dp/DynamicClasspath
      (assoc dp/base-readable-addable-classpath ;; implements can-read? and can-add?
             :classpath-urls (fn [cl] ...)
             :add-classpath-url (fn [cl url] ...)))
             
## Who's using it?

* [immutant](https://github.com/immutant/immutant)
* [ritz](https://github.com/pallet/ritz)

There are currently pending pull requests for:

* [pomegranate](https://github.com/cemerick/pomegranate)
* [bultitude](https://github.com/Raynes/bultitude)

Are you using it? If so, add yourself to this list and send me a PR.

## License

Copyright © 2012 Tobias Crawley

Distributed under the Eclipse Public License.
