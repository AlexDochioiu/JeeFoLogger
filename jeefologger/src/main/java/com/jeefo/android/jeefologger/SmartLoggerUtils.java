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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Alexandru Iustin Dochioiu on 5/27/2018
 */
class SmartLoggerUtils {

    @NonNull
    static String getSimpleClassName(int depth) {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

        return getClassNameFromFileName(ste[depth].getFileName());
    }

    /**
     * Get the method name for a depth in call stack. <br />
     * Utility function
     *
     * @return method name
     */
    static String getMethodName(int depth) {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

        return ste[depth].getMethodName();
    }

    @Nullable
    static String getMethodName(String className, int depth) {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

        for (StackTraceElement traceElement : ste) {
            if (traceElement.getClassName().matches(className)) {
                if (--depth == 0) {
                    return traceElement.getMethodName();
                }
            }
        }

        return null;
    }

    static String getFullClassName(int depth) {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

        return ste[depth].getClassName();
    }

    static List<Pair<String, List<String>>> getAllTraceForPackage(String packageName) {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        final List<Pair<String, List<String>>> packageCalls = new LinkedList<>();
        for (int index = 0; index < ste.length; ++index) {
            if (ste[index].getClassName().contains(packageName)) {
                final StackTraceElement element = ste[index];
                //todo: use the full class name with package to compare if one class is the same or not
                final String className = getClassNameFromFileName(element.getFileName());
                final String methodName = element.getMethodName();

                if (packageCalls.size() > 0) {
                    if (className.equals(packageCalls.get(packageCalls.size() - 1).first)) {
                        // add a second method to the same class then continue
                        packageCalls.get(packageCalls.size() - 1).second.add(methodName);
                        continue;
                    }
                }

                final List<String> methodCalls = new LinkedList<>();
                methodCalls.add(element.getMethodName());

                packageCalls.add(new Pair<>(className, methodCalls));
            }
        }

        return packageCalls;
    }

    @NonNull
    private static String getClassNameFromFileName(@NonNull String fileName) {
        return fileName.substring(0, fileName.indexOf('.'));
    }
}
