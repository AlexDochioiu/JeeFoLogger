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

package com.jeefo.android.log;

import java.io.Serializable;

/**
 * Created by Alexandru Iustin Dochioiu on 13/12/17.
 *
 * Interface used for logging messages. It is {@link Serializable} so we can pass it through
 * Bundles
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