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

import java.io.File;
import java.lang.ref.WeakReference;

/**
 * Created by Alexandru Iustin Dochioiu on 02/01/18.
 */
@SuppressWarnings({"WeakerAccess", "UnusedReturnValue", "unused"})
public class JeefoLogger {
    static final String TAG_LIBRARY_LOG = "[LIB-JEEFO]";

    /**
     * Public method used for initializing persistence. If persistence is not desired, do not
     * call this method.
     *
     * <b>NOTE: This is now deprecated and will be removed entirely at the beginning of 2019. USE
     * {@link JeefoLogger.Builder} instead</b>
     *
     * @param context any kind of context.
     * @throws IllegalArgumentException if the context is null and the persistence has not
     *                                  already been initialized
     */
    @Deprecated
    public static void initPersistence(@NonNull Context context) {
        PersistentLogger.init(context, LogLevel.VERBOSE);
    }

    /**
     * Public method used for initializing the debug logger. This does not need to be called
     * if the {@link JeefoLogger#initPersistence(Context)} is called
     *
     * <b>NOTE: This is now deprecated and will be removed entirely at the beginning of 2019. USE
     * {@link JeefoLogger.Builder} instead</b>
     *
     * @param context any kind of context
     */
    @Deprecated
    public static void initLazyLogger(@NonNull Context context) {
        LazyLogger.packageName = context.getPackageName();
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
     * @return {@value PersistentTagsManager#TAG_REMOVED} if the {@link String} identifier is not known;
     * {@value PersistentTagsManager#TAG_ID_IS_NULL} if the parameter is null;
     * {@value PersistentTagsManager#TAG_REMOVED} if the tag was successfully removed
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

    /**
     * Each {@link File} object will have the name following the structure:
     * yyyy_MM_dd_Log.txt
     *
     * @return array of all the log files as long as the persistent logging was initialized;
     * null if the persistent logging was not initialized
     */
    @Nullable
    public static File[] getAllLogFiles() {
        return PersistentLogger.getAllLogFiles();
    }

    /**
     * Private constructor as this should never be initialized
     */
    private JeefoLogger() {
    }

    /**
     * Builder class for setting up the JeeFoLogger
     */
    public static class Builder {
        WeakReference<Context> appContextWeakReference;

        private boolean useLazyLogger = false;
        private boolean usePersistence = false;
        @LogLevel
        private int minPersistenceLevel = LogLevel.VERBOSE;
        @LogLevel
        private int minLogcatLevel = LogLevel.VERBOSE;

        @SuppressWarnings("ConstantConditions")
        public Builder(@NonNull Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Non-null context required!");
            }

            Context applicationContext;
            if (context.getApplicationContext() != null) {
                applicationContext = context.getApplicationContext();
            } else {
                applicationContext = context;
            }

            appContextWeakReference = new WeakReference<>(applicationContext);
        }

        /**
         * Whether the lazy logger should be initialized. Generally, this can be set to true even
         * when there's no current intention of using it as there's not really a memory/performance
         * hit to it
         *
         * @param initLazyLogger whether the lazy logger should be initialized
         * @return self to allow cascading
         */
        public Builder withLazyLogger(boolean initLazyLogger) {
            this.useLazyLogger = initLazyLogger;
            return this;
        }

        /**
         * <b>NOTE:</b> The log files are not stored securely. I recommend turning this off when
         * rolling out to production if there's any sensitive data getting logged.
         *
         * @param persistLogs whether persisting logs to file is desired
         * @return self to allow cascading
         */
        public Builder withPersistence(boolean persistLogs) {
            this.usePersistence = persistLogs;
            return this;
        }

        /**
         * <b>NOTE:</b> In order for the persistence to work, you should set the persistence to
         * be used using {@link Builder#withPersistence(boolean)}
         * <p>
         * <b>NOTE:</b> The default log level is {@value LogLevel#VERBOSE}
         *
         * @param minimumPersistenceLevel minimum level for the messages to be saved persistently
         * @return self to allow cascading
         */
        public Builder withMinimumPersistenceLevel(@LogLevel int minimumPersistenceLevel) {
            this.minPersistenceLevel = minimumPersistenceLevel;
            return this;
        }

        /**
         * As an extra security measure, this can be set to {@value LogLevel#NONE} when rolling
         * for production. That will ensure no message is passed to logcat ( so it cannot be
         * intercepted )
         * <p>
         * <b>NOTE:</b> The default log level is {@value LogLevel#VERBOSE}
         *
         * @param minimumLogcatLevel minimum level for the messages to be passed onto logcat
         * @return self to allow cascading
         */
        public Builder withMinimumLogcatLevel(@LogLevel int minimumLogcatLevel) {
            this.minLogcatLevel = minimumLogcatLevel;
            return this;
        }

        /**
         * Called to build the JeeFoLogger settings and initialize it using them
         */
        public void buildAndInit() {
            final Context context = appContextWeakReference.get();

            if (context != null) {
                FinalLogger.persistenceMinLevel = minPersistenceLevel;

                if (useLazyLogger) {
                    LazyLogger.packageName = context.getPackageName();
                } else {
                    LazyLogger.packageName = null;
                }

                PersistentLogger.setIsActive(usePersistence);
                if (usePersistence) {
                    PersistentLogger.init(context, minPersistenceLevel);
                }
            } else {
                Log.w(TAG_LIBRARY_LOG, "Cannot initialize JeeFoLogger as the context was lost");
            }
        }
    }
}
