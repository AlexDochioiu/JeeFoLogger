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
class LazyLoggerInternal extends AbstractScopedLogger {

    /**
     * Constructor
     */
    LazyLoggerInternal() {
        initLogger(null);
    }

    /**
     * @return the prefix containing the tags or a LazyLoggerNotInitialized tag if that's the case
     */
    @Override
    String getMessageLogPrefix() {
        if (LazyLogger.packageName == null) {
            return "[LazyLoggerNotInitialized]";
        }
        StringBuilder logPrefix = new StringBuilder();

        List<Pair<String, LinkedList<String>>> traces = LazyLoggerUtils.getAllTraceForPackage(LazyLogger.packageName);

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
     */
    @Override
    public void Verbose(String messageToLog, Object... args) {
        InternalVerbose((LinkedList) null, messageToLog, args);
    }

    /**
     * @param exception    the exception to be logged
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public void Verbose(Exception exception, String messageToLog, Object... args) {
        InternalVerbose((LinkedList) null, exception, messageToLog, args);
    }

    /**
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public void Debug(String messageToLog, Object... args) {
        InternalDebug((LinkedList) null, messageToLog, args);
    }

    /**
     * @param exception    the exception to be logged
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public void Debug(Exception exception, String messageToLog, Object... args) {
        InternalDebug((LinkedList) null, exception, messageToLog, args);
    }

    /**
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public void Info(String messageToLog, Object... args) {
        InternalInfo((LinkedList) null, messageToLog, args);
    }

    /**
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public void Warn(String messageToLog, Object... args) {
        InternalWarn((LinkedList) null, messageToLog, args);
    }

    /**
     * @param exception    the exception to be logged
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public void Warn(Exception exception, String messageToLog, Object... args) {
        InternalWarn((LinkedList) null, exception, messageToLog, args);
    }

    /**
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public void Error(String messageToLog, Object... args) {
        InternalError((LinkedList) null, messageToLog, args);
    }

    /**
     * @param exception    the exception to be logged
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public void Error(Exception exception, String messageToLog, Object... args) {
        InternalError((LinkedList) null, exception, messageToLog, args);
    }

    /**
     * @param exception the exception to be logged
     */
    @Override
    public void Error(Exception exception) {
        InternalError((LinkedList) null, exception, "");
    }

    /**
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public void Wtf(String messageToLog, Object... args) {
        InternalWtf((LinkedList) null, messageToLog, args);

    }

    /**
     * @param exception    the exception to be logged
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public void Wtf(Exception exception, String messageToLog, Object... args) {
        InternalWtf((LinkedList) null, exception, messageToLog, args);
    }

    /**
     * @param exception the exception to be logged
     */
    @Override
    public void Wtf(Exception exception) {
        InternalWtf((LinkedList) null, exception, "");
    }
}
