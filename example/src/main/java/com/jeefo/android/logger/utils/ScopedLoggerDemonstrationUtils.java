package com.jeefo.android.logger.utils;

import com.jeefo.android.jeefologger.ILog;
import com.jeefo.android.jeefologger.ScopedLogger;

/**
 * Created by Alexandru Iustin Dochioiu on 6/9/2018
 */
public class ScopedLoggerDemonstrationUtils {
    public static void printMeALog(ILog logger) {
        // this.getClass() can be used instead *ClassName*.class for non-static methods
        logger = new ScopedLogger(logger, ScopedLoggerDemonstrationUtils.class, "printMeALog");

        logger.Info("ScopedLogger: Printed log message");
        printMeSecondLog(logger);
    }

    private static void printMeSecondLog(ILog logger) {
        logger = new ScopedLogger(logger, "printMeSecondLog");
        logger.Debug("ScopedLogger: Printed second log message");
    }
}
