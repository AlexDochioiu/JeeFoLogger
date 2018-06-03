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

    @NonNull
    static String getSimpleClassName(int depth) {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

        return getClassNameFromFileName(ste[depth].getFileName());
    }

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

            if (className.compareTo(elementClassName) == 0 || ( anonymousHostClassName != null && className.compareTo(anonymousHostClassName) == 0 )) {
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

    static String getFullClassName(int depth) {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

        return ste[depth].getClassName();
    }

    static List<Pair<String, List<String>>> getAllTraceForPackage(String packageName) {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        final List<Pair<String, List<String>>> packageCalls = new LinkedList<>();

        for (StackTraceElement traceElement : ste) {
            if (traceElement.getClassName().contains(packageName)) {
                //todo: use the full class name with package to compare if one class is the same or not
                final String className = getClassNameFromFileName(traceElement.getFileName());
                final String methodName = traceElement.getMethodName();

                if (packageCalls.size() > 0) {
                    if (className.equals(packageCalls.get(packageCalls.size() - 1).first)) {
                        // add a second method to the same class then continue
                        packageCalls.get(packageCalls.size() - 1).second.add(methodName);
                        continue;
                    }
                }

                final List<String> methodCalls = new LinkedList<>();
                methodCalls.add(traceElement.getMethodName());

                packageCalls.add(new Pair<>(className, methodCalls));
            }
        }

        return packageCalls;
    }

    @NonNull
    private static String getClassNameFromFileName(@NonNull String fileName) {
        return fileName.substring(0, fileName.indexOf('.'));
    }

    public static boolean checkRunnable(int depth) {
        return Thread.currentThread().getStackTrace()[depth].getClassName().contains("$");
    }
}
