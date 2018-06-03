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
import android.support.annotation.Nullable;

/**
 * Created by Alexandru Iustin Dochioiu on 6/3/2018
 *
 * Factory class used for creating new {@link SmartLogger} instances
 */
public class SmartLoggerFactory {

    /**
     * private constructor
     */
    private SmartLoggerFactory() {
    }

    /**
     * @return a new {@link SmartLogger} which will not have an instance tag
     */
    @NonNull
    public static SmartLogger createSmartLogger() {
        return new SmartLogger(false);
    }

    /**
     * Adding the instance tag can be helpful in the scenarios such as (but not limited to):
     * 1. You create a SmartLogger for an activity and then you start chaining from it. At some point
     * you start a second activity, do whatever it is you need and then go back to the previous
     * activity. A bit more work is done and then you trigger an exception. Without the instance
     * tag you might not now for sure if the activity was the same one before starting Activity2,
     * or it was a newly created activity, so tracking what went wrong will be harder.
     * 2. You keep on using a complex asynctask class you created multiple times during the lifetime
     * of your program. Adding an instance tag to it will help you track down whether two log
     * messages are part of the same task or not
     *
     * @param addInstanceTag whether this {@link SmartLogger} should also have an instance tag so
     *                       you can track which instance of a class/fragment/activity/etc initiated
     *                       a call
     * @return a newly created {@link SmartLogger}
     */
    @NonNull
    public static SmartLogger createSmartLogger(boolean addInstanceTag) {
        return new SmartLogger(addInstanceTag);
    }

    @NonNull
    public static SmartLogger createSmartLogger(@Nullable ILog logger) {
        if (logger instanceof SmartLogger) {
            if (isSameClass((SmartLogger) logger)) {
                return (SmartLogger) logger;
            } else {
                return new SmartLogger(logger, false);
            }
        } else {
            return new SmartLogger(logger, false);
        }
    }

    /**
     * Adding the instance tag can be helpful in the scenarios such as (but not limited to):
     * 1. You create a SmartLogger for an activity and then you start chaining from it. At some point
     * you start a second activity, do whatever it is you need and then go back to the previous
     * activity. A bit more work is done and then you trigger an exception. Without the instance
     * tag you might not now for sure if the activity was the same one before starting Activity2,
     * or it was a newly created activity, so tracking what went wrong will be harder.
     * 2. You keep on using a complex asynctask class you created multiple times during the lifetime
     * of your program. Adding an instance tag to it will help you track down whether two log
     * messages are part of the same task or not
     *
     * @param logger         the {@link ILog} on top of which the new tags will be added
     * @param addInstanceTag whether this {@link SmartLogger} should also have an instance tag so
     *                       you can track which instance of a class/fragment/activity/etc initiated
     *                       a call
     * @return the {@link SmartLogger} extended with new tags
     */
    @NonNull
    public static SmartLogger createSmartLogger(@Nullable ILog logger, boolean addInstanceTag) {
        if (logger instanceof SmartLogger) {
            if (isSameClass((SmartLogger) logger)) {
                return (SmartLogger) logger;
            } else {
                return new SmartLogger(logger, addInstanceTag);
            }
        } else {
            return new SmartLogger(logger, addInstanceTag);
        }
    }

    /**
     * Verifies if the {@link SmartLogger} to be extended was created in the same class as the
     * one which requested a new {@link SmartLogger}.
     *
     * @param smartLogger the {@link SmartLogger} to be extended
     * @return true if the {@link SmartLogger} was created in the same class as the
     *          one which requested a new {@link SmartLogger}; false otherwise
     */
    private static boolean isSameClass(SmartLogger smartLogger) {
        String upperClassName = smartLogger.getFullClientClassName();
        if (upperClassName.contains("$")) {
            upperClassName = upperClassName.substring(0, upperClassName.indexOf("$"));
        }

        String className = SmartLoggerUtils.getFullClassName(5);
        if (className.contains("$")) {
            className = className.substring(0, className.indexOf("$"));
        }

        return upperClassName.equals(className);
    }
}
