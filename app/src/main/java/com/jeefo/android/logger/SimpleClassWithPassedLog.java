package com.jeefo.android.logger;

import com.jeefo.android.jeefologger.ILog;
import com.jeefo.android.jeefologger.SmartLogger;

/**
 * Created by Alexandru Iustin Dochioiu on 5/27/2018
 */
public class SimpleClassWithPassedLog {
    final ILog logger;

    public SimpleClassWithPassedLog(ILog logger) {
        this.logger = new SmartLogger(logger);
    }

    public void tellMeSomethingNasty() {
        logger.Warn("Spank me");
    }
}
