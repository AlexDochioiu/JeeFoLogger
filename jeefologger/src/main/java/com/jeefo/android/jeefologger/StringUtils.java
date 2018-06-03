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
 * Created by Alexandru Iustin Dochioiu on 5/26/2018
 */
class StringUtils {

    private static final String LOG_MISSING_ALL_ARGS = "no_args_passed"; // no args when there are placeholders
    private static final String LOG_WITH_ARGS_AND_NO_MESSAGE = "no_message_provided"; // args present but message is null or empty string

    /**
     * Computes the formatted message by replacing the placeholders with the values
     *
     * @param ex      the exception whose message will be logged
     * @param message the message containing placeholders
     * @param args    the {@link Object} arguments to replace the placeholders
     * @return the formatted string message
     * <p>
     * NOTE: If there are more placeholders than arguments (or the type of a placeholder does not
     * match the argument and the cast fails), the placeholders will be left untouched and the
     * arguments will be listed as part of the log message (at the end of the message but before
     * the exception message). If there are more arguments than placeholders, the extra arguments
     * will be ignored.
     */
    static String getFormattedMessage(@Nullable Exception ex, @Nullable String message, Object... args) {
        StringBuilder formattedMessage = new StringBuilder();

        if (message != null && !message.equals("")) {
            // if there's a message, try and format it
            try {
                formattedMessage.append(String.format(message, args));
            } catch (Exception e) {
                // if formatting failed, add the message and the args separately and the end
                formattedMessage.append(message);
                appendArgsAtEndOfString(formattedMessage, args);
            }
        } else {
            // there's no message, do we have any args? If the answer is yes, put them there
            if (args.length != 0) {
                formattedMessage.append(LOG_WITH_ARGS_AND_NO_MESSAGE);
                appendArgsAtEndOfString(formattedMessage, args);
            }
        }

        // If there's an exception, log its message
        if (ex != null) {
            formattedMessage.append(" :: ").append(ex.getMessage());
        }
        return formattedMessage.toString();
    }

    /**
     * Appends all the args at the end of a {@link StringBuilder} . toString() is called for the args.
     * <p>
     * NOTE: This method should never be called unless there is a problem with doing String.format()
     * on the message (either args do not match placeholders or args are present when there's no
     * message)
     *
     * @param stringBuilder the {@link StringBuilder} to which we add the args at the end
     * @param args          the {@link Object} collection which will be added at the end of the string builder (using the toString() method)
     */
    private static void appendArgsAtEndOfString(@NonNull StringBuilder stringBuilder, Object... args) {
        stringBuilder.append(" - args: ");
        if (args.length != 0) {
            for (Object arg : args) {
                stringBuilder.append(arg.toString()).append(";");
            }
        } else {
            stringBuilder.append(LOG_MISSING_ALL_ARGS);
        }
    }
}
