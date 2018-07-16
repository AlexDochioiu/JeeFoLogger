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

import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexandru Iustin Dochioiu on 5/27/2018
 */
@SuppressWarnings("WeakerAccess")
final class PersistentTagsManager {

    public static final int TAG_ID_IS_NULL = -1;
    public static final int TAG_ID_NOT_FOUND = 0;
    public static final int TAG_REMOVED = 1;

    private static final List<PersistentTag> persistentTags = new ArrayList<>();
    private static final Object tagsListLockObject = new Object();

    private static volatile String tagsLoggingStringPrefix = "";

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
     * or null if any of the parameters were null
     */
    @Nullable
    static String addPersistentTag(String key, String value) {
        if (key == null || value == null) {
            return null;
        }

        final PersistentTag persistentTag = new PersistentTag(key, value);
        synchronized (tagsListLockObject) {
            persistentTags.add(persistentTag);

            reconstructTagsStringPrefix();
            return persistentTag.getUniqueIdentifier();
        }
    }

    /**
     * Used for removing ALL the persistent tags having a particular {@link String} as the key
     *
     * @param key the (case-sensitive) key we are looking for in order to remove the tags
     * @return the number of tags that were removed based on the given key; 0 if the key is null
     */
    static int removeAllPersistentTagsFromKey(String key) {
        if (key == null) {
            return 0;
        }
        synchronized (tagsListLockObject) {
            int removedCount = 0;

            for (int index = persistentTags.size() - 1 ; index >= 0; --index) {
                if (persistentTags.get(index).getKey().equals(key)) {
                    ++removedCount;
                    persistentTags.remove(index);
                }
            }

            reconstructTagsStringPrefix();
            return removedCount;
        }
    }

    /**
     * Used for removing a particular persistent tag
     *
     * @param uniqueTagIdentifier the {@link String} unique identifier for the tag to be remove
     * @return {@value TAG_ID_NOT_FOUND} if the {@link String} identifier is not known;
     * {@value TAG_ID_IS_NULL} if the unique identifier is passed as null;
     * {@value TAG_REMOVED} if the tag was successfully removed
     */
    static int removePersistentTag(String uniqueTagIdentifier) {
        if (uniqueTagIdentifier == null) {
            return TAG_ID_IS_NULL;
        }

        synchronized (tagsListLockObject) {
            for (PersistentTag persistentTag : persistentTags) {
                if (persistentTag.getUniqueIdentifier().equals(uniqueTagIdentifier)) {
                    persistentTags.remove(persistentTag);

                    reconstructTagsStringPrefix();
                    return TAG_REMOVED;
                }
            }
            return TAG_ID_NOT_FOUND;
        }
    }

    /**
     * Used for removing all the persistent tags
     *
     * @return the number of tags removed
     */
    static int clearPersistentTags() {
        synchronized (tagsListLockObject) {
            final int tagsCount = persistentTags.size();
            try {
                persistentTags.clear();
            } catch (Exception e) {
                Log.w("JeeFoLoggerLibrary", "Something went wrong when removing the persistent tags");
                return persistentTags.size() - tagsCount;
            } finally {
                reconstructTagsStringPrefix();
            }
            return tagsCount;
        }
    }

    /**
     * @return hte {@link String} containing all the persistent tags to be added in the log message
     */
    static String getTagsStringPrefix() {
        return tagsLoggingStringPrefix;

    }

    /**
     * Used internally in the class to re-generate the string prefix
     * <p>
     * NOTE: This is used so we don't reconstruct the string for every log message as chances are
     * the persistent tags are very rarely modified
     */
    private static void reconstructTagsStringPrefix() {
        StringBuilder allTags = new StringBuilder();
        for (PersistentTag persistentTag : persistentTags) {
            allTags.append(persistentTag.toString());
        }

        tagsLoggingStringPrefix = allTags.toString();
    }
}
