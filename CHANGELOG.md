## 1.1.0 (July 2018)

### Enhancements and Changes

* JeeFoLogger can new be initialized using a builder (old init static methods are now deprecated and will be removed entirely early 2019)
* Minimum log level can now be set for both logcat and persistence
* All the loggers methods are wrapped in try/catch blocks to avoid any possible crashes (If they do generate exception, the messages will still be logged to logcat with no tags)

### Fixes

* Documentation for JeeFoLogger#getAllLogFiles() was stating a wrong pattern for the file name. This is now corrected.
* Added the tag: "[LIB-JEEFO]" which will be used to log messages regarding the library
* Now the logger writing to file is a singleton to improve multi-threading
* SmartLoggerFactory is now returning ILog instead of SmartLogger (which was causing issues with kotlin because SmartLogger is package private)

### Internal

* Reflection is not used anymore