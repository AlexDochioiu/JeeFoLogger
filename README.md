# JeeFo Logger

[![bintray](https://api.bintray.com/packages/jeefo12/JLogger/jeefologger/images/download.svg) ](https://bintray.com/jeefo12/JLogger/jeefologger/_latestVersion)
[![License](https://img.shields.io/badge/License-Apache-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

## In this repository you can find an example project going through most of the possibilities of this class and the java source code for the JeeFo Logger, which currently runs only on Android. 

JeeFo Logger is an android library which would drasticly improve the quality of your log messages and it allows persistence for log files.

## Features
* **Easily-Identifiable Logs:** All your log messages will now have a constant tag: "[JeeFo-Log]" in logcat so filtering your custom messages from all the other android framework messages will become a piece of cake
* **Persistence:** With a simple line of code, all your log messages will automatically be stored on your Android device and will be easily fetched at any point, even months later. The files have the date baked in both the log messages and the file name (one file per day).
* **Formatting:** As our time is too precious to manually String.format() for every single log message, this is now built-in behaviour with this logger.
* **Logging Exceptions:** To further simplify our life, all the log levels (except Info) can take an exception as a first parameter. This would end up adding its message at the end of that log entry.
* **Scoping:** Your messages become much more than simple piece of information regarding an event. They now hold the power of tracing its source automatically and then indicating it through a chain of class,method,instance tags.
* **Debugging:** We all want the best debugging tools available so we don't waste our pracious times. The LazyLogger might come in handy here, providing static calls for logging messages. The difference is that those messages will automatically have an easily readable trace of relevant classes and methods added as tags.
* **Persistent Tags:** You can easily add or remove sticky tags which will appear on evey single message you log. This can be useful for cases such as logged in user tag: "[USER aa@mail.xyz]"

## Why would you want the JeeFo Logger?
* Easy filtering for your log messages
* Easy formatting for your log messages
* Better messages with smart tags for easy tracking the event's source
* Incredibly easy debugging with the LazyLogger
* Persistent log files which you can easily go back to at any point (this is incredibly useful for in-house testing, especially if you combine it with an interceptor for Thread Exceptions which would log the message, cause and the stack trace. Adding an easy way to send the log file as an e-mail directly from the app might also be useful.)
* All of this comes with a very small price in performance. Logging a message is only around 3 times slower than the Android standard logger, which means it still takes less than half a millisecond for your log messages.

## Current Limitations (they are planned to be solved in further releases)
* The persisted log files are not secured, and can be easily accessible with a file manager. It is currently recommended that persistence is disabled on the official releases until this is fixed.
* There is no possibility of adding a tag for the current thread
* There is no possibility of adding a custom tag to an ILog instance

## Installation with Android Gradle
```groovy
// Add JeeFo Logger dependencies
dependencies {
  compile 'com.jeefo.android:jeefologger:1.1.0'
}
```

## Initializing LazyLogger and Persistence
**In order to use LazyLogger and/or to persist the logs, you must initilize them. This needs doing only once using the JeeFoLogger builder. (This is not required unless you want to use persistence, lazylogger or to set the minimum logcat logging level different than VERBOSE)**

```groovy
new JeefoLogger.Builder(context)
                .withPersistence(true) // default is false
                .withLazyLogger(true) // default is false
				.withMinimumLogcatLevel(LogLevel.VERBOSE) // default is LogLevel.VERBOSE
                .withMinimumPersistenceLevel(LogLevel.VERBOSE) // default is LogLevel.VERBOSE (this line will not do anything unless withPeristence(true) is used)
                .buildAndInit();
```

You must provide an Android `context`. A good place to initialize them is in `onCreate` on an application subclass:
```groovy
public class MyApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    JeefoLogger.initPersistence(this);
    JeefoLogger.initLazyLogger(this);
  }
}
```

If you create your own application subclass, you must add it to the app’s `AndroidManifest.xml`:
```groovy
<application
  android:name=".MyApplication"
  ...
/>
```

## Logging Flavours

### LazyLogger
**Before you can use the LazyLogger, you must initialize it. See section above on how to do it.**

The lazy logger provides a static way of logging messages. It determins the entire trace at runtime and logs a message with tags formed out of it. This is a very powerful debugging tool which requires almost no preparation before using it. To get a better idea of its output, check the examples below!

#### LazyLogger: Example 1 (usual case)

```groovy
public class MainActivity extends AppCompatActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		LazyLogger.Info("This is log message number %d", 1);
		MyUtils.logErrorMessage();
	}
}

public class MyUtils {
	public static void logErrorMessage() {
		LazyLogger.Error("This is log message number %d", 2);
		logThirdMessage();
	}
	
	static void logThirdMessage() {
        LazyLogger.Warn("This is log message number %d", 3);
    }
}
```

The result of running this is:
```
06-09 22:54:00.878 4079-4079/com.jeefo.android.logger I/[JeeFo-Log]: [Class MainActivity][Method onCreate] This is log message number 1
06-09 22:54:00.888 4079-4079/com.jeefo.android.logger E/[JeeFo-Log]: [Class MainActivity][Method onCreate][Class MyUtils][Method logErrorMessage] This is log message number 2
06-09 22:54:00.888 4079-4079/com.jeefo.android.logger W/[JeeFo-Log]: [Class MainActivity][Method onCreate][Class MyUtils][Method logErrorMessage][Method logThirdMessage()] This is log message number 3
```

#### LazyLogger: Example 2 (with anonymous classes)

```groovy
public interface IListener {
    void onEvenetHappened();
}

public class MainActivity extends AppCompatActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyUtils.requestToRunMyAnonymousMethod(new IListener() {
            @Override
            public void onEvenetHappened() {
                LazyLogger.Debug("This is my log message");
            }
        });
	}
}

public class MyUtils {
	public static void requestToRunMyAnonymousMethod(IListener listener) {
        runMyAnonymousMethod(listener);
    }

    static void runMyAnonymousMethod(IListener listener) {
        listener.onEvenetHappened();
    }
}
```

The result of running this is:
```
06-09 23:01:38.298 4251-4251/com.jeefo.android.logger D/[JeeFo-Log]: [Class MainActivity][Method onCreate][Method onEvenetHappened() <- MyUtils#requestToRunMyAnonymousMethod()#runMyAnonymousMethod()] This is my log message
```

To explain this a bit. We better split this in two parts (with a small intersection):
* `[Class MainActivity][Method onCreate][Method onEvenetHappened()` Tells us we ran `onEvenetHappened()` which is defined inside `MainActivity -> onCreate()`
* `Method onEvenetHappened() <- MyUtils#requestToRunMyAnonymousMethod()#runMyAnonymousMethod()` tells us we ran `onEvenetHappened()` which was triggered by `MyUtils` class and the two methods in there.

### SmartLogger

This is an `ILog` instance and gives you a bit more control over the scoping. It is not as smart as the lazy logger, providing a good combination of power and control.

The smart logger determines the class when the instance is created and the method(s) when the message is logged.

#### SmartLogger: Example 1 (usual case)

```groovy
public class MainActivity extends AppCompatActivity {
	private ILog logger = SmartLoggerFactory.createSmartLogger();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		logger.Info("This is log message number %d", 1);
		MyUtils.logErrorMessage(logger);
	}
}

public class MyUtils {
	public static void logErrorMessage(ILog logger) {
        logger = SmartLoggerFactory.createSmartLogger(logger);
        logger.Error("This is log message number %d", 2);
        logThirdMessage(logger);
    }

    static void logThirdMessage(ILog logger) {
        logger.Warn("This is log message number %d", 3);
    }
}
```

The result (in logcat) of running this is:
```
06-09 23:16:28.558 4483-4483/com.jeefo.android.logger I/[JeeFo-Log]: [Class MainActivity][Method onCreate] This is log message number 1
06-09 23:16:28.558 4483-4483/com.jeefo.android.logger E/[JeeFo-Log]: [Class MainActivity][Method onCreate] [Class MyUtils][Method logErrorMessage] This is log message number 2
06-09 23:16:28.568 4483-4483/com.jeefo.android.logger W/[JeeFo-Log]: [Class MainActivity][Method onCreate] [Class MyUtils][Method logErrorMessage#logThirdMessage] This is log message number 3
```

#### SmartLogger: Example 2 (with anonymous classes)

```groovy
public interface IListener {
    void onEvenetHappened();
}

public class MainActivity extends AppCompatActivity {
	private ILog logger = SmartLoggerFactory.createSmartLogger();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyUtils.requestToRunMyAnonymousMethod(new IListener() {
            @Override
            public void onEvenetHappened() {
                logger.Debug("This is my log message");
            }
        });
	}
}

public class MyUtils {
	public static void requestToRunMyAnonymousMethod(IListener listener) {
        runMyAnonymousMethod(listener);
    }

    static void runMyAnonymousMethod(IListener listener) {
        listener.onEvenetHappened();
    }
}
```

The result (in logcat) of running this is:
```
06-09 23:18:48.028 4625-4625/com.jeefo.android.logger D/[JeeFo-Log]: [Class MainActivity][Method onEvenetHappened <- MyUtils#runMyAnonymousMethod()] This is my log message
```

To explain this a bit. We better split this in two parts (with a small intersection):
* `[Class MainActivity][Method onEvenetHappened()` Tells us we ran `onEvenetHappened()` which is defined inside `MainActivity'
* `Method onEvenetHappened() <- MyUtils#runMyAnonymousMethod()` tells us we ran `onEvenetHappened()` which was triggered by `MyUtils` class in the `runMyAnonymousMethod()` method.

#### SmartLogger: Example 3 (with instance tag)
It is often that you might be interested in a way of identifying whether two log messages generated in the same place have been generated by the same instance of an object. (Assuming you use the logger inside a fragment. You might want to have an instance tag for the logger to idenify whether the instance of the fragment is the same one as 5 minutes beforehand, or it is a new one)

```groovy
public class MainActivity extends AppCompatActivity {
	private ILog logger = SmartLoggerFactory.createSmartLogger();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyClass firstObject = new MyClass(logger);
        MyClass secondObject = new MyClass(logger);

        firstObject.logMeADebug();
        secondObject.logMeAWarn();
        firstObject.logMeAWarn();
	}
}

public class MyClass {
    private final ILog logger;

    public MyClass(ILog logger) {
        this.logger = SmartLoggerFactory.createSmartLogger(logger, true); // true to get instance tag
    }

    public void logMeAWarn() {
        logger.Warn("Warn message with instance");
    }

    public void logMeADebug() {
        logger.Debug("Debug message with instance");
    }
}
```

The result (in logcat) of running this is:
```
06-09 23:31:55.328 4946-4946/com.jeefo.android.logger D/[JeeFo-Log]: [Class MainActivity][Method onCreate] [Class MyClass][Instance 8c6f76][Method logMeADebug] Debug message with instance
06-09 23:31:55.328 4946-4946/com.jeefo.android.logger W/[JeeFo-Log]: [Class MainActivity][Method onCreate] [Class MyClass][Instance 02a485][Method logMeAWarn] Warn message with instance
06-09 23:31:55.328 4946-4946/com.jeefo.android.logger W/[JeeFo-Log]: [Class MainActivity][Method onCreate] [Class MyClass][Instance 8c6f76][Method logMeAWarn] Warn message with instance
```

As you can notice, the instance tag for the first and last message is the same, being generated by the same object. The middle one has a different tag, due to the different object generating the message.

### ScopedLogger

This is an `ILog` instance and gives you a the most control over the scoping. It is not very smart but it is fully predictable and with it you cannot go wrong.

#### ScopedLogger: Example 1 (usual case)

```groovy
public class MainActivity extends AppCompatActivity {
	// for static loggers it is: private static ILog logger = new ScopedLogger(MainActivity.class);
	private ILog logger = new ScopedLogger(this.getClass(), true); // true to add instance tag

	@Override
	public void onCreate(Bundle savedInstanceState) {
		logger.Error("");
        logger.Info("This is log message number %d", 1);
        ILog loggerWithMethod = new ScopedLogger(logger, "onCreate");
        loggerWithMethod.Debug("This is a log message with method scope");

        MyUtils.logErrorMessage(loggerWithMethod);
	}
}

public class MyUtils {
	public static void logErrorMessage(ILog logger) {
        logger = new ScopedLogger(logger, MyUtils.class, "logErrorMessage"); // method should be null if you create a member logger in the constructor
        logger.Error("This is log message number %d", 2);
        logThirdMessage(logger);
    }

    static void logThirdMessage(ILog logger) {
        logger = new ScopedLogger(logger, "logThirdMessage");
        logger.Warn("This is log message number %d", 3);
    }
}
```

The result (in logcat) of running this is:
```
06-09 23:44:45.128 5380-5380/com.jeefo.android.logger I/[JeeFo-Log]: [Class MainActivity][Instance 399d02] This is log message number 1
06-09 23:44:45.138 5380-5380/com.jeefo.android.logger D/[JeeFo-Log]: [Class MainActivity][Instance 399d02][Method onCreate] This is a log message with method scope
06-09 23:44:45.148 5380-5380/com.jeefo.android.logger E/[JeeFo-Log]: [Class MainActivity][Instance 399d02][Method onCreate][Class MyUtils][Method logErrorMessage] This is log message number 2
06-09 23:44:45.148 5380-5380/com.jeefo.android.logger W/[JeeFo-Log]: [Class MainActivity][Instance 399d02][Method onCreate][Class MyUtils][Method logErrorMessage][Method logThirdMessage] This is log message number 3
```

#### ScopedLogger: Example 2 (with anonymous classes)

```groovy
public interface IListener {
    void onEvenetHappened();
}

public class MainActivity extends AppCompatActivity {
	private ILog logger = new ScopedLogger(this.getClass(), true);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		final ILog loggerWithMethod = new ScopedLogger(logger, "onCreate");

        MyUtils.requestToRunMyAnonymousMethod(new IListener() {
            @Override
            public void onEvenetHappened() {
                ILog anonLogger = new ScopedLogger(loggerWithMethod, this.getClass(), "onEventHappened");
                anonLogger.Debug("This is my log message");
            }
        });	}
}

public class MyUtils {
	public static void requestToRunMyAnonymousMethod(IListener listener) {
        runMyAnonymousMethod(listener);
    }

    static void runMyAnonymousMethod(IListener listener) {
        listener.onEvenetHappened();
    }
}
```

The result (in logcat) of running this is:
```
06-09 23:49:30.598 5553-5553/com.jeefo.android.logger D/[JeeFo-Log]: [Class MainActivity][Instance dd1496][Method onCreate][Method onEventHappened] This is my log message
```

As you can see, there is info provided on what is getting run, but nothing really on who triggered it.

#### ScopedLogger: Example 3 (with instance tag)
It is often that you might be interested in a way of identifying whether two log messages generated in the same place have been generated by the same instance of an object. (Assuming you use the logger inside a fragment. You might want to have an instance tag for the logger to idenify whether the instance of the fragment is the same one as 5 minutes beforehand, or it is a new one)

```groovy
public class MainActivity extends AppCompatActivity {
	private ILog logger = new ScopedLogger(this.getClass(), true);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyClass firstObject = new MyClass(logger);
        MyClass secondObject = new MyClass(logger);

        firstObject.logMeADebug();
        secondObject.logMeAWarn();
        firstObject.logMeAWarn();
	}
}

public class MyClass {
    private final ILog logger;

    public MyClass(ILog logger) {
        this.logger = new ScopedLogger(logger, this.getClass(), true);
    }

    public void logMeAWarn() {
        ILog methodLogger = new ScopedLogger(logger, "logMeAWarn()");
        methodLogger.Warn("Warn message with instance");
    }

    public void logMeADebug() {
        // If you do not want a new logger, just use the member (no method tag will be added)
        logger.Debug("Debug message with instance");
    }
}
```

The result (in logcat) of running this is:
```
06-09 23:56:41.198 5754-5754/? D/[JeeFo-Log]: [Class MainActivity][Instance 7d351e][Class MyClass][Instance 9a5845] Debug message with instance
06-09 23:56:41.208 5754-5754/? W/[JeeFo-Log]: [Class MainActivity][Instance 7d351e][Class MyClass][Instance 740ac0][Method logMeAWarn()] Warn message with instance
06-09 23:56:41.208 5754-5754/? W/[JeeFo-Log]: [Class MainActivity][Instance 7d351e][Class MyClass][Instance 9a5845][Method logMeAWarn()] Warn message with instance
```

All the three messages have the same instance tag for the MainActivity, however, the MyClass tag for the second log message is different (as expected because it's a different object). It can also be noticed that the `logMeADebug()` method does not add a method tag. That's because we used the member logger.

### ScopedLogger combined with SmartLogger

As they are both `ILog` instances, they can actually be combined for better functionality (and predictability).

#### ScopedLogger combined with SmartLogger: Example 1 (usual case)

```groovy
public class MainActivity extends AppCompatActivity {
	// for static loggers it is: private static ILog logger = new ScopedLogger(MainActivity.class);
	private ILog logger = new ScopedLogger(this.getClass(), true); // true to add instance tag

	@Override
	public void onCreate(Bundle savedInstanceState) {
		logger.Error("");
        logger.Info("This is log message number %d", 1);
        ILog loggerWithMethod = new ScopedLogger(logger, "onCreate");
        loggerWithMethod.Debug("This is a log message with method scope");

        MyUtils.logErrorMessage(loggerWithMethod);
	}
}

public class MyUtils {
	public static void logErrorMessage(ILog logger) {
		// we now create a SmartLogger from a ScopedLogger so we don't need to provide the methods manually
        logger = SmartLoggerFactory.createSmartLogger(logger);
        logger.Error("This is log message number %d", 2);
        logThirdMessage(logger);
    }

    static void logThirdMessage(ILog logger) {
        logger.Warn("This is log message number %d", 3);
    }
}
```

The result (in logcat) of running this is:
```
06-10 00:04:18.868 5965-5965/com.jeefo.android.logger D/[JeeFo-Log]: [Class MainActivity][Instance 03909c][Method onCreate] This is a log message with method scope
06-10 00:04:18.878 5965-5965/com.jeefo.android.logger E/[JeeFo-Log]: [Class MainActivity][Instance 03909c][Method onCreate][Class MyUtils][Method logErrorMessage] This is log message number 2
06-10 00:04:18.878 5965-5965/com.jeefo.android.logger W/[JeeFo-Log]: [Class MainActivity][Instance 03909c][Method onCreate][Class MyUtils][Method logErrorMessage#logThirdMessage] This is log message number 3
```

#### ScopedLogger combined with SmartLogger: Example 2 (with anonymous classes)

```groovy
public interface IListener {
    void onEvenetHappened();
}

public class MainActivity extends AppCompatActivity {
	private ILog logger = new ScopedLogger(this.getClass(), true);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		final ILog loggerWithMethod = new ScopedLogger(logger, "onCreate");

        MyUtils.requestToRunMyAnonymousMethod(new IListener() {
            @Override
            public void onEvenetHappened() {
                ILog anonLogger = SmartLoggerFactory.createSmartLogger(loggerWithMethod);
                anonLogger.Debug("This is my log message");
            }
        });	}
}

public class MyUtils {
	public static void requestToRunMyAnonymousMethod(IListener listener) {
        runMyAnonymousMethod(listener);
    }

    static void runMyAnonymousMethod(IListener listener) {
        listener.onEvenetHappened();
    }
}
```

The result (in logcat) of running this is:
```
06-10 00:06:30.928 6104-6104/com.jeefo.android.logger D/[JeeFo-Log]: [Class MainActivity][Instance 898aaf][Method onCreate][Class MainActivity][Method onEvenetHappened <- MyUtils#runMyAnonymousMethod()] This is my log message
```

As you can see, there is info provided on where it started: `onCreate()`, where the ran method can be found: `[Class MainActivity][Method onEvenetHappened` as well as who triggered it: `<- MyUtils#runMyAnonymousMethod()`.

#### ScopedLogger combined with SmartLogger: Example 3 (with instance tag)
It is often that you might be interested in a way of identifying whether two log messages generated in the same place have been generated by the same instance of an object. (Assuming you use the logger inside a fragment. You might want to have an instance tag for the logger to idenify whether the instance of the fragment is the same one as 5 minutes beforehand, or it is a new one)

```groovy
public class MainActivity extends AppCompatActivity {
	private ILog logger = new ScopedLogger(this.getClass(), true);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyClass firstObject = new MyClass(logger);
        MyClass secondObject = new MyClass(logger);

        firstObject.logMeADebug();
        secondObject.logMeAWarn();
        firstObject.logMeAWarn();
	}
}

public class MyClass {
    private final ILog logger;

    public MyClass(ILog logger) {
        this.logger = SmartLoggerFactory.createSmartLogger(logger, true);
    }

    public void logMeAWarn() {
        logger.Warn("Warn message with instance");
    }

    public void logMeADebug() {
        logger.Debug("Debug message with instance");
    }
}
```

The result (in logcat) of running this is:
```
06-10 00:11:41.258 6270-6270/com.jeefo.android.logger W/[JeeFo-Log]: [Class MainActivity][Instance bc1d3c][Class MyClass][Instance 09e77c][Method logMeADebug] Debug message with instance
06-10 00:11:41.258 6270-6270/com.jeefo.android.logger W/[JeeFo-Log]: [Class MainActivity][Instance bc1d3c][Class MyClass][Instance d50e23][Method logMeAWarn] Warn message with instance
06-10 00:11:41.268 6270-6270/com.jeefo.android.logger W/[JeeFo-Log]: [Class MainActivity][Instance bc1d3c][Class MyClass][Instance 09e77c][Method logMeAWarn] Warn message with instance
```

All the three messages have the same instance tag for the MainActivity, however, the MyClass tag for the second log message is different (as expected because it's a different object). As we created a `SmartLogger` inside `MyClass`, we now have the method tag added automatically.
