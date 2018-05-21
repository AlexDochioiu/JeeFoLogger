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

/**
 * Created by Alexandru Iustin Dochioiu on 5/21/2018
 * <p>
 * Internal interface used for
 */
interface ITaggableLog extends ILog {
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
     */
    String addPersistentTag(String key, String value);

    /**
     * Used for removing all the persistent tags having a particular {@link String} as the key
     *
     * @param key the (case-sensitive) key we are looking for in order to remove the tags
     * @return the number of tags that were removed based on the given key
     */
    int removePersistentTagsFromKey(String key);

    /**
     * Used for removing a particular persistent tag
     *
     * @param uniqueTagIdentifier the {@link String} unique identifier for the tag to be remove
     * @return an integer indicating the status of the operation (such as tag removed, tag not removed, tag not found)
     */
    int removePersistentTag(String uniqueTagIdentifier);

    /**
     * Used for removing all the persistent tags
     *
     * @return the number of tags removed
     */
    int clearPersistentTags();
}
