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
import android.support.annotation.NonNull;
import android.util.Log;

import static com.jeefo.android.jeefologger.StringUtils.getFormattedMessage;

/**
 * Created by Alexandru Iustin Dochioiu on 02/01/18.
 */
@SuppressWarnings("WeakerAccess")
public class JeefoLogger implements ITaggableLog {

    public static final int TAG_ID_NOT_FOUND = 10;
    public static final int TAG_NOT_REMOVED = 11;
    public static final int TAG_REMOVED = 12;

    private static final ILog persistentLogger = new PersistentLogger();
    public static final String loggingPrefix = "[JeeFo-Log]";
    private static String userPrefix = "";

    /**
     * Public method used for initializing persistence. If persistence is not desired, do not
     * call this method.
     *
     * @param context the Application Context.
     * @throws IllegalArgumentException if the context is null and the persistence has not
     *                                   already been initialized
     */
    public static void initPersistence(@NonNull Context context) {
        PersistentLogger.init(context);
    }

    @Override
    public void Debug(String messageToLog, Object... args) {
        String finalMessage = getFormattedMessage(null, messageToLog, args);
        Log.d(loggingPrefix+userPrefix, finalMessage);
        persistentLogger.Debug(finalMessage);
    }

    @Override
    public void Debug(Exception exception, String messageToLog, Object... args) {
        String finalMessage = getFormattedMessage(exception, messageToLog, args);
        Log.d(loggingPrefix+userPrefix, finalMessage);
        persistentLogger.Debug(finalMessage);
    }

    @Override
    public void Info(String messageToLog, Object... args) {
        String finalMessage = getFormattedMessage(null, messageToLog, args);
        Log.i(loggingPrefix+userPrefix, finalMessage);
        persistentLogger.Info(finalMessage);
    }

    @Override
    public void Warn(String messageToLog, Object... args) {
        String finalMessage = getFormattedMessage(null, messageToLog, args);
        Log.w(loggingPrefix+userPrefix, finalMessage);
        persistentLogger.Warn(finalMessage);
    }

    @Override
    public void Warn(Exception exception, String messageToLog, Object... args) {
        String finalMessage = getFormattedMessage(exception, messageToLog, args);
        Log.w(loggingPrefix+userPrefix, finalMessage);
        persistentLogger.Warn(finalMessage);
    }

    @Override
    public void Error(String messageToLog, Object... args) {
        String finalMessage = getFormattedMessage(null, messageToLog, args);
        Log.e(loggingPrefix+userPrefix, finalMessage);
        persistentLogger.Error(finalMessage);
    }

    @Override
    public void Error(Exception exception, String messageToLog, Object... args) {
        String finalMessage = getFormattedMessage(exception, messageToLog, args);
        Log.e(loggingPrefix+userPrefix, finalMessage);
        persistentLogger.Error(finalMessage);
    }

    @Override
    public void Error(Exception exception) {
        Log.e(loggingPrefix+userPrefix, getFormattedMessage(exception, null));
        persistentLogger.Error(exception);
    }



    public static void setLoggedUserTag(String uid) {
        userPrefix = String.format("[USER %s]", uid);
    }
    public static void removeLoggedUserTag() {
        userPrefix = "";
    }

    /**
     * {@inheritDoc}
     * @param key The string param which will represent the KEY in the tag. There is NO REQUIREMENT
     *            for it to be unique.
     * @param value The string param which will represent the VALUE in the tag.
     * @return {@inheritDoc}
     */
    @Override
    public String addPersistentTag(String key, String value) {
        return null;
    }

    /**
     * {@inheritDoc}
     * @param key the (case-sensitive) key we are looking for in order to remove the tags
     * @return {@inheritDoc}
     */
    @Override
    public int removePersistentTagsFromKey(String key) {
        return 0;
    }

    /**
     * {@inheritDoc}
     * @param uniqueTagIdentifier the {@link String} unique identifier for the tag to be remove
     * @return {@value TAG_ID_NOT_FOUND} if the {@link String} identifier is not known;
     *          {@value TAG_NOT_REMOVED} if for any reason the operation could not be performed;
     *          {@value TAG_REMOVED} if the tag was successfully removed
     */
    @Override
    public int removePersistentTag(String uniqueTagIdentifier) {
        return 0;
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public int clearPersistentTags() {
        return 0;
    }
}
