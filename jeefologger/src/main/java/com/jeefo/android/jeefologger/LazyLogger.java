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

import android.util.Pair;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alexandru Iustin Dochioiu on 6/2/2018
 */
@SuppressWarnings({"unchecked", "RedundantCast"})
public class LazyLogger extends AbstractScopedLogger {

    /**
     * Constructor
     */
    public LazyLogger() {
        initLogger(null);
    }

    /**
     * @return the prefix containing the tags
     * @throws ExceptionInInitializerError if this method is called before JeefoLogger.initDebugLogger(Context)
     */
    @Override
    String getMessageLogPrefix() {
        if (JeefoLogger.packageName == null) {
            throw new ExceptionInInitializerError("JeefoLogger.initDebugLogger(Context) should have been called before logging a message with this logger");
        }
        StringBuilder logPrefix = new StringBuilder();

        List<Pair<String, LinkedList<String>>> traces = SmartLoggerUtils.getAllTraceForPackage(JeefoLogger.packageName);

        for (Pair<String, LinkedList<String>> trace : traces) {
            logPrefix.append("[").append(TAG_KEY_CLASS).append(" ").append(trace.first).append("]");
            for (final String methodName : trace.second) {
                logPrefix.append("[").append(TAG_KEY_METHOD).append(" ").append(methodName).append("]");
            }
        }

        return logPrefix.append(" ").toString();
    }

    /**
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     * @throws ExceptionInInitializerError if this method is called before JeefoLogger.initDebugLogger(Context)
     */
    @Override
    public void Debug(String messageToLog, Object... args) {
        DebugReflection((LinkedList) null, messageToLog, args);
    }

    /**
     * @param exception    the exception to be logged
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     * @throws ExceptionInInitializerError if this method is called before JeefoLogger.initDebugLogger(Context)
     */
    @Override
    public void Debug(Exception exception, String messageToLog, Object... args) {
        DebugReflection((LinkedList) null, exception, messageToLog, args);
    }

    /**
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     * @throws ExceptionInInitializerError if this method is called before JeefoLogger.initDebugLogger(Context)
     */
    @Override
    public void Info(String messageToLog, Object... args) {
        InfoReflection((LinkedList) null, messageToLog, args);
    }

    /**
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     * @throws ExceptionInInitializerError if this method is called before JeefoLogger.initDebugLogger(Context)
     */
    @Override
    public void Warn(String messageToLog, Object... args) {
        WarnReflection((LinkedList) null, messageToLog, args);
    }

    /**
     * @param exception    the exception to be logged
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     * @throws ExceptionInInitializerError if this method is called before JeefoLogger.initDebugLogger(Context)
     */
    @Override
    public void Warn(Exception exception, String messageToLog, Object... args) {
        WarnReflection((LinkedList) null, exception, messageToLog, args);
    }

    /**
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     * @throws ExceptionInInitializerError if this method is called before JeefoLogger.initDebugLogger(Context)
     */
    @Override
    public void Error(String messageToLog, Object... args) {
        ErrorReflection((LinkedList) null, messageToLog, args);
    }

    /**
     * @param exception    the exception to be logged
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     * @throws ExceptionInInitializerError if this method is called before JeefoLogger.initDebugLogger(Context)
     */
    @Override
    public void Error(Exception exception, String messageToLog, Object... args) {
        ErrorReflection((LinkedList) null, exception, messageToLog, args);
    }

    /**
     * @param exception the exception to be logged
     * @throws ExceptionInInitializerError if this method is called before JeefoLogger.initDebugLogger(Context)
     */
    @Override
    public void Error(Exception exception) {
        ErrorReflection((LinkedList) null, exception, "");
    }
}
