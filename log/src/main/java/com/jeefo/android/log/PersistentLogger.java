package com.jeefo.android.log;

import android.app.Application;
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
 * Created by Alex on 29/01/18.
 */

public class PersistentLogger implements ILog {

    public static void setLoggedUserTag(String uid) {
        userPrefix = String.format("[USER %s]", uid);
    }

    public static void removeLoggedUserTag() {
        userPrefix = "";
    }

    private static String userPrefix = "";
    private static boolean wasInitialised = false;
    private static File logFile;

    private static final Object lockObject = new Object();

    /**
     * @param context the ApplicationContext!
     */
    public static void init(@NonNull Context context) {
        if (context instanceof Application) {

            File dirPath;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                dirPath = context.getExternalFilesDir(Environment.DIRECTORY_DCIM);
            } else {
                dirPath = context.getFilesDir();
            }

            String timeStamp = new SimpleDateFormat("yyyy_MM_dd", Locale.UK).format(new Date());
            logFile = new File(dirPath, timeStamp + "_Log.txt");

            try {
                FileWriter logWriter = new FileWriter(logFile, true);
                BufferedWriter outStream = new BufferedWriter(logWriter);

                outStream.write(
                        new SimpleDateFormat("yyyy/MM/dd HH:mm:ss ", Locale.UK).format(new Date()) +
                                "Session Started" + "\n");
                outStream.close();
                wasInitialised = true;
            } catch (Exception e) {
                Log.e("PersistentLogger", "Failed to open or create the file");
            }
        } else {
            Log.e("PersistentLogger", "expected application context");
            throw new IllegalArgumentException("expected application context");
        }

    }

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
}
