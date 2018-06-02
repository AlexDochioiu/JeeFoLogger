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

/**
 * Created by Alexandru Iustin Dochioiu on 5/27/2018
 */
public class SmartLogger extends AbstractScopedLogger {

    public SmartLogger() {
        super();
        initLogger(null);

        final String classCaller = SmartLoggerUtils.getSimpleClassName(4);

        addTag(TAG_KEY_CLASS, classCaller, false);
        addTag(TAG_KEY_INSTANCE, UuidCustomUtils.generateShortUUID(), true);
    }

    public SmartLogger(@Nullable ILog logger) {
        super();
        initLogger(logger);

        final String classCaller = SmartLoggerUtils.getSimpleClassName(4);

        addTag(TAG_KEY_CLASS, classCaller, false);
        addTag(TAG_KEY_INSTANCE, UuidCustomUtils.generateShortUUID(), true);
    }

    @Override
    String getMessageLogPrefix() {
        final String methodName = SmartLoggerUtils.getMethodName(fullClassName, 1);

        if (methodName != null) {
            return String.format("%s[%s %s] ", getLoggingPrefix(), TAG_KEY_METHOD, methodName);
        } else {
            return getLoggingPrefix();
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
        DebugReflection(messageToLog, args);
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
        DebugReflection(exception, messageToLog, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public synchronized void Info(String messageToLog, Object... args) {
        InfoReflection(messageToLog, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public synchronized void Warn(String messageToLog, Object... args) {
        WarnReflection(messageToLog, args);
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
        WarnReflection(exception, messageToLog, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public synchronized void Error(String messageToLog, Object... args) {
        ErrorReflection(messageToLog, args);
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
        ErrorReflection(exception, messageToLog, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param exception the exception to be logged
     */
    @Override
    public synchronized void Error(Exception exception) {
        ErrorReflection(exception, "");
    }
}
