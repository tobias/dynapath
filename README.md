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

    [org.tcrawley/dynapath "0.2.5"]

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
`classpath-urls` and `add-classpath-url` for `URLClassLoader` and 
`DynamicClassLoader`.

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

## Note on Java 9

If you are using Java 9, you'll have to use dynapath 0.2.5. And under
Java 9, `URLClassLoader` instances won't be modifiable via dynapath by
default, since it uses reflection to access the protected method
`addURL`, and security changes prevent calling `.setAccessible` on
it. You can work around that by passing `----add-opens
java.base/java.net=ALL-UNNAMED` to `java`.

If you are building dynapath under Java 9, you'll have to provide that
options for the tests to pass:

```
JVM_OPTS="--add-opens java.base/java.net=ALL-UNNAMED" lein test-all
```

## Who's using it?

* [bultitude](https://github.com/Raynes/bultitude)
* [immutant](https://github.com/immutant/immutant)
* [ritz](https://github.com/pallet/ritz)
* [tair-repl](https://github.com/xumingming/tair-repl)
* [pomegranate](https://github.com/cemerick/pomegranate)
* [boot](http://boot-clj.com/)

Are you using it? If so, add yourself to this list and send me a PR.

## License

Copyright © 2012-2016 Tobias Crawley

Distributed under the Eclipse Public License.
