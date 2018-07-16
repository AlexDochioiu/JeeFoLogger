package com.jeefo.android.jeefologger;

import android.support.annotation.IntDef;

import static com.jeefo.android.jeefologger.LogLevel.DEBUG;
import static com.jeefo.android.jeefologger.LogLevel.ERROR;
import static com.jeefo.android.jeefologger.LogLevel.INFO;
import static com.jeefo.android.jeefologger.LogLevel.NONE;
import static com.jeefo.android.jeefologger.LogLevel.VERBOSE;
import static com.jeefo.android.jeefologger.LogLevel.WARN;
import static com.jeefo.android.jeefologger.LogLevel.WTF;

/**
 * Created by Alexandru Iustin Dochioiu on 7/15/2018
 */
@IntDef({VERBOSE, DEBUG, INFO, WARN, ERROR, WTF, NONE})
public @interface LogLevel {
    int VERBOSE = 0;
    int DEBUG = 1;
    int INFO = 2;
    int WARN = 3;
    int ERROR = 4;
    int WTF = 5;
    int NONE = 6;
}
