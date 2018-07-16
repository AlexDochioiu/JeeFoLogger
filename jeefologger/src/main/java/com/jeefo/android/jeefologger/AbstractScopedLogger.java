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

import java.util.LinkedList;
import java.util.Locale;

/**
 * Created by Alexandru Iustin Dochioiu on 5/27/2018
 */
abstract class AbstractScopedLogger implements ILog, IInternalLog {
    final static String TAG_KEY_INSTANCE = "Instance";
    final static String TAG_KEY_CLASS = "Class";
    final static String TAG_KEY_METHOD = "Method";

    // Used by classes extending this one
    LinkedList<StackTraceElement> traceElements = null;

    private String loggingPrefix = "";
    protected IInternalLog logger;

    final String getLoggingPrefix() {
        return loggingPrefix;
    }

    /**
     * @return the {@link String} prefix for the logging message including all the tags
     */
    abstract String getMessageLogPrefix();

    /**
     * Used for adding tags to the {@link ILog}. The format of the tag: "[KEY VALUE]"
     *
     * @param key                     the {@link String} matching the KEY field
     * @param value                   the {@link String} matching the VALUE field
     * @param throwOnNullOrEmptyValue boolean indicating whether a null value param is acceptable
     * @throws IllegalArgumentException for null <i>value</i> IF <i>throwOnNullOrEmptyValue</i> is true
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
     * Used for initiating the {@link IInternalLog} member using the passed param if not null; otherwise
     * a new {@link JeefoLogger} is created
     *
     * @param logger the {@link IInternalLog} to be stored or null to create a new instance of {@link JeefoLogger}
     */
    final synchronized void initLogger(@Nullable IInternalLog logger) {
        if (logger == null) {
            this.logger = FinalLogger.getInstance();
        } else {
            this.logger = logger;
        }
    }

    @Override
    public final synchronized void InternalVerbose(@Nullable final LinkedList<StackTraceElement> traceElements, String messageToLog, Object... args) {
        this.traceElements = traceElements;
        logger.InternalVerbose(traceElements, getMessageLogPrefix() + messageToLog, args);
    }

    @Override
    public final synchronized void InternalVerbose(@Nullable final LinkedList<StackTraceElement> traceElements, Exception exception, String messageToLog, Object... args) {
        this.traceElements = traceElements;
        logger.InternalVerbose(traceElements, exception, getMessageLogPrefix() + messageToLog, args);
    }

    @Override
    public final synchronized void InternalDebug(@Nullable final LinkedList<StackTraceElement> traceElements, String messageToLog, Object... args) {
        this.traceElements = traceElements;
        logger.InternalDebug(traceElements, getMessageLogPrefix() + messageToLog, args);
    }

    @Override
    public final synchronized void InternalDebug(@Nullable final LinkedList<StackTraceElement> traceElements, Exception exception, String messageToLog, Object... args) {
        this.traceElements = traceElements;
        logger.InternalDebug(traceElements, exception, getMessageLogPrefix() + messageToLog, args);
    }

    @Override
    public final synchronized void InternalInfo(@Nullable final LinkedList<StackTraceElement> traceElements, String messageToLog, Object... args) {
        this.traceElements = traceElements;
        logger.InternalInfo(traceElements, getMessageLogPrefix() + messageToLog, args);
    }

    @Override
    public final synchronized void InternalWarn(@Nullable final LinkedList<StackTraceElement> traceElements, String messageToLog, Object... args) {
        this.traceElements = traceElements;
        logger.InternalWarn(traceElements, getMessageLogPrefix() + messageToLog, args);
    }

    @Override
    public final synchronized void InternalWarn(@Nullable final LinkedList<StackTraceElement> traceElements, Exception exception, String messageToLog, Object... args) {
        this.traceElements = traceElements;
        logger.InternalWarn(traceElements, exception, getMessageLogPrefix() + messageToLog, args);
    }

    @Override
    public final synchronized void InternalError(@Nullable final LinkedList<StackTraceElement> traceElements, String messageToLog, Object... args) {
        this.traceElements = traceElements;
        logger.InternalError(traceElements, getMessageLogPrefix() + messageToLog, args);
    }

    @Override
    public final synchronized void InternalError(@Nullable final LinkedList<StackTraceElement> traceElements, Exception exception, String messageToLog, Object... args) {
        this.traceElements = traceElements;
        logger.InternalError(traceElements, exception, getMessageLogPrefix() + messageToLog, args);
    }

    @Override
    public final synchronized void InternalWtf(@Nullable final LinkedList<StackTraceElement> traceElements, String messageToLog, Object... args) {
        this.traceElements = traceElements;
        logger.InternalWtf(traceElements, getMessageLogPrefix() + messageToLog, args);
    }

    @Override
    public final synchronized void InternalWtf(@Nullable final LinkedList<StackTraceElement> traceElements, Exception exception, String messageToLog, Object... args) {
        this.traceElements = traceElements;
        logger.InternalWtf(traceElements, exception, getMessageLogPrefix() + messageToLog, args);
    }
}
