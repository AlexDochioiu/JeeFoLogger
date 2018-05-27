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

    private int depth = 5;

    public SmartLogger() {
        initLogger(null);

        validateDepth(1);
        final String classCaller = SmartLoggerUtils.getClassName();

        addTag(TAG_KEY_CLASS, classCaller, false);
        addTag(TAG_KEY_INSTANCE, UuidCustomUtils.generateShortUUID(), true);
    }

    public void increaseDepth(int extraDepth) {
        depth = 5 + extraDepth;
    }

    private String getLoggingPrefixWithMethod(@Nullable String messageToLog) {
        if (messageToLog != null) {
            return String.format("%s[%s %s] %s", getLoggingPrefix(), TAG_KEY_METHOD, SmartLoggerUtils.getMethodName(depth), messageToLog);
        } else {
            return String.format("%s[%s %s]", getLoggingPrefix(), TAG_KEY_METHOD, SmartLoggerUtils.getMethodName(depth));
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
        logger.Debug(getLoggingPrefixWithMethod(messageToLog), args);
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
        logger.Debug(exception, getLoggingPrefixWithMethod(messageToLog), args);
    }

    /**
     * {@inheritDoc}
     *
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public synchronized void Info(String messageToLog, Object... args) {
        logger.Info(getLoggingPrefixWithMethod(messageToLog), args);
    }

    /**
     * {@inheritDoc}
     *
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public synchronized void Warn(String messageToLog, Object... args) {
        logger.Warn(getLoggingPrefixWithMethod(messageToLog), args);
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
        logger.Warn(exception, getLoggingPrefixWithMethod(messageToLog), args);
    }

    /**
     * {@inheritDoc}
     *
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public synchronized void Error(String messageToLog, Object... args) {
        logger.Error(getLoggingPrefixWithMethod(messageToLog), args);
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
        logger.Error(exception, getLoggingPrefixWithMethod(messageToLog), args);
    }

    /**
     * {@inheritDoc}
     *
     * @param exception the exception to be logged
     */
    @Override
    public synchronized void Error(Exception exception) {
        logger.Error(exception, getLoggingPrefixWithMethod(null));
    }

    synchronized void DebugReflection(String messageToLog, Object... args) {
        logger.Debug(getLoggingPrefix() + messageToLog, args);
    }

    synchronized void DebugReflection(Exception exception, String messageToLog, Object... args) {
        logger.Debug(exception, getLoggingPrefix() + messageToLog, args);
    }

    synchronized void InfoReflection(String messageToLog, Object... args) {
        logger.Info(getLoggingPrefix() + messageToLog, args);
    }

    synchronized void WarnReflection(String messageToLog, Object... args) {
        logger.Warn(getLoggingPrefix() + messageToLog, args);
    }

    synchronized void WarnReflection(Exception exception, String messageToLog, Object... args) {
        logger.Warn(exception, getLoggingPrefix() + messageToLog, args);
    }

    synchronized void ErrorReflection(String messageToLog, Object... args) {
        logger.Error(getLoggingPrefix() + messageToLog, args);
    }

    synchronized void ErrorReflection(Exception exception, String messageToLog, Object... args) {
        logger.Error(exception, getLoggingPrefix() + messageToLog, args);
    }

    synchronized void ErrorReflection(Exception exception) {
        logger.Error(exception, getLoggingPrefix());
    }
}
