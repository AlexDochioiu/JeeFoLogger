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

import android.support.annotation.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Created by Alexandru Iustin Dochioiu on 5/27/2018
 */
abstract class AbstractScopedLogger implements ILog{
    final static String TAG_KEY_INSTANCE = "Instance";
    final static String TAG_KEY_CLASS = "Class";
    final static String TAG_KEY_METHOD = "Method";

    LinkedList<StackTraceElement> traceElements = null;
    private String loggingPrefix = "";
    protected ILog logger;

    // required when we search for the method call, use full name so we avoid big
    // problems caused by obfuscation
    final String fullClassName;


    AbstractScopedLogger() {
        this.fullClassName = SmartLoggerUtils.getFullClassName(5);
    }

    final String getLoggingPrefix() {
        return loggingPrefix;
    }

    abstract String getMessageLogPrefix();

    /**
     * Used for adding tags to the {@link ILog}. The format of the tag: "[KEY VALUE]"
     *
     * @param key                     the {@link String} matching the KEY field
     * @param value                   the {@link String} matching the VALUE field
     * @param throwOnNullOrEmptyValue boolean indicating whether a null value param is acceptable
     */
    final synchronized void addTag(String key, String value, boolean throwOnNullOrEmptyValue) {
        if (value == null || value.equals("")) {
            if (throwOnNullOrEmptyValue) {
                throw new IllegalArgumentException("value should be non-null, non-empty string");
            } else {
                return;
            }
        }

        loggingPrefix += String.format(Locale.UK, "[%s %s]", key, value);
    }

    /**
     * Used for initiating the {@link ILog} member using the passed param if not null; otherwise
     * a new {@link JeefoLogger} is created
     *
     * @param logger the {@link ILog} to be stored or null to create a new instance of {@link JeefoLogger}
     */
    final synchronized void initLogger(@Nullable ILog logger) {
        if (logger == null) {
            this.logger = new JeefoLogger();
        } else {
            this.logger = logger;
        }
    }

    synchronized void DebugReflection(final LinkedList<StackTraceElement> traceElements, String messageToLog, Object... args) {
        this.traceElements = traceElements;

        try {
            logger.getClass().getSuperclass().getDeclaredMethod("DebugReflection",
                    LinkedList.class, String.class, Object[].class)
                    .invoke(logger, this.traceElements, getMessageLogPrefix() + messageToLog, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            logger.Debug(getMessageLogPrefix() + messageToLog, args);
        }
    }

    synchronized void DebugReflection(final LinkedList<StackTraceElement> traceElements, Exception exception, String messageToLog, Object... args) {
        this.traceElements = traceElements;

        try {
            logger.getClass().getSuperclass().getDeclaredMethod("DebugReflection",
                    LinkedList.class, Exception.class, String.class, Object[].class)
                    .invoke(logger, this.traceElements, exception, getMessageLogPrefix() + messageToLog, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            logger.Debug(exception, getMessageLogPrefix() + messageToLog, args);
        }
    }

    synchronized void InfoReflection(final LinkedList<StackTraceElement> traceElements, String messageToLog, Object... args) {
        this.traceElements = traceElements;

        try {
            logger.getClass().getSuperclass().getDeclaredMethod("InfoReflection",
                    LinkedList.class, String.class, Object[].class)
                    .invoke(logger, this.traceElements, getMessageLogPrefix() + messageToLog, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            logger.Info(getMessageLogPrefix() + messageToLog, args);
        }
    }

    synchronized void WarnReflection(final LinkedList<StackTraceElement> traceElements, String messageToLog, Object... args) {
        this.traceElements = traceElements;

        try {
            logger.getClass().getSuperclass().getDeclaredMethod("WarnReflection",
                    LinkedList.class, String.class, Object[].class)
                    .invoke(logger, this.traceElements, getMessageLogPrefix() + messageToLog, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            logger.Warn(getMessageLogPrefix() + messageToLog, args);
        }
    }

    synchronized void WarnReflection(final LinkedList<StackTraceElement> traceElements, Exception exception, String messageToLog, Object... args) {
        this.traceElements = traceElements;

        try {
            logger.getClass().getSuperclass().getDeclaredMethod("WarnReflection",
                    LinkedList.class, Exception.class, String.class, Object[].class)
                    .invoke(logger, this.traceElements, exception, getMessageLogPrefix() + messageToLog, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            logger.Warn(exception, getMessageLogPrefix() + messageToLog, args);
        }
    }

    synchronized void ErrorReflection(final LinkedList<StackTraceElement> traceElements, String messageToLog, Object... args) {
        this.traceElements = traceElements;

        try {
            logger.getClass().getSuperclass().getDeclaredMethod("ErrorReflection",
                    LinkedList.class, String.class, Object[].class)
                    .invoke(logger, this.traceElements, getMessageLogPrefix() + messageToLog, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            logger.Error(getMessageLogPrefix() + messageToLog, args);
        }
    }

    synchronized void ErrorReflection(final LinkedList<StackTraceElement> traceElements, Exception exception, String messageToLog, Object... args) {
        this.traceElements = traceElements;

        try {
            logger.getClass().getSuperclass().getDeclaredMethod("ErrorReflection",
                    LinkedList.class, Exception.class, String.class, Object[].class)
                    .invoke(logger, this.traceElements, exception, getMessageLogPrefix() + messageToLog, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            logger.Error(exception, getMessageLogPrefix() + messageToLog, args);
        }
    }
}
