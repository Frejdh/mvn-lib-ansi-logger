Changelog
-
# 1.2.0
* Fixed "paths" variable.
* Fixed unit tests. Now uses Junit 5
* Other dependency updates

# 1.1.0
* Environmental variables for setting packages/classes to log. Default global is `WARN`.
* The class `Throwable` is now used instead of `Exception` when logging stacktraces.

# 1.0.1
* Migrated from [https://github.com/fusesource/jansi](Jansi) to [https://github.com/dialex/JColor](JColor)
* Added the class `AnsiColorBuilder` that allows building custom Ansi color objects
* New exception `ColorCodeOutOfRange` for validating the range of said color object
* Added `critical`, `major` and `trace` level logging
* More ansi colors that can be selected in the default class
* 8 bit depth support for ansi colors. All terminals don't support this.
* Refactoring
* Small tests were added
* Support for array and list objects

# 1.0.0
First version
