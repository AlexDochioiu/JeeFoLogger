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

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Alexandru Iustin Dochioiu on 29/01/18.
 */

class PersistentLogger implements ILog {

    @LogLevel
    private static int persistenceMinLevel = LogLevel.NONE;
    private static boolean wasInitialised = false;
    private static File logsPath;
    private static File logFile;

    private static final Object lockObject = new Object();

    private static PersistentLogger instance;

    /**
     * Internal method used for getting the same instance always
     *
     * @return instance of {@link PersistentLogger}
     */
    @NonNull
    static synchronized ILog getInstance() {
        if (instance == null) {
            instance = new PersistentLogger();
        }

        return instance;
    }

    /**
     * @param context any kind of context.
     * @throws IllegalArgumentException if the context is null
     */
    public static void init(@NonNull final Context context, @LogLevel final int persistenceLevel) {
        // use the same lock as the one for the messages writing to avoid some kind of race
        // condition (a very unlikely one though but still good to have this)
        synchronized (lockObject) {
            persistenceMinLevel = persistenceLevel;

            if (!wasInitialised) {
                //noinspection ConstantConditions
                if (context == null) {
                    throw new IllegalArgumentException("Non-null context required.");
                }

                //TODO: Move log files into a private dir
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    logsPath = context.getExternalFilesDir(Environment.DIRECTORY_DCIM);
                } else {
                    logsPath = context.getFilesDir();
                }

                try {
                    File newLogsPath = new File(logsPath, "jeefoLogFiles");
                    if (newLogsPath.exists() || newLogsPath.mkdir()) {
                        logsPath = newLogsPath;
                    }
                } catch (Exception e) {
                    Log.w(JeefoLogger.TAG_LIBRARY_LOG, "Failed to create logs directory, use default DCIM instead ; " + e.getMessage());
                }

                final String timeStamp = new SimpleDateFormat("yyyy_MM_dd", Locale.UK).format(new Date());
                logFile = new File(logsPath, timeStamp + "_Log.txt");
                BufferedWriter outStream = null;

                try {
                    FileWriter logWriter = new FileWriter(logFile, true);
                    outStream = new BufferedWriter(logWriter);

                    outStream.write(
                            new SimpleDateFormat("yyyy/MM/dd HH:mm:ss ", Locale.UK).format(new Date()) +
                                    "Session Started" + "\n");
                    wasInitialised = true;
                } catch (Exception e) {
                    Log.e(JeefoLogger.TAG_LIBRARY_LOG, String.format(Locale.UK,
                            "Failed to open or create the file for the persistent logging: %s", e.getMessage()));
                } finally {
                    if (outStream != null) {
                        try {
                            outStream.close();
                        } catch (Exception e) {
                            Log.e(JeefoLogger.TAG_LIBRARY_LOG, String.format(Locale.UK,
                                    "Failed to close the file stream for the persistent logger: %s", e.getMessage()));
                        }
                    }
                }
            }
        }
    }

    /**
     * @return array of all the log files as long as the persistent logging was initialized;
     * null if the persistent logging was not initialized
     */
    @Nullable
    static File[] getAllLogFiles() {
        synchronized (lockObject) {
            if (wasInitialised) {
                return logsPath.listFiles();
            } else {
                return null;
            }
        }
    }

    @Override
    public void Verbose(String messageToLog, Object... args) {
        logMessageIfInitialized("VERBOSE", LogLevel.VERBOSE, null, messageToLog, args);
    }

    @Override
    public void Verbose(Exception exception, String messageToLog, Object... args) {
        logMessageIfInitialized("VERBOSE", LogLevel.VERBOSE, exception, messageToLog, args);
    }

    @Override
    public void Debug(String messageToLog, Object... args) {
        logMessageIfInitialized("DEBUG", LogLevel.DEBUG, null, messageToLog, args);
    }

    @Override
    public void Debug(Exception exception, String messageToLog, Object... args) {
        logMessageIfInitialized("DEBUG", LogLevel.DEBUG, exception, messageToLog, args);
    }

    @Override
    public void Info(String messageToLog, Object... args) {
        logMessageIfInitialized("INFO", LogLevel.INFO, null, messageToLog, args);
    }

    @Override
    public void Warn(String messageToLog, Object... args) {
        logMessageIfInitialized("WARN", LogLevel.WARN, null, messageToLog, args);
    }

    @Override
    public void Warn(Exception exception, String messageToLog, Object... args) {
        logMessageIfInitialized("WARN", LogLevel.WARN, exception, messageToLog, args);
    }

    @Override
    public void Error(String messageToLog, Object... args) {
        logMessageIfInitialized("ERROR", LogLevel.ERROR, null, messageToLog, args);
    }

    @Override
    public void Error(Exception exception, String messageToLog, Object... args) {
        logMessageIfInitialized("ERROR", LogLevel.ERROR, exception, messageToLog, args);
    }

    @Override
    public void Error(Exception exception) {
        logMessageIfInitialized("ERROR", LogLevel.ERROR, exception);
    }

    @Override
    public void Wtf(String messageToLog, Object... args) {
        logMessageIfInitialized("WTF", LogLevel.WTF, null, messageToLog, args);
    }

    @Override
    public void Wtf(Exception exception, String messageToLog, Object... args) {
        logMessageIfInitialized("WTF", LogLevel.WTF, exception, messageToLog, args);
    }

    @Override
    public void Wtf(Exception exception) {
        logMessageIfInitialized("WTF", LogLevel.WTF, exception);
    }

    private void logMessageIfInitialized(@NonNull String type, @LogLevel int logLevel, @Nullable Exception exception, @NonNull String messageToLog, @Nullable Object... args) {
        synchronized (lockObject) {
            if (wasInitialised && logLevel >= persistenceMinLevel) {
                try {
                    FileWriter logWriter = new FileWriter(logFile, true);
                    BufferedWriter outStream = new BufferedWriter(logWriter);
                    outStream.write(
                            new SimpleDateFormat("yyyy/MM/dd HH:mm:ss ", Locale.UK).format(new Date()) + type + "/" +
                                    PersistentTagsManager.getTagsStringPrefix() + StringUtils.getFormattedMessage(exception, messageToLog, args) + "\n");
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void logMessageIfInitialized(String type, @LogLevel int logLevel, Exception exception) {
        synchronized (lockObject) {
            if (wasInitialised && logLevel >= persistenceMinLevel) {
                try {
                    FileWriter logWriter = new FileWriter(logFile, true);
                    BufferedWriter outStream = new BufferedWriter(logWriter);
                    outStream.write(
                            new SimpleDateFormat("yyyy/MM/dd HH:mm:ss ", Locale.UK).format(new Date()) + type + "/" +
                                    PersistentTagsManager.getTagsStringPrefix() + " EXCEPTION_ONLY :: " + exception.getMessage() + "--" + exception.toString() + "\n");
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Private to avoid instantiation as this is a singleton
     */
    private PersistentLogger() {
    }

    public static synchronized void setIsActive(boolean usePersistence) {
        if (!usePersistence) {
            synchronized (lockObject) {
                persistenceMinLevel = LogLevel.NONE;
            }
        }
    }
}
