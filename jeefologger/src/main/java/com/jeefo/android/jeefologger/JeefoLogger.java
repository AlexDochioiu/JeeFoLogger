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
import android.support.annotation.Nullable;
import android.util.Log;

import static com.jeefo.android.jeefologger.StringUtils.getFormattedMessage;

/**
 * Created by Alexandru Iustin Dochioiu on 02/01/18.
 */
@SuppressWarnings("WeakerAccess")
public class JeefoLogger implements ILog {

    public static final int TAG_ID_NOT_FOUND = PersistentTagsManager.TAG_ID_NOT_FOUND;
    public static final int TAG_ID_IS_NULL = PersistentTagsManager.TAG_ID_IS_NULL;
    public static final int TAG_REMOVED = PersistentTagsManager.TAG_REMOVED;

    private static final ILog persistentLogger = new PersistentLogger();
    public static final String loggingPrefix = "[JeeFo-Log]";

    /**
     * Public method used for initializing persistence. If persistence is not desired, do not
     * call this method.
     *
     * @param context the Application Context.
     * @throws IllegalArgumentException if the context is null and the persistence has not
     *                                  already been initialized
     */
    public static void initPersistence(@NonNull Context context) {
        PersistentLogger.init(context);
    }

    @Override
    public void Debug(String messageToLog, Object... args) {
        String finalMessage = getFormattedMessage(null, messageToLog, args);
        Log.d(loggingPrefix + PersistentTagsManager.getTagsStringPrefix(), finalMessage);
        persistentLogger.Debug(finalMessage);
    }

    @Override
    public void Debug(Exception exception, String messageToLog, Object... args) {
        String finalMessage = getFormattedMessage(exception, messageToLog, args);
        Log.d(loggingPrefix + PersistentTagsManager.getTagsStringPrefix(), finalMessage);
        persistentLogger.Debug(finalMessage);
    }

    @Override
    public void Info(String messageToLog, Object... args) {
        String finalMessage = getFormattedMessage(null, messageToLog, args);
        Log.i(loggingPrefix + PersistentTagsManager.getTagsStringPrefix(), finalMessage);
        persistentLogger.Info(finalMessage);
    }

    @Override
    public void Warn(String messageToLog, Object... args) {
        String finalMessage = getFormattedMessage(null, messageToLog, args);
        Log.w(loggingPrefix + PersistentTagsManager.getTagsStringPrefix(), finalMessage);
        persistentLogger.Warn(finalMessage);
    }

    @Override
    public void Warn(Exception exception, String messageToLog, Object... args) {
        String finalMessage = getFormattedMessage(exception, messageToLog, args);
        Log.w(loggingPrefix + PersistentTagsManager.getTagsStringPrefix(), finalMessage);
        persistentLogger.Warn(finalMessage);
    }

    @Override
    public void Error(String messageToLog, Object... args) {
        String finalMessage = getFormattedMessage(null, messageToLog, args);
        Log.e(loggingPrefix + PersistentTagsManager.getTagsStringPrefix(), finalMessage);
        persistentLogger.Error(finalMessage);
    }

    @Override
    public void Error(Exception exception, String messageToLog, Object... args) {
        String finalMessage = getFormattedMessage(exception, messageToLog, args);
        Log.e(loggingPrefix + PersistentTagsManager.getTagsStringPrefix(), finalMessage);
        persistentLogger.Error(finalMessage);
    }

    @Override
    public void Error(Exception exception) {
        Log.e(loggingPrefix + PersistentTagsManager.getTagsStringPrefix(), getFormattedMessage(exception, null));
        persistentLogger.Error(exception);
    }

    /**
     * Used for adding a global persistent tag which will stick to all log messages on all threads.
     * This will look something like "[KEY VALUE]" in the log messages and will appear before the
     * Scope Tags
     * NOTE: this will apply to the ScopedLoggers already instantiated as well
     *
     * @param key   The string param which will represent the KEY in the tag. There is NO REQUIREMENT
     *              for it to be unique.
     * @param value The string param which will represent the VALUE in the tag.
     * @return a {@link String} unique identifier which can be used to remove the newly added tag
     * OR null if any parameter is null
     */
    @Nullable
    public static String addPersistentTag(String key, String value) {
        return PersistentTagsManager.addPersistentTag(key, value);
    }

    /**
     * Used for removing ALL the persistent tags having a particular {@link String} as the key
     *
     * @param key the (case-sensitive) key we are looking for in order to remove the tags
     * @return the number of tags that were removed based on the given key; 0 if the key is null
     */
    public static int removeAllPersistentTagsFromKey(String key) {
        return PersistentTagsManager.removeAllPersistentTagsFromKey(key);
    }

    /**
     * Used for removing a particular persistent tag
     *
     * @param uniqueTagIdentifier the {@link String} unique identifier for the tag to be remove
     * @return {@value TAG_ID_NOT_FOUND} if the {@link String} identifier is not known;
     * {@value TAG_ID_IS_NULL} if the parameter is null;
     * {@value TAG_REMOVED} if the tag was successfully removed
     */
    public static int removePersistentTag(String uniqueTagIdentifier) {
        return PersistentTagsManager.removePersistentTag(uniqueTagIdentifier);
    }

    /**
     * Used for removing all the persistent tags
     *
     * @return the number of tags removed
     */
    public static int clearPersistentTags() {
        return PersistentTagsManager.clearPersistentTags();
    }
}
