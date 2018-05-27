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
    private static boolean wasInitialised = false;
    private static File logFile;

    private static final Object lockObject = new Object();

    /**
     * @param context the Application Context.
     * @throws IllegalArgumentException if the context is null
     */
    public static void init(@NonNull final Context context) {
        // use the same lock as the one for the messages writing to avoid some kind of race
        // condition (a very unlikely one though but still good to have this)
        synchronized (lockObject) {
            if (!wasInitialised) {
                //noinspection ConstantConditions
                if (context == null) {
                    throw new IllegalArgumentException("Non-null context required.");
                }

                Context applicationContext;
                if (context.getApplicationContext() != null) {
                    applicationContext = context.getApplicationContext();
                } else {
                    applicationContext = context;
                }

                //TODO: Move log files into a private dir
                File dirPath;
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    dirPath = applicationContext.getExternalFilesDir(Environment.DIRECTORY_DCIM);
                } else {
                    dirPath = applicationContext.getFilesDir();
                }

                final String timeStamp = new SimpleDateFormat("yyyy_MM_dd", Locale.UK).format(new Date());
                logFile = new File(dirPath, timeStamp + "_Log.txt");
                BufferedWriter outStream = null;

                try {
                    FileWriter logWriter = new FileWriter(logFile, true);
                    outStream = new BufferedWriter(logWriter);

                    outStream.write(
                            new SimpleDateFormat("yyyy/MM/dd HH:mm:ss ", Locale.UK).format(new Date()) +
                                    "Session Started" + "\n");
                    wasInitialised = true;
                } catch (Exception e) {
                    Log.e(JeefoLogger.loggingPrefix, String.format(Locale.UK,
                            "Failed to open or create the file for the persistent logging: %s", e.getMessage()));
                } finally {
                    if (outStream != null) {
                        try {
                            outStream.close();
                        } catch (Exception e) {
                            Log.e(JeefoLogger.loggingPrefix, String.format(Locale.UK,
                                    "Failed to close the file stream for the persistent logger: %s", e.getMessage()));
                        }
                    }
                }
            }
        }
    }

    /**
     * Internal Constructor to avoid instantiation outside the library as it should never
     * be required
     */
    PersistentLogger() {};

    @Override
    public void Debug(String messageToLog, Object... args) {
        logMessage("DEBUG", null, messageToLog, args);
    }

    @Override
    public void Debug(Exception exception, String messageToLog, Object... args) {
        logMessage("DEBUG", exception, messageToLog, args);
    }

    @Override
    public void Info(String messageToLog, Object... args) {
        logMessage("INFO", null, messageToLog, args);
    }

    @Override
    public void Warn(String messageToLog, Object... args) {
        logMessage("WARN", null, messageToLog, args);
    }

    @Override
    public void Warn(Exception exception, String messageToLog, Object... args) {
        logMessage("WARN", exception, messageToLog, args);
    }

    @Override
    public void Error(String messageToLog, Object... args) {
        logMessage("ERROR", null, messageToLog, args);
    }

    @Override
    public void Error(Exception exception, String messageToLog, Object... args) {
        logMessage("ERROR", exception, messageToLog, args);
    }

    @Override
    public void Error(Exception exception) {
        logMessage("ERROR", exception);
    }

    private void logMessage(@NonNull String type, @Nullable Exception exception, @NonNull String messageToLog, @Nullable Object... args) {
        if (wasInitialised) {
            synchronized (lockObject) {
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
    private void logMessage(String type, Exception exception) {
        if (wasInitialised) {
            synchronized (lockObject) {
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
}
