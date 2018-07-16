package com.jeefo.android.jeefologger;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.LinkedList;

import static com.jeefo.android.jeefologger.StringUtils.getFormattedMessage;

/**
 * Created by Alexandru Iustin Dochioiu on 7/15/2018
 */
@SuppressWarnings("WeakerAccess")
public class FinalLogger implements IInternalLog {
    static final String TAG_LOGGING_PREFIX = "[JeeFo-Log]";
    @LogLevel
    static int logcatMinLevel = LogLevel.VERBOSE;

    private static final ILog persistentLogger = PersistentLogger.getInstance();

    private static FinalLogger instance;

    /**
     * Internal method used for getting the same instance always
     *
     * @return instance of {@link FinalLogger}
     */
    @NonNull
    static synchronized FinalLogger getInstance() {
        if (instance == null) {
            instance = new FinalLogger();
        }
        return instance;
    }

    private FinalLogger() {
    }

    @Override
    public void InternalVerbose(@Nullable LinkedList<StackTraceElement> traceElements, String messageToLog, Object... args) {
        String finalMessage = getFormattedMessage(null, messageToLog, args);
        if (logcatMinLevel <= LogLevel.VERBOSE) {
            Log.v(TAG_LOGGING_PREFIX + PersistentTagsManager.getTagsStringPrefix(), finalMessage);
        }
        persistentLogger.Verbose(finalMessage);
    }

    @Override
    public void InternalVerbose(@Nullable LinkedList<StackTraceElement> traceElements, Exception exception, String messageToLog, Object... args) {
        String finalMessage = getFormattedMessage(exception, messageToLog, args);
        if (logcatMinLevel <= LogLevel.VERBOSE) {
            Log.v(TAG_LOGGING_PREFIX + PersistentTagsManager.getTagsStringPrefix(), finalMessage);
        }
        persistentLogger.Verbose(finalMessage);
    }

    @Override
    public void InternalDebug(@Nullable LinkedList<StackTraceElement> traceElements, String messageToLog, Object... args) {
        String finalMessage = getFormattedMessage(null, messageToLog, args);
        if (logcatMinLevel <= LogLevel.DEBUG) {
            Log.d(TAG_LOGGING_PREFIX + PersistentTagsManager.getTagsStringPrefix(), finalMessage);
        }
        persistentLogger.Debug(finalMessage);
    }

    @Override
    public void InternalDebug(@Nullable LinkedList<StackTraceElement> traceElements, Exception exception, String messageToLog, Object... args) {
        String finalMessage = getFormattedMessage(exception, messageToLog, args);
        if (logcatMinLevel <= LogLevel.DEBUG) {
            Log.d(TAG_LOGGING_PREFIX + PersistentTagsManager.getTagsStringPrefix(), finalMessage);
        }
        persistentLogger.Debug(finalMessage);
    }

    @Override
    public void InternalInfo(@Nullable LinkedList<StackTraceElement> traceElements, String messageToLog, Object... args) {
        String finalMessage = getFormattedMessage(null, messageToLog, args);
        if (logcatMinLevel <= LogLevel.INFO) {
            Log.i(TAG_LOGGING_PREFIX + PersistentTagsManager.getTagsStringPrefix(), finalMessage);
        }
        persistentLogger.Info(finalMessage);
    }

    @Override
    public void InternalWarn(@Nullable LinkedList<StackTraceElement> traceElements, String messageToLog, Object... args) {
        String finalMessage = getFormattedMessage(null, messageToLog, args);
        if (logcatMinLevel <= LogLevel.WARN) {
            Log.w(TAG_LOGGING_PREFIX + PersistentTagsManager.getTagsStringPrefix(), finalMessage);
        }
        persistentLogger.Warn(finalMessage);
    }

    @Override
    public void InternalWarn(@Nullable LinkedList<StackTraceElement> traceElements, Exception exception, String messageToLog, Object... args) {
        String finalMessage = getFormattedMessage(exception, messageToLog, args);
        if (logcatMinLevel <= LogLevel.WARN) {
            Log.w(TAG_LOGGING_PREFIX + PersistentTagsManager.getTagsStringPrefix(), finalMessage);
        }
        persistentLogger.Warn(finalMessage);
    }

    @Override
    public void InternalError(@Nullable LinkedList<StackTraceElement> traceElements, String messageToLog, Object... args) {
        String finalMessage = getFormattedMessage(null, messageToLog, args);
        if (logcatMinLevel <= LogLevel.ERROR) {
            Log.e(TAG_LOGGING_PREFIX + PersistentTagsManager.getTagsStringPrefix(), finalMessage);
        }
        persistentLogger.Error(finalMessage);
    }

    @Override
    public void InternalError(@Nullable LinkedList<StackTraceElement> traceElements, Exception exception, String messageToLog, Object... args) {
        String finalMessage = getFormattedMessage(exception, messageToLog, args);
        if (logcatMinLevel <= LogLevel.ERROR) {
            Log.e(TAG_LOGGING_PREFIX + PersistentTagsManager.getTagsStringPrefix(), finalMessage);
        }
        persistentLogger.Error(finalMessage);
    }

    @Override
    public void InternalWtf(@Nullable LinkedList<StackTraceElement> traceElements, String messageToLog, Object... args) {
        String finalMessage = getFormattedMessage(null, messageToLog, args);
        if (logcatMinLevel <= LogLevel.WTF) {
            Log.wtf(TAG_LOGGING_PREFIX + PersistentTagsManager.getTagsStringPrefix(), finalMessage);
        }
        persistentLogger.Wtf(finalMessage);
    }

    @Override
    public void InternalWtf(@Nullable LinkedList<StackTraceElement> traceElements, Exception exception, String messageToLog, Object... args) {
        String finalMessage = getFormattedMessage(exception, messageToLog, args);
        if (logcatMinLevel <= LogLevel.WTF) {
            Log.wtf(TAG_LOGGING_PREFIX + PersistentTagsManager.getTagsStringPrefix(), finalMessage);
        }
        persistentLogger.Wtf(finalMessage);
    }
}
