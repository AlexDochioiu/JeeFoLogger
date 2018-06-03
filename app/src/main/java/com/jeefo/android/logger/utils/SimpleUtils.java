package com.jeefo.android.logger.utils;

import com.jeefo.android.jeefologger.ILog;
import com.jeefo.android.jeefologger.LazyLogger;
import com.jeefo.android.jeefologger.SmartLoggerFactory;

/**
 * Created by Alexandru Iustin Dochioiu on 5/27/2018
 */
public class SimpleUtils {
    private static ILog lazyLogger = new LazyLogger();

    public static void printMeALog(ILog log) {
        ILog logger = SmartLoggerFactory.createSmartLogger(log);
        //logger.Debug("Hard to believe it will work");

        printMeSecondLog(log);
        //logger.Debug("Stuff");
        printMeSecondLog(logger);
    }

    private static void printMeSecondLog(ILog log) {
        ILog logger = SmartLoggerFactory.createSmartLogger(log);

        logger.Debug("hmm");

        //lazyLogger.Warn("Is this fine? %s", "is it?");
        //final ILog scopedLog = new ScopedLogger(lazyLogger, String.class, true);
        //scopedLog.Error("Whatever");
    }
}
