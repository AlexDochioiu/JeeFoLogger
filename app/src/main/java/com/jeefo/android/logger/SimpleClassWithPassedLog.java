package com.jeefo.android.logger;

import com.jeefo.android.jeefologger.ILog;
import com.jeefo.android.jeefologger.SmartLoggerFactory;

/**
 * Created by Alexandru Iustin Dochioiu on 5/27/2018
 */
public class SimpleClassWithPassedLog {
    final ILog logger;

    public SimpleClassWithPassedLog(ILog logger) {
        this.logger = SmartLoggerFactory.createSmartLogger(logger);
    }

    public void tellMeSomethingNasty() {
        logger.Warn("Spank me");
    }
}
