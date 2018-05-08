package com.jeefo.android.log;

import android.util.Log;

/**
 * Created by Alex on 02/01/18.
 */

public class MainLogger implements ILog {

    //TODO: tell in a settings class where to log
    @Override
    public void Debug(String messageToLog, String... args) {
        String finalMessage = getFormattedMessage(null, messageToLog, args);
        Log.d(loggingPrefix+userPrefix, finalMessage);
        persistentLogger.Debug(finalMessage);
    }

    @Override
    public void Debug(Exception exception, String messageToLog, String... args) {
        String finalMessage = getFormattedMessage(exception, messageToLog, args);
        Log.d(loggingPrefix+userPrefix, finalMessage);
        persistentLogger.Debug(finalMessage);
    }

    @Override
    public void Info(String messageToLog, String... args) {
        String finalMessage = getFormattedMessage(null, messageToLog, args);
        Log.i(loggingPrefix+userPrefix, finalMessage);
        persistentLogger.Info(finalMessage);
    }

    @Override
    public void Warn(String messageToLog, String... args) {
        String finalMessage = getFormattedMessage(null, messageToLog, args);
        Log.w(loggingPrefix+userPrefix, finalMessage);
        persistentLogger.Warn(finalMessage);
    }

    @Override
    public void Warn(Exception exception, String messageToLog, String... args) {
        String finalMessage = getFormattedMessage(exception, messageToLog, args);
        Log.w(loggingPrefix+userPrefix, finalMessage);
        persistentLogger.Warn(finalMessage);
    }

    @Override
    public void Error(String messageToLog, String... args) {
        String finalMessage = getFormattedMessage(null, messageToLog, args);
        Log.e(loggingPrefix+userPrefix, finalMessage);
        persistentLogger.Error(finalMessage);
    }

    @Override
    public void Error(Exception exception, String messageToLog, String... args) {
        String finalMessage = getFormattedMessage(exception, messageToLog, args);
        Log.e(loggingPrefix+userPrefix, finalMessage);
        persistentLogger.Error(finalMessage);
    }

    @Override
    public void Error(Exception exception) {
        Log.e(loggingPrefix+userPrefix, exception.getMessage());
        persistentLogger.Error(exception);
    }

    private String getFormattedMessage(Exception ex, String message, String... args) {
        StringBuilder formattedMessage = new StringBuilder();
        try {
            formattedMessage.append(String.format(message,(Object[]) args));
        } catch (Exception e) {
            formattedMessage.append(message).append(" - args: ");
            for (String arg : args) {
                formattedMessage.append(arg).append(";");
            }
        }
        if (ex != null) {
            formattedMessage.append(" :: ").append(ex.getMessage());
        }
        return formattedMessage.toString();
    }

    public static void setLoggedUserTag(String uid) {
        userPrefix = String.format("[USER %s]", uid);
    }
    public static void removeLoggedUserTag() {
        userPrefix = "";
    }

    private static final ILog persistentLogger = new PersistentLogger();
    private static final String loggingPrefix = "[CustomLog]";
    private static String userPrefix = "";
}
