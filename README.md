# dynapath [![Build Status](https://secure.travis-ci.org/tobias/dynapath.png?branch=master)](https://travis-ci.org/tobias/dynapath)

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

For a Leiningen/Boot project:

    [org.tcrawley/dynapath "1.0.0"]

For a maven project:

    <dependency>
      <groupId>org.tcrawley</groupId>
      <artifactId>dynapath</artifactId>
      <version>0.2.4</version>
    </dependency>

If you need to access or modify the effective classpath:

    (require '[dynapath.util :as dp])
    
    ;; returns a seq of the urls for the classloader. Takes any classloader
    ;; (whether it implements DynamicClasspath or not) and does the right thing
    (dp/classpath-urls a-classloader)
      
    ;; returns a seq of all the urls available from the classloader and its 
    ;; parentage chain
    (dp/all-classpath-urls a-classloader)
    
    ;; adds a url to the given classloader if it is addable
    (dp/add-classpath-url a-classloader a-url)

Loading the `dynapath.defaults` namespace will automatically implement 
`classpath-urls` and `add-classpath-url` for `clojure.lang.DynamicClassLoader` and
`classpath-urls` for `java.net.URLClassLoader`.

If you need to implement `DynamicClasspath`:

    (require '[dynapath.dynamic-classpath :as dc])
    
    (extend-type AReadableButNotModifiableClassLoader
      dc/DynamicClasspath
      (can-read? [_] true)
      (can-add? [_] false)
      (classpath-urls [cl] (seq ...)))

    (extend AReadableAndModifiableClassLoader
      dc/DynamicClasspath
      (assoc dc/base-readable-addable-classpath ;; implements can-read? and can-add?
             :classpath-urls (fn [cl] ...)
             :add-classpath-url (fn [cl url] ...)))

## Note on URLClassLoader

Prior versions of dynapath implemented `add-classpath-url` for
`java.net.URLClassLoader`. Doing so required reflective access to its
protected `addURL` method, which would result in a warning printed to
stdout under Java 9. To prevent that, that implementation has been
removed, and libraries that were relying on that behavior should
instead ensure they have a modifiable classloader as high in the
classloader tree as they can control. For example,
[boot implements its own classloader](https://github.com/boot-clj/boot/commit/a046a497a8bb7f3d1e7aa8d4db4a81c51beaef7d)
to do this.

## Who's using it?

* [bultitude](https://github.com/Raynes/bultitude)
* [immutant](https://github.com/immutant/immutant)
* [ritz](https://github.com/pallet/ritz)
* [tair-repl](https://github.com/xumingming/tair-repl)
* [pomegranate](https://github.com/cemerick/pomegranate)
* [boot](http://boot-clj.com/)

Are you using it? If so, add yourself to this list and send me a PR.

## License

Copyright © 2012-2017 Tobias Crawley

Distributed under the Eclipse Public License.
