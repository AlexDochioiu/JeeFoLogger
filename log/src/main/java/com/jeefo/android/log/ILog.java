package com.jeefo.android.log;

/**
 * Created by Alex on 13/12/17.
 */

import java.io.Serializable;

/**
 * Interface used for logging messages
 */
public interface ILog extends Serializable {

    /**
     * Write a custom message to the debug log
     * @param messageToLog the message to be logged
     * @param args arguments for messageToLog
     */
    void Debug(String messageToLog, String... args);

    /**
     * Write the exception message to the debug log
     * @param exception the exception to be logged
     * @param messageToLog the message to be logged
     * @param args arguments for messageToLog
     */
    void Debug(Exception exception, String messageToLog, String... args);

    /**
     * Write a custom message to the info log
     * @param messageToLog the message to be logged
     * @param args arguments for messageToLog
     */
    void Info(String messageToLog, String... args);

    /**
     * Write a custom message to the warning logged
     * @param messageToLog the message to be logged
     * @param args arguments for messageToLog
     */
    void Warn(String messageToLog, String... args);

    /**
     * Write the exception message to the warning log
     * @param exception the exception to be logged
     * @param messageToLog the message to be logged
     * @param args arguments for messageToLog
     */
    void Warn(Exception exception, String messageToLog, String... args);

    /**
     * Write a custom message to the error log
     * @param messageToLog the message to be logged
     * @param args arguments for messageToLog
     */
    void Error(String messageToLog, String... args);

    /**
     * Write the exception message and a custom message to the error log
     * @param exception the exception to be logged
     * @param messageToLog the message to be logged
     * @param args arguments for messageToLog
     */
    void Error(Exception exception, String messageToLog, String... args);

    /**
     * Write the exception message to the error log
     * @param exception the exception to be logged
     */
    void Error(Exception exception);
}
