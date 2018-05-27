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

import java.util.Locale;

/**
 * Created by Alexandru Iustin Dochioiu on 5/27/2018
 */
abstract class AbstractScopedLogger implements ILog{
    final static String TAG_KEY_INSTANCE = "Instance";
    final static String TAG_KEY_CLASS = "Class";
    final static String TAG_KEY_METHOD = "Method";

    private String loggingPrefix = "";
    protected ILog logger;

    protected int depth = 5;

    public final void validateDepth(int extraDepth) {
        if (logger.getClass() == ScopedLogger.class) {
            ((ScopedLogger) logger).validateDepth(extraDepth + 2);
        } else {
            if (logger.getClass() == SmartLogger.class) {
                ((SmartLogger) logger).validateDepth(extraDepth + 1);
                ((SmartLogger) logger).increaseDepth(extraDepth);
            }
        }
    }

    final String getLoggingPrefix() {
        return loggingPrefix;
    }

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

    abstract void DebugReflection(String messageToLog, Object... args);

    abstract void DebugReflection(Exception exception, String messageToLog, Object... args);

    abstract void InfoReflection(String messageToLog, Object... args);

    abstract void WarnReflection(String messageToLog, Object... args);

    abstract void WarnReflection(Exception exception, String messageToLog, Object... args);

    abstract void ErrorReflection(String messageToLog, Object... args);

    abstract void ErrorReflection(Exception exception, String messageToLog, Object... args);

    abstract void ErrorReflection(Exception exception);
}
