# 1.0.0 - 2017-10-25

* BREAKING - Don't allow URLClassLoader to be modified. See the README
  for more details.

# 0.2.5 - 2016-12-09

* Fix URLClassLoader extension to work with Java 9 nightly builds
* Resolve classes at runtime instead of compile time to resolve AOT issues with Java 9

# 0.2.4 - 2016-05-27

* Update default extension to work with Java 9
* Remove reflection warnings

# 0.2.3 - 2013-02-21

* Allow the ExtClassLoader to at least be readable

# 0.2.2 - 2013-02-21

* Disable modification of ExtClassLoader

# Anything older than 0.2.2 is fundamentally broken, and shouldn't be used
