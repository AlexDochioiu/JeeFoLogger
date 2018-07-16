package com.jeefo.android.jeefologger;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.LinkedList;

/**
 * Created by Alexandru Iustin Dochioiu on 7/14/2018
 */
interface IInternalLog extends Serializable {

    /**
     * Write a custom message to the verbose log
     *
     * @param traceElements the stackTrace (can be null) used by the {@link SmartLogger} when
     *                      computing the logging prefix
     * @param messageToLog  the message to be logged
     * @param args          arguments for messageToLog
     */
    void InternalVerbose(@Nullable final LinkedList<StackTraceElement> traceElements, String messageToLog, Object... args);

    /**
     * Write the exception message to the verbose log
     *
     * @param traceElements the stackTrace (can be null) used by the {@link SmartLogger} when
     *                      computing the logging prefix
     * @param exception     the exception to be logged
     * @param messageToLog  the message to be logged
     * @param args          arguments for messageToLog
     */
    void InternalVerbose(@Nullable final LinkedList<StackTraceElement> traceElements, Exception exception, String messageToLog, Object... args);

    /**
     * Write a custom message to the debug log
     *
     * @param traceElements the stackTrace (can be null) used by the {@link SmartLogger} when
     *                      computing the logging prefix
     * @param messageToLog  the message to be logged
     * @param args          arguments for messageToLog
     */
    void InternalDebug(@Nullable final LinkedList<StackTraceElement> traceElements, String messageToLog, Object... args);

    /**
     * Write the exception message to the debug log
     *
     * @param traceElements the stackTrace (can be null) used by the {@link SmartLogger} when
     *                      computing the logging prefix
     * @param exception     the exception to be logged
     * @param messageToLog  the message to be logged
     * @param args          arguments for messageToLog
     */
    void InternalDebug(@Nullable final LinkedList<StackTraceElement> traceElements, Exception exception, String messageToLog, Object... args);

    /**
     * Write a custom message to the info log
     *
     * @param traceElements the stackTrace (can be null) used by the {@link SmartLogger} when
     *                      computing the logging prefix
     * @param messageToLog  the message to be logged
     * @param args          arguments for messageToLog
     */
    void InternalInfo(@Nullable final LinkedList<StackTraceElement> traceElements, String messageToLog, Object... args);

    /**
     * Write a custom message to the warning logged
     *
     * @param traceElements the stackTrace (can be null) used by the {@link SmartLogger} when
     *                      computing the logging prefix
     * @param messageToLog  the message to be logged
     * @param args          arguments for messageToLog
     */
    void InternalWarn(@Nullable final LinkedList<StackTraceElement> traceElements, String messageToLog, Object... args);

    /**
     * Write the exception message to the warning log
     *
     * @param traceElements the stackTrace (can be null) used by the {@link SmartLogger} when
     *                      computing the logging prefix
     * @param exception     the exception to be logged
     * @param messageToLog  the message to be logged
     * @param args          arguments for messageToLog
     */
    void InternalWarn(@Nullable final LinkedList<StackTraceElement> traceElements, Exception exception, String messageToLog, Object... args);

    /**
     * Write a custom message to the error log
     *
     * @param traceElements the stackTrace (can be null) used by the {@link SmartLogger} when
     *                      computing the logging prefix
     * @param messageToLog  the message to be logged
     * @param args          arguments for messageToLog
     */
    void InternalError(@Nullable final LinkedList<StackTraceElement> traceElements, String messageToLog, Object... args);

    /**
     * Write the exception message and a custom message to the error log
     *
     * @param traceElements the stackTrace (can be null) used by the {@link SmartLogger} when
     *                      computing the logging prefix
     * @param exception     the exception to be logged
     * @param messageToLog  the message to be logged
     * @param args          arguments for messageToLog
     */
    void InternalError(@Nullable final LinkedList<StackTraceElement> traceElements, Exception exception, String messageToLog, Object... args);

    /**
     * Write a custom message to the wtf log
     *
     * @param traceElements the stackTrace (can be null) used by the {@link SmartLogger} when
     *                      computing the logging prefix
     * @param messageToLog  the message to be logged
     * @param args          arguments for messageToLog
     */
    void InternalWtf(@Nullable final LinkedList<StackTraceElement> traceElements, String messageToLog, Object... args);

    /**
     * Write the exception message and a custom message to the wtf log
     *
     * @param traceElements the stackTrace (can be null) used by the {@link SmartLogger} when
     *                      computing the logging prefix
     * @param exception     the exception to be logged
     * @param messageToLog  the message to be logged
     * @param args          arguments for messageToLog
     */
    void InternalWtf(@Nullable final LinkedList<StackTraceElement> traceElements, Exception exception, String messageToLog, Object... args);
}
