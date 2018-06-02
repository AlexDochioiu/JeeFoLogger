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

import android.support.annotation.NonNull;

/**
 * Created by Alexandru Iustin Dochioiu on 5/27/2018
 */
@SuppressWarnings("WeakerAccess")
final class PersistentTag {

    @NonNull
    private final String uniqueIdentifier;
    @NonNull
    private final String key;
    @NonNull
    private final String value;

    /**
     * The unique identifier is a {@link String} used to uniquely identify any persistent tag. This
     * will be required as we do not enforce the key to be unique.
     *
     * @return the uid
     */
    @NonNull
    public String getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    /**
     * the key is the first part of the tag: "[KEY value]".
     * <p>
     * NOTE: The key is not enforced to be unique (but it is recommended that we have a single tag
     * with a particular key for clarity)
     *
     * @return the key string
     */
    @NonNull
    public String getKey() {
        return key;
    }

    /**
     * the value is the second part of the tag: "[key VALUE]"
     *
     * @return the value string
     */
    @NonNull
    public String getValue() {
        return value;
    }

    /**
     * Constructor
     *
     * @param key   the tag's key (first part of the tag in its string format)
     * @param value the tag's value (second part of the tag in its string format)
     */
    PersistentTag(@NonNull String key, @NonNull String value) {
        this.uniqueIdentifier = UuidCustomUtils.generateUUID();
        this.key = key;
        this.value = value;
    }

    /**
     * @return our tag as we want to be displayed in the log: "[KEY VALUE]"
     */
    @Override
    public String toString() {
        return String.format("[%s %s]", key, value);
    }

    /**
     * We verify equality based on the unique identifier we defined.
     *
     * @param other the {@link Object} to which we compare for equality
     * @return true if both objects are {@link PersistentTag} and their uniqueIdentifier matches
     */
    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        return this.getUniqueIdentifier().equals(((PersistentTag) other).getUniqueIdentifier());
    }

    /**
     * Modify the hashCode to match our new equality logic
     *
     * @return an int value for the hashcode of the tag
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + uniqueIdentifier.hashCode();
        return result;
    }
}
