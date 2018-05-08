package com.jeefo.android.log;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jeefo.android.log.utils.UuidCustomUtils;


/**
 * Created by Alex on 13/12/17.
 */

public class ScopedLogger implements ILog {
    public ScopedLogger(@Nullable ILog logger, @NonNull Class classCaller, @Nullable String method) {

        if (logger == null) {
            this.logger = new MainLogger();
        }
        else {
            this.logger = logger;
        }

        if (method == null || method.equals("")) {
            loggingPrefix = String.format("[Class %s] ", classCaller.getSimpleName());
        }
        else {
            loggingPrefix = String.format("[Class %s][Method %s()] ", classCaller.getSimpleName(), method);
        }
    }

    public ScopedLogger(@NonNull ILog logger, @NonNull String method) {
        this.logger = logger;
        loggingPrefix = String.format("[Method %s] ", method);
    }

    public ScopedLogger(@Nullable ILog logger, @NonNull Class classCaller, @Nullable String TagName, @NonNull String TagValue) {
        if (logger == null) {
            this.logger = new MainLogger();
        }
        else {
            this.logger = logger;
        }
        loggingPrefix = String.format("[Class %s][%s %s] ", classCaller.getSimpleName(), TagName, TagValue);
    }

    public ScopedLogger(@NonNull Class classCaller) {
        this(null, classCaller,"Instance", UuidCustomUtils.generateShortUUID());
    }

    public ScopedLogger(@NonNull Class classCaller, @NonNull String method) {
        this(null, classCaller, method);
    }

    //TODO: add locks for each log level

    @Override
    public void Debug(String messageToLog, String... args) {
        logger.Debug(loggingPrefix + messageToLog, args);
    }

    @Override
    public void Debug(Exception exception, String messageToLog, String... args) {
        logger.Debug(exception, loggingPrefix + messageToLog, args);
    }

    @Override
    public void Info(String messageToLog, String... args) {
        logger.Info(loggingPrefix + messageToLog, args);
    }

    @Override
    public void Warn(String messageToLog, String... args) {
        logger.Warn(loggingPrefix + messageToLog, args);
    }

    @Override
    public void Warn(Exception exception, String messageToLog, String... args) {
        logger.Warn(exception, loggingPrefix + messageToLog, args);
    }

    @Override
    public void Error(String messageToLog, String... args) {
        //TODO: send e-mail with logs
        logger.Error(loggingPrefix + messageToLog, args);
    }

    @Override
    public void Error(Exception exception, String messageToLog, String... args) {
        logger.Error(exception, loggingPrefix + messageToLog, args);
    }

    @Override
    public void Error(Exception exception) {
        logger.Error(exception, loggingPrefix);
    }

    private String loggingPrefix;
    private ILog logger;
}
