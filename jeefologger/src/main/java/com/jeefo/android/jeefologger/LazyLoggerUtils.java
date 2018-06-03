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
import android.util.Pair;

import java.util.LinkedList;
import java.util.List;

import static com.jeefo.android.jeefologger.SmartLoggerUtils.getClassNameFromFileName;

/**
 * Created by Alexandru Iustin Dochioiu on 6/3/2018
 */
public class LazyLoggerUtils {
    /**
     * Queries the entire stack trace and groups all method calls and classes part of a given package
     * in the calling order. It automatically groups consecutive method calls made part of the same class.
     * It also automatically groups method calls made through anonymous classes.
     *
     * @param packageName only the method calls made from classes containing this package name will
     *                    be considered
     * @return the entire ordered trace of method calls with the class they originated from (only for
     * the classes part of the given package)
     */
    static List<Pair<String, LinkedList<String>>> getAllTraceForPackage(@NonNull String packageName) {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        final LinkedList<Pair<String, LinkedList<String>>> packageCalls = new LinkedList<>();

        for (int index = ste.length - 1; index >= 0; --index) {
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
}
