package com.jeefo.android.logger;

import com.jeefo.android.jeefologger.LazyLogger;
import com.jeefo.android.jeefologger.ILog;
import com.jeefo.android.jeefologger.SmartLoggerFactory;

/**
 * Created by Alexandru Iustin Dochioiu on 5/27/2018
 */
public class SimpleClassWithInstantiatedLog {
    private final ILog logger = SmartLoggerFactory.createSmartLogger();
    private final ILog debugLog = new LazyLogger();

    public void tellMeSomethingNice() {
        logger.Warn("What's that nice logger you have over there?");
        debugLog.Debug("test");
    }
}
