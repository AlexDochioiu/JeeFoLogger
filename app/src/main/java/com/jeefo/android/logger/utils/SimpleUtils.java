package com.jeefo.android.logger.utils;

import com.jeefo.android.jeefologger.ILog;
import com.jeefo.android.jeefologger.SmartLogger;

/**
 * Created by Alexandru Iustin Dochioiu on 5/27/2018
 */
public class SimpleUtils {
    public static void printMeALog(ILog log) {
        ILog logger = new SmartLogger(log);
        logger.Info("Hard to believe it will work");

        printMeSecondLog(log);
        logger.Info("Stuff");
        printMeSecondLog(logger);
    }

    private static void printMeSecondLog(ILog log) {
        ILog logger = new SmartLogger(log);

        logger.Debug("hmm");
    }
}
