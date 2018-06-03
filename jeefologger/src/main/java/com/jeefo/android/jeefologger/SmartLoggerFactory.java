package com.jeefo.android.jeefologger;

import android.support.annotation.Nullable;

/**
 * Created by Alexandru Iustin Dochioiu on 6/3/2018
 */
public class SmartLoggerFactory {

    /**
     * private constructor
     */
    private SmartLoggerFactory() {}

    public static SmartLogger createSmartLogger() {
        return new SmartLogger();
    }

    public static SmartLogger createSmartLogger(@Nullable ILog logger) {
        if (logger instanceof SmartLogger) {
            if (isSameClass((SmartLogger) logger)) {
                return (SmartLogger) logger;
            } else {
                return new SmartLogger(logger);
            }
        } else {
            return new SmartLogger(logger);
        }
    }

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
