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

import java.util.UUID;

/**
 * Created by Alexandru Iustin Dochioiu on 02/01/18.
 */

class UuidCustomUtils {
    /**
     * Static method used for generating an unique identifier
     * @return the generated uid
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Static method used for generating a 6 digits identifier
     * @return the generated identifier
     */
    public static String generateShortUUID() {
        return UUID.randomUUID().toString().substring(0, 6);
    }
}