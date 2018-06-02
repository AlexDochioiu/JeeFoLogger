package com.jeefo.android.logger.utils;

import com.jeefo.android.jeefologger.ILog;
import com.jeefo.android.jeefologger.LazyLogger;
import com.jeefo.android.jeefologger.ScopedLogger;
import com.jeefo.android.jeefologger.SmartLogger;

/**
 * Created by Alexandru Iustin Dochioiu on 5/27/2018
 */
public class SimpleUtils {
    private static ILog lazyLogger = new LazyLogger();

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

        lazyLogger.Warn("Is this fine? %s", "is it?");
        final ILog scopedLog = new ScopedLogger(lazyLogger, String.class, true);
        scopedLog.Error("Whatever");
    }
}
