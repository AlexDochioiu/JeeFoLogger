package com.jeefo.android.logger.utils;

import com.jeefo.android.jeefologger.ILog;
import com.jeefo.android.jeefologger.SmartLoggerFactory;

/**
 * Created by Alexandru Iustin Dochioiu on 5/27/2018
 */
public class SmartLoggerDemonstrationUtils {
    public static void printMeALog(ILog logger) {
        logger = SmartLoggerFactory.createSmartLogger(logger);

        logger.Info("SmartLogger: Printed log message");
        printMeSecondLog(logger);
    }

    private static void printMeSecondLog(ILog logger) {
        logger.Debug("SmartLogger: Printed second log message");
    }
}
