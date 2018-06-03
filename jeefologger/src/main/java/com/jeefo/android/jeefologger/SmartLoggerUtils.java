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
import android.util.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Alexandru Iustin Dochioiu on 5/27/2018
 */
class SmartLoggerUtils {

    /**
     * Queries a {@link List} of {@link StackTraceElement} for the first occurrence of a class
     *
     * @param className the full name (with package) of the class we are looking for in the trace list
     * @param elements the {@link LinkedList} of {@link StackTraceElement} to be inspected
     * @return the method name for the first occurrence of a given class or null if the class never
     *          occurred in the list
     */
    @Nullable
    static String getMethodName(String className, @Nullable LinkedList<StackTraceElement> elements) {
        if (elements == null) {
            return null;
        }

        String methodName = null;

        while (elements.size() > 0) {
            String elementClassName = elements.getFirst().getClassName();
            String anonymousHostClassName = null;

            if (elementClassName.contains("$")) {
                // that generally means it's an anonymous class such as runnable
                anonymousHostClassName = elementClassName.substring(0, elementClassName.indexOf('$'));
            }

            if (className.compareTo(elementClassName) == 0 || (anonymousHostClassName != null && className.compareTo(anonymousHostClassName) == 0)) {
                methodName = elements.getFirst().getMethodName();
                elements.removeFirst();

                if (anonymousHostClassName != null) {
                    methodName = String.format(Locale.UK, "%s <- %s#%s(args)", methodName, getClassNameFromFileName(elements.getFirst().getFileName()), elements.getFirst().getMethodName());
                    elements.removeFirst();
                }
                break;
            }
            elements.removeFirst();
        }

        return methodName;
    }

    /**
     * Queries the stack trace for a simple class name
     *
     * @param depth the depth in the stack trace for the class we desire the name of
     * @return the simple name of the class
     */
    @NonNull
    static String getSimpleClassName(int depth) {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

        return getClassNameFromFileName(ste[depth].getFileName());
    }

    /**
     * Queries the stack trace for a full class name, containing the package as well
     *
     * @param depth the depth in the stack trace for the class we desire the name of
     * @return the full name of the class, including package
     */
    static String getFullClassName(int depth) {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

        return ste[depth].getClassName();
    }

    /**
     * @param fileName the name of the class file
     * @return the simple name of the class
     */
    @NonNull
    static String getClassNameFromFileName(@NonNull String fileName) {
        return fileName.substring(0, fileName.indexOf('.'));
    }
}
