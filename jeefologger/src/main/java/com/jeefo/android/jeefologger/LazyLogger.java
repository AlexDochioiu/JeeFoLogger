/*
 * Copyright 2018 Alexandru Iustin Dochioiu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.jeefo.android.jeefologger;

import android.util.Log;

/**
 * Created by Alexandru Iustin Dochioiu on 6/9/2018
 * <p>
 * Static class used for accessing the power of the {@link LazyLoggerInternal}
 * NOTE: In order to use those static methods, you NEED to initialize the lazy logger beforehand
 * --JeefoLogger.initLazyLogger(Context)--
 */
public class LazyLogger {
    private static final ILog lazyLoggerImplementation = new LazyLoggerInternal();
    static String packageName = null;

    /**
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    public static void Verbose(String messageToLog, Object... args) {
        try {
            lazyLoggerImplementation.Verbose(messageToLog, args);
        } catch (Exception libraryException) {
            Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, StringUtils.getFormattedMessage(libraryException, "LazyLogger"));
            Log.v(FinalLogger.TAG_LOGGING_PREFIX, StringUtils.getFormattedMessage(null, messageToLog, args));
        }
    }

    /**
     * @param exception    the exception to be logged
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    public static void Verbose(Exception exception, String messageToLog, Object... args) {
        try {
            lazyLoggerImplementation.Verbose(exception, messageToLog, args);
        } catch (Exception libraryException) {
            Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, StringUtils.getFormattedMessage(libraryException, "LazyLogger"));
            Log.v(FinalLogger.TAG_LOGGING_PREFIX, StringUtils.getFormattedMessage(exception, messageToLog, args));
        }
    }

    /**
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    public static void Debug(String messageToLog, Object... args) {
        try {
            lazyLoggerImplementation.Debug(messageToLog, args);
        } catch (Exception libraryException) {
            Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, StringUtils.getFormattedMessage(libraryException, "LazyLogger"));
            Log.d(FinalLogger.TAG_LOGGING_PREFIX, StringUtils.getFormattedMessage(null, messageToLog, args));
        }
    }

    /**
     * @param exception    the exception to be logged
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    public static void Debug(Exception exception, String messageToLog, Object... args) {
        try {
            lazyLoggerImplementation.Debug(exception, messageToLog, args);
        } catch (Exception libraryException) {
            Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, StringUtils.getFormattedMessage(libraryException, "LazyLogger"));
            Log.d(FinalLogger.TAG_LOGGING_PREFIX, StringUtils.getFormattedMessage(exception, messageToLog, args));
        }
    }

    /**
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    public static void Info(String messageToLog, Object... args) {
        try {
            lazyLoggerImplementation.Info(messageToLog, args);
        } catch (Exception libraryException) {
            Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, StringUtils.getFormattedMessage(libraryException, "LazyLogger"));
            Log.i(FinalLogger.TAG_LOGGING_PREFIX, StringUtils.getFormattedMessage(null, messageToLog, args));
        }
    }

    /**
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    public static void Warn(String messageToLog, Object... args) {
        try {
            lazyLoggerImplementation.Warn(messageToLog, args);
        } catch (Exception libraryException) {
            Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, StringUtils.getFormattedMessage(libraryException, "LazyLogger"));
            Log.w(FinalLogger.TAG_LOGGING_PREFIX, StringUtils.getFormattedMessage(null, messageToLog, args));
        }
    }

    /**
     * @param exception    the exception to be logged
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    public static void Warn(Exception exception, String messageToLog, Object... args) {
        try {
            lazyLoggerImplementation.Warn(exception, messageToLog, args);
        } catch (Exception libraryException) {
            Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, StringUtils.getFormattedMessage(libraryException, "LazyLogger"));
            Log.w(FinalLogger.TAG_LOGGING_PREFIX, StringUtils.getFormattedMessage(exception, messageToLog, args));
        }
    }

    /**
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    public static void Error(String messageToLog, Object... args) {
        try {
            lazyLoggerImplementation.Error(messageToLog, args);
        } catch (Exception libraryException) {
            Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, StringUtils.getFormattedMessage(libraryException, "LazyLogger"));
            Log.e(FinalLogger.TAG_LOGGING_PREFIX, StringUtils.getFormattedMessage(null, messageToLog, args));
        }
    }

    /**
     * @param exception    the exception to be logged
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    public static void Error(Exception exception, String messageToLog, Object... args) {
        try {
            lazyLoggerImplementation.Error(exception, messageToLog, args);
        } catch (Exception libraryException) {
            Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, StringUtils.getFormattedMessage(libraryException, "LazyLogger"));
            Log.e(FinalLogger.TAG_LOGGING_PREFIX, StringUtils.getFormattedMessage(exception, messageToLog, args));
        }
    }

    /**
     * @param exception the exception to be logged
     */
    public static void Error(Exception exception) {
        try {
            lazyLoggerImplementation.Error(exception);
        } catch (Exception libraryException) {
            Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, StringUtils.getFormattedMessage(libraryException, "LazyLogger"));
            Log.e(FinalLogger.TAG_LOGGING_PREFIX, StringUtils.getFormattedMessage(exception, ""));
        }
    }

    /**
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    public static void Wtf(String messageToLog, Object... args) {
        try {
            lazyLoggerImplementation.Wtf(messageToLog, args);
        } catch (Exception libraryException) {
            Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, StringUtils.getFormattedMessage(libraryException, "LazyLogger"));
            Log.wtf(FinalLogger.TAG_LOGGING_PREFIX, StringUtils.getFormattedMessage(null, messageToLog, args));
        }
    }

    /**
     * @param exception    the exception to be logged
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    public static void Wtf(Exception exception, String messageToLog, Object... args) {
        try {
            lazyLoggerImplementation.Wtf(exception, messageToLog, args);
        } catch (Exception libraryException) {
            Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, StringUtils.getFormattedMessage(libraryException, "LazyLogger"));
            Log.wtf(FinalLogger.TAG_LOGGING_PREFIX, StringUtils.getFormattedMessage(exception, messageToLog, args));
        }
    }

    /**
     * @param exception the exception to be logged
     */
    public static void Wtf(Exception exception) {
        try {
            lazyLoggerImplementation.Wtf(exception);
        } catch (Exception libraryException) {
            Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, StringUtils.getFormattedMessage(libraryException, "LazyLogger"));
            Log.wtf(FinalLogger.TAG_LOGGING_PREFIX, StringUtils.getFormattedMessage(exception, ""));
        }
    }

    /**
     * Private Constructor to avoid initialization
     */
    private LazyLogger() {
    }
}
