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

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by Alexandru Iustin Dochioiu on 5/27/2018
 */
class SmartLogger extends AbstractScopedLogger {

    // required when we search for the method call, use full name so we avoid big
    // problems caused by obfuscation
    private final String fullClientClassName;

    SmartLogger() {
        initLogger(null);

        String clientClassCaller;

        fullClientClassName = SmartLoggerUtils.getFullClassName(5);
        clientClassCaller = SmartLoggerUtils.getSimpleClassName(5);

        addTag(TAG_KEY_CLASS, clientClassCaller, false);
        addTag(TAG_KEY_INSTANCE, UuidCustomUtils.generateShortUUID(), true);
    }

    SmartLogger(@Nullable ILog logger) {
        initLogger(logger);

        //todo: check if logger class is the same as the top one. if that's the case, we should not add extra tags here

        String clientClassCaller;

        fullClientClassName = SmartLoggerUtils.getFullClassName(5);
        clientClassCaller = SmartLoggerUtils.getSimpleClassName(5);

        addTag(TAG_KEY_CLASS, clientClassCaller, false);
        addTag(TAG_KEY_INSTANCE, UuidCustomUtils.generateShortUUID(), true);
    }

    String getFullClientClassName() {
        return fullClientClassName;
    }

    @Override
    String getMessageLogPrefix() {

        final String methodName = SmartLoggerUtils.getMethodName(fullClientClassName, traceElements);
        StringBuilder messageLogPrefix = new StringBuilder();
        messageLogPrefix.append(getLoggingPrefix());

        if (methodName != null) {
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
    public synchronized void Debug(String messageToLog, Object... args) {
        DebugReflection(
                new LinkedList<>(Arrays.asList(Thread.currentThread().getStackTrace())),
                messageToLog,
                args
        );
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
        DebugReflection(
                new LinkedList<>(Arrays.asList(Thread.currentThread().getStackTrace())),
                exception,
                messageToLog,
                args
        );
    }

    /**
     * {@inheritDoc}
     *
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public synchronized void Info(String messageToLog, Object... args) {
        InfoReflection(
                new LinkedList<>(Arrays.asList(Thread.currentThread().getStackTrace())),
                messageToLog,
                args
        );
    }

    /**
     * {@inheritDoc}
     *
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public synchronized void Warn(String messageToLog, Object... args) {
        WarnReflection(
                new LinkedList<>(Arrays.asList(Thread.currentThread().getStackTrace())),
                messageToLog,
                args
        );
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
        WarnReflection(
                new LinkedList<>(Arrays.asList(Thread.currentThread().getStackTrace())),
                exception,
                messageToLog,
                args
        );
    }

    /**
     * {@inheritDoc}
     *
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public synchronized void Error(String messageToLog, Object... args) {
        ErrorReflection(
                new LinkedList<>(Arrays.asList(Thread.currentThread().getStackTrace())),
                messageToLog,
                args
        );
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
        ErrorReflection(new LinkedList<>(Arrays.asList(Thread.currentThread().getStackTrace())),
                exception,
                messageToLog,
                args
        );
    }

    /**
     * {@inheritDoc}
     *
     * @param exception the exception to be logged
     */
    @Override
    public synchronized void Error(Exception exception) {
        ErrorReflection(
                new LinkedList<>(Arrays.asList(Thread.currentThread().getStackTrace())),
                exception,
                ""
        );
    }
}
