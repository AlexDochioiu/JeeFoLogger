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

    static String getFullClassName(int depth) {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

        return ste[depth].getClassName();
    }

    static List<Pair<String, LinkedList<String>>> getAllTraceForPackage(@NonNull String packageName) {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        final LinkedList<Pair<String, LinkedList<String>>> packageCalls = new LinkedList<>();

        for (int index = ste.length - 1; index >= 0; -- index) {
            if (ste[index].getClassName().contains(packageName)) {
                final boolean isAnonymousClass = ste[index].getClassName().contains("$");
                final String className = getClassNameFromFileName(ste[index].getFileName());
                String methodName = ste[index].getMethodName();

                if (packageCalls.size() > 0) {
                    if (className.equals(packageCalls.getLast().first)) {
                        if (!isAnonymousClass) {
                            // add a second method to the same class then continue
                                packageCalls.getLast().second.add(methodName);

                        } else {
                            final String callingMethodName = packageCalls.getLast().second.removeLast();
                            methodName = String.format("%s <- %s#%s(args)", methodName, packageCalls.getLast().first, callingMethodName);
                            packageCalls.getLast().second.add(methodName);
                        }
                        continue;
                    }
                }

                if (!isAnonymousClass) {
                    final LinkedList<String> methodCalls = new LinkedList<>();
                    methodCalls.add(ste[index].getMethodName());

                    packageCalls.add(new Pair<>(className, methodCalls));
                } else {
                    StringBuilder callingMethodsName = new StringBuilder();
                    for (String methdCall : packageCalls.getLast().second) {
                        callingMethodsName.append('#').append(methdCall).append("(args)");
                    }
                    methodName = String.format("%s <- %s%s", methodName, packageCalls.getLast().first, callingMethodsName.toString());
                    packageCalls.removeLast();
                    packageCalls.getLast().second.add(methodName);
                }
            }
        }

        return packageCalls;
    }

    @NonNull
    private static String getClassNameFromFileName(@NonNull String fileName) {
        return fileName.substring(0, fileName.indexOf('.'));
    }
}
