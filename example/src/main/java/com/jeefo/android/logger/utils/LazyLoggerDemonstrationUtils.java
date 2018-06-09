package com.jeefo.android.logger.utils;

import com.jeefo.android.jeefologger.LazyLogger;

/**
 * Created by Alexandru Iustin Dochioiu on 6/9/2018
 */
public class LazyLoggerDemonstrationUtils {
    public static void printMeALog() {

        LazyLogger.Info("LazyLogger: Printed log message");
        printMeSecondLog();
    }

    private static void printMeSecondLog() {
        LazyLogger.Debug("LazyLogger: Printed second log message");
    }
}
