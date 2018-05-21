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

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
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

class PersistentLogger implements ITaggableLog {
    private static String userPrefix = "";
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
    public void Debug(String messageToLog, String... args) {
        logMessage("DEBUG", messageToLog, args);
    }

    @Override
    public void Debug(Exception exception, String messageToLog, String... args) {
        logMessage("DEBUG", exception, messageToLog, args);
    }

    @Override
    public void Info(String messageToLog, String... args) {
        logMessage("INFO", messageToLog, args);
    }

    @Override
    public void Warn(String messageToLog, String... args) {
        logMessage("WARN", messageToLog, args);
    }

    @Override
    public void Warn(Exception exception, String messageToLog, String... args) {
        logMessage("WARN", exception, messageToLog, args);
    }

    @Override
    public void Error(String messageToLog, String... args) {
        logMessage("ERROR", messageToLog, args);
    }

    @Override
    public void Error(Exception exception, String messageToLog, String... args) {
        logMessage("ERROR", exception, messageToLog, args);
    }

    @Override
    public void Error(Exception exception) {
        logMessage("ERROR", exception);
    }


    private void logMessage(String type, String messageToLog, String... args) {
        if (wasInitialised) {
            synchronized (lockObject) {
                try {
                    FileWriter logWriter = new FileWriter(logFile, true);
                    BufferedWriter outStream = new BufferedWriter(logWriter);
                    outStream.write(
                            new SimpleDateFormat("yyyy/MM/dd HH:mm:ss ", Locale.UK).format(new Date()) + type + "/" +
                                    userPrefix + String.format(messageToLog, (Object[]) args) + "\n");
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void logMessage(String type, Exception exception, String messageToLog, String... args) {
        if (wasInitialised) {
            synchronized (lockObject) {
                try {
                    FileWriter logWriter = new FileWriter(logFile, true);
                    BufferedWriter outStream = new BufferedWriter(logWriter);
                    outStream.write(
                            new SimpleDateFormat("yyyy/MM/dd HH:mm:ss ", Locale.UK).format(new Date()) + type + "/" +
                                    userPrefix + String.format(messageToLog, (Object[]) args) + " :: " + exception.getMessage() + "--" + exception.toString() + "\n");
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
                                    userPrefix + " EXCEPTION_ONLY :: " + exception.getMessage() + "--" + exception.toString() + "\n");
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void setLoggedUserTag(String uid) {
        userPrefix = String.format("[USER %s]", uid);
    }

    public static void removeLoggedUserTag() {
        userPrefix = "";
    }

    @Override
    public String addPersistentTag(String key, String value) {
        return null;
    }

    @Override
    public int removePersistentTagsFromKey(String key) {
        return 0;
    }

    @Override
    public int removePersistentTag(String uniqueTagIdentifier) {
        return 0;
    }

    @Override
    public int clearPersistentTags() {
        return 0;
    }
}
