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
import android.util.Log;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by Alexandru Iustin Dochioiu on 5/27/2018
 * <p>
 * Concurrent logger used for creating a pass-through trace which will improve drastically the readability of
 * the logged messages as well as decrease the debugging time when bugs/ crashes arise. For ease of
 * use, the messages contain placeholders ('%s') and String params which will replace them.
 * </p>
 * <p>
 * It logs all the messages to Logcat as well as to a persistent file (if activated).
 * </p>
 * <p>
 * Compared to the {@link ScopedLogger}, this uses a tiny bit more resources but it identifies the
 * method at runtime for every message that is being logger. To have a method tag, the {@link ScopedLogger}
 * would require a new creation at the beginning of every method. However, the {@link SmartLogger} requires
 * a single instance as the class' member and it provides the same piece of information.
 * </p>
 * <p>
 * Compared to the {@link LazyLogger}. this uses less resources as the {@link LazyLogger} goes all the
 * way to identifying the entire trace of method calls through classes up to the point of the logged message.
 * </p>
 * <p>
 * This in combination with the {@link ScopedLogger} are recommended for use in production, whereas the
 * {@link LazyLogger} is designed to mainly be a debugging tool used for quickly tracing the source
 * of any problems during development stage.
 * </p>
 */
class SmartLogger extends AbstractScopedLogger {

    // required when we search for the method call, use full name so we avoid big
    // problems caused by obfuscation
    private final String fullClientClassName;

    /**
     * Constructor
     *
     * @param addInstanceTag whether an instance tag is desired
     */
    SmartLogger(boolean addInstanceTag) {
        initLogger(null);

        String clientClassCaller;

        fullClientClassName = SmartLoggerUtils.getFullClassName(5);
        clientClassCaller = SmartLoggerUtils.getSimpleClassName(5);

        addTag(TAG_KEY_CLASS, clientClassCaller, false);

        if (addInstanceTag) {
            try {
                addTag(TAG_KEY_INSTANCE, UuidCustomUtils.generateShortUUID(), true);
            } catch (IllegalArgumentException e) {
                // I don't really expect to ever get here
                Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, "Failed to generate UID for instance tag");
            }
        }
    }

    /**
     * Constructor
     *
     * @param logger         the {@link IInternalLog} to be extended with the new tags. can be either of
     *                       {@link ScopedLogger}, {@link SmartLogger} or {@link LazyLogger}.
     * @param addInstanceTag whether an instance tag is desired
     */
    SmartLogger(@Nullable IInternalLog logger, boolean addInstanceTag) {
        initLogger(logger);

        String clientClassCaller;

        fullClientClassName = SmartLoggerUtils.getFullClassName(5);
        clientClassCaller = SmartLoggerUtils.getSimpleClassName(5);

        addTag(TAG_KEY_CLASS, clientClassCaller, false);

        if (addInstanceTag) {
            try {
                addTag(TAG_KEY_INSTANCE, UuidCustomUtils.generateShortUUID(), true);
            } catch (IllegalArgumentException e) {
                // I don't really expect to ever get here
                Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, "Failed to generate UID for instance tag");
            }
        }
    }

    /**
     * @return the client class name (if the class is an anonymous class, it will give the class
     * name of the host where it was defined)
     */
    String getFullClientClassName() {
        return fullClientClassName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    String getMessageLogPrefix() {

        final String methodName = SmartLoggerUtils.getMethodsName(fullClientClassName, traceElements);
        StringBuilder messageLogPrefix = new StringBuilder();
        messageLogPrefix.append(getLoggingPrefix());

        if (!methodName.equals("")) {
            messageLogPrefix.append("[").append(TAG_KEY_METHOD).append(" ").append(methodName).append("]");
        }

        messageLogPrefix.append(" ");
        return messageLogPrefix.toString();
    }

    /**
     * {@inheritDoc}
     *
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public void Verbose(String messageToLog, Object... args) {
        try {
            InternalVerbose(new LinkedList<>(Arrays.asList(Thread.currentThread().getStackTrace())), messageToLog, args);
        } catch (Exception libraryException) {
            Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, StringUtils.getFormattedMessage(libraryException, "SmartLogger"));
            Log.v(FinalLogger.TAG_LOGGING_PREFIX, StringUtils.getFormattedMessage(null, messageToLog, args));
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public void Verbose(Exception exception, String messageToLog, Object... args) {
        try {
            InternalVerbose(new LinkedList<>(Arrays.asList(Thread.currentThread().getStackTrace())), exception, messageToLog, args);
        } catch (Exception libraryException) {
            Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, StringUtils.getFormattedMessage(libraryException, "SmartLogger"));
            Log.v(FinalLogger.TAG_LOGGING_PREFIX, StringUtils.getFormattedMessage(exception, messageToLog, args));
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public synchronized void Debug(String messageToLog, Object... args) {
        try {
            InternalDebug(new LinkedList<>(Arrays.asList(Thread.currentThread().getStackTrace())), messageToLog, args);
        } catch (Exception libraryException) {
            Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, StringUtils.getFormattedMessage(libraryException, "SmartLogger"));
            Log.d(FinalLogger.TAG_LOGGING_PREFIX, StringUtils.getFormattedMessage(null, messageToLog, args));
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param exception    the exception to be logged
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public synchronized void Debug(Exception exception, String messageToLog, Object... args) {
        try {
            InternalDebug(new LinkedList<>(Arrays.asList(Thread.currentThread().getStackTrace())), exception, messageToLog, args);
        } catch (Exception libraryException) {
            Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, StringUtils.getFormattedMessage(libraryException, "SmartLogger"));
            Log.d(FinalLogger.TAG_LOGGING_PREFIX, StringUtils.getFormattedMessage(exception, messageToLog, args));
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public synchronized void Info(String messageToLog, Object... args) {
        try {
            InternalInfo(new LinkedList<>(Arrays.asList(Thread.currentThread().getStackTrace())), messageToLog, args);
        } catch (Exception libraryException) {
            Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, StringUtils.getFormattedMessage(libraryException, "SmartLogger"));
            Log.i(FinalLogger.TAG_LOGGING_PREFIX, StringUtils.getFormattedMessage(null, messageToLog, args));
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public synchronized void Warn(String messageToLog, Object... args) {
        try {
            InternalWarn(new LinkedList<>(Arrays.asList(Thread.currentThread().getStackTrace())), messageToLog, args);
        } catch (Exception libraryException) {
            Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, StringUtils.getFormattedMessage(libraryException, "SmartLogger"));
            Log.w(FinalLogger.TAG_LOGGING_PREFIX, StringUtils.getFormattedMessage(null, messageToLog, args));
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param exception    the exception to be logged
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public synchronized void Warn(Exception exception, String messageToLog, Object... args) {
        try {
            InternalWarn(new LinkedList<>(Arrays.asList(Thread.currentThread().getStackTrace())), exception, messageToLog, args);
        } catch (Exception libraryException) {
            Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, StringUtils.getFormattedMessage(libraryException, "SmartLogger"));
            Log.w(FinalLogger.TAG_LOGGING_PREFIX, StringUtils.getFormattedMessage(exception, messageToLog, args));
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public synchronized void Error(String messageToLog, Object... args) {
        try {
            InternalError(new LinkedList<>(Arrays.asList(Thread.currentThread().getStackTrace())), messageToLog, args);
        } catch (Exception libraryException) {
            Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, StringUtils.getFormattedMessage(libraryException, "SmartLogger"));
            Log.e(FinalLogger.TAG_LOGGING_PREFIX, StringUtils.getFormattedMessage(null, messageToLog, args));
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param exception    the exception to be logged
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public synchronized void Error(Exception exception, String messageToLog, Object... args) {
        try {
            InternalError(new LinkedList<>(Arrays.asList(Thread.currentThread().getStackTrace())), exception, messageToLog, args);
        } catch (Exception libraryException) {
            Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, StringUtils.getFormattedMessage(libraryException, "SmartLogger"));
            Log.e(FinalLogger.TAG_LOGGING_PREFIX, StringUtils.getFormattedMessage(exception, messageToLog, args));
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param exception the exception to be logged
     */
    @Override
    public synchronized void Error(Exception exception) {
        try {
            InternalError(new LinkedList<>(Arrays.asList(Thread.currentThread().getStackTrace())), exception, "");
        } catch (Exception libraryException) {
            Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, StringUtils.getFormattedMessage(libraryException, "SmartLogger"));
            Log.e(FinalLogger.TAG_LOGGING_PREFIX, StringUtils.getFormattedMessage(exception, ""));
        }
    }

    @Override
    public void Wtf(String messageToLog, Object... args) {
        try {
            InternalWtf(new LinkedList<>(Arrays.asList(Thread.currentThread().getStackTrace())), messageToLog, args);
        } catch (Exception libraryException) {
            Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, StringUtils.getFormattedMessage(libraryException, "SmartLogger"));
            Log.wtf(FinalLogger.TAG_LOGGING_PREFIX, StringUtils.getFormattedMessage(null, messageToLog, args));
        }
    }

    @Override
    public void Wtf(Exception exception, String messageToLog, Object... args) {
        try {
            InternalWtf(new LinkedList<>(Arrays.asList(Thread.currentThread().getStackTrace())), exception, messageToLog, args);
        } catch (Exception libraryException) {
            Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, StringUtils.getFormattedMessage(libraryException, "SmartLogger"));
            Log.wtf(FinalLogger.TAG_LOGGING_PREFIX, StringUtils.getFormattedMessage(exception, messageToLog, args));
        }
    }

    @Override
    public void Wtf(Exception exception) {
        try {
            InternalWtf(new LinkedList<>(Arrays.asList(Thread.currentThread().getStackTrace())), exception, "");
        } catch (Exception libraryException) {
            Log.wtf(JeefoLogger.TAG_LIBRARY_LOG, StringUtils.getFormattedMessage(libraryException, "SmartLogger"));
            Log.wtf(FinalLogger.TAG_LOGGING_PREFIX, StringUtils.getFormattedMessage(exception, ""));
        }
    }
}
