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

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jeefo.android.log.utils.UuidCustomUtils;

import java.util.Locale;


/**
 * Created by Alexandru Iustin Dochioiu on 13/12/17.
 *
 * Concurrent logger used for creating a pass-through trace which will improve drastically the readability of
 * the logged messages as well as decrease the debugging time when bugs/ crashes arise. For ease of
 * use, the messages contain placeholders ('%s') and String params which will replace them.
 *
 * It logs all the messages to Logcat as well as to a persistent file (if activated).
 */
@SuppressWarnings("WeakerAccess")
public class ScopedLogger implements ILog {

    private final static String TAG_KEY_INSTANCE = "Instance";
    private final static String TAG_KEY_CLASS = "Class";
    private final static String TAG_KEY_METHOD = "Method";

    private String loggingPrefix = "";
    private ILog logger;

    /**
     * Constructor used for initializing a completely new {@link ScopedLogger} which will (ideally)
     * be used in creating further {@link ScopedLogger}s with extended trace.
     *
     * NOTE: This constructor adds the instance tag. if this is undesired, use the following constructor:
     *  ScopedLogger(@NonNull Class classCaller, boolean addInstanceUidTag) and set the second param to false
     *
     * @param classCaller       the {@link Class} in which this log is created
     */
    public ScopedLogger(@NonNull Class classCaller) {
        this(classCaller, true);
    }

    /**
     * Constructor used for initializing a completely new {@link ScopedLogger} which will (ideally)
     * be used in creating further {@link ScopedLogger}s with extended trace
     *
     * @param classCaller       the {@link Class} in which this log is created
     * @param addInstanceUidTag used for telling whether an instance tag should be added to this logger
     */
    public ScopedLogger(@NonNull Class classCaller, boolean addInstanceUidTag) {
        this(null, classCaller, addInstanceUidTag);
    }

    /**
     * Constructor used for initializing a completely new {@link ScopedLogger} for a class OR for extending
     * the trace of an existent {@link ILog}
     *
     * @param logger            the {@link ILog} to which we extend the tags trace OR NULL to create a new {@link ScopedLogger}
     * @param classCaller       the {@link Class} in which this log is created
     * @param addInstanceUidTag used for telling whether an instance tag should be added to this logger
     */
    public ScopedLogger(@Nullable ILog logger, @NonNull Class classCaller, boolean addInstanceUidTag) {
        this(logger, classCaller, null);

        if (addInstanceUidTag) {
            try {
                addTag(TAG_KEY_INSTANCE, UuidCustomUtils.generateShortUUID(), true);
            } catch (IllegalArgumentException e) {
                // I don't really expect to ever get here
                this.logger.Warn("Failed to add the instance unique id");
            }
        }
    }

    /**
     * Constructor used for creating a {@link ScopedLogger} with both class and method tags
     * <p>
     * INTENDED USAGE: When creating a logger based on an {@link ILog} passed as a parameter (especially applicable to static methods)
     * <p>
     * ALTERNATIVE USAGE: When creating a completely new logger (not extending from an existent one) inside a method which is to be used only
     * in the scope of that method. -- note: I generally believe that this is not good practice, and that the previous logger should be passed as a param and used
     *
     * @param logger      the {@link ILog} to which we extend the tags trace OR NULL to create a new {@link ScopedLogger}
     * @param classCaller the {@link Class} in which this log is created
     * @param method      the {@link String} containing the method name (ignored if null or empty)
     */
    @SuppressWarnings("ConstantConditions")
    public ScopedLogger(@Nullable ILog logger, @NonNull Class classCaller, @Nullable String method) {
        if (classCaller == null) {
            throw new IllegalArgumentException("classCaller should not be null");
        }
        initLogger(logger);

        addTag(TAG_KEY_CLASS, classCaller.getSimpleName(), false);
        addTag(TAG_KEY_METHOD, method, false);
    }


    /**
     * Constructor used for adding a method tag to an existent {@link ILog}.
     * <p>
     * NOTE: The created {@link ILog} should exist only within the scope of a method (or passed on further calls made within that method)!
     * (IT SHOULD NEVER BE STORED AS A MEMBER)
     * <p>
     * USAGE: This was intended for the scenario in which a class stores an {@link ILog} as a member. At the beginning of a method call,
     * we use the member logger to create a new ILog methodLogger = new ScopedLogger(memberLogger, "onMethodAction()"); This new logger
     * will exist only in the scope of this method, and should not be stored elsewhere.
     *
     * @param logger the {@link ILog} used for the log trace
     * @param method the {@link String} containing the method name
     * @throws IllegalArgumentException when either of the parameters is null or method is empty string
     */
    @SuppressWarnings("ConstantConditions")
    public ScopedLogger(@NonNull ILog logger, @NonNull String method) {
        if (logger == null) {
            throw new IllegalArgumentException("logger should not be null");
        }
        if (method == null || method.equals("")) {
            throw new IllegalArgumentException("method should not be null or empty string");
        }

        this.logger = logger;
        addTag(TAG_KEY_METHOD, method, false);

    }

    /**
     * Used for adding tags to the {@link ILog}. The format of the tag: "[KEY VALUE]"
     *
     * @param key                     the {@link String} matching the KEY field
     * @param value                   the {@link String} matching the VALUE field
     * @param throwOnNullOrEmptyValue boolean indicating whether a null value param is acceptable
     */
    @SuppressWarnings("SameParameterValue")
    private synchronized void addTag(String key, String value, boolean throwOnNullOrEmptyValue) {
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
    private synchronized void initLogger(@Nullable ILog logger) {
        if (logger == null) {
            this.logger = new JeefoLogger();
        } else {
            this.logger = logger;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public synchronized void Debug(String messageToLog, String... args) {
        logger.Debug(loggingPrefix + " " + messageToLog, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param exception    the exception to be logged
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public synchronized void Debug(Exception exception, String messageToLog, String... args) {
        logger.Debug(exception, loggingPrefix + " " + messageToLog, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public synchronized void Info(String messageToLog, String... args) {
        logger.Info(loggingPrefix + " " + messageToLog, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public synchronized void Warn(String messageToLog, String... args) {
        logger.Warn(loggingPrefix + " " + messageToLog, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param exception    the exception to be logged
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public synchronized void Warn(Exception exception, String messageToLog, String... args) {
        logger.Warn(exception, loggingPrefix + " " + messageToLog, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public synchronized void Error(String messageToLog, String... args) {
        logger.Error(loggingPrefix + " " + messageToLog, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param exception    the exception to be logged
     * @param messageToLog the message to be logged
     * @param args         arguments for messageToLog
     */
    @Override
    public synchronized void Error(Exception exception, String messageToLog, String... args) {
        logger.Error(exception, loggingPrefix + " " + messageToLog, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param exception the exception to be logged
     */
    @Override
    public synchronized void Error(Exception exception) {
        logger.Error(exception, loggingPrefix);
    }
}
