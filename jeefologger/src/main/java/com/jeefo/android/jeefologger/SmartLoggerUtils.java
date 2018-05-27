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

/**
 * Created by Alexandru Iustin Dochioiu on 5/27/2018
 */
class SmartLoggerUtils {

    static String getClassName() {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

        final String fileName = ste[4].getFileName();
        return fileName.substring(0, fileName.indexOf('.'));
    }

    /**
     * Get the method name for a depth in call stack. <br />
     * Utility function
     * @return method name
     */
    static String getMethodName(int depth)
    {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

        //System. out.println(ste[ste.length-depth].getClassName()+"#"+ste[ste.length-depth].getMethodName());
        // return ste[ste.length - depth].getMethodName();  //Wrong, fails for depth = 0
        return ste[depth].getMethodName(); //Thank you Tom Tresansky
    }

    static String getMethodName(String className, int depth)
    {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

        for (StackTraceElement traceElement : ste) {
            if (traceElement.getClassName().matches(className)) {
                if (--depth == 0) {
                    return traceElement.getMethodName();
                }
            }
        }

        return "";
    }

    public static String getFullClassName() {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

        return ste[5].getClassName();
    }
}
