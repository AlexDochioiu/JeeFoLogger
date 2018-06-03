/*
 * Copyright 2018 Alexandru Iustin Dochioiu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeefo.android.logger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jeefo.android.jeefologger.ILog;
import com.jeefo.android.jeefologger.JeefoLogger;
import com.jeefo.android.jeefologger.LazyLogger;
import com.jeefo.android.jeefologger.ScopedLogger;
import com.jeefo.android.jeefologger.SmartLoggerFactory;
import com.jeefo.android.logger.utils.RunnableUtils;


public class MainActivity extends AppCompatActivity {
    private ILog logger = SmartLoggerFactory.createSmartLogger();
    private Runnable runnable;
    private Runnable outerRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JeefoLogger.initDebugLogger(this);

        SimpleClassWithPassedLog classOne = new SimpleClassWithPassedLog(logger);
        //classOne.tellMeSomethingNasty();

        SimpleClassWithInstantiatedLog classTwo = new SimpleClassWithInstantiatedLog();
        //classTwo.tellMeSomethingNice();

        //SimpleUtils.printMeALog(logger);

//        logger.Debug("This is a debug message");
//        logger.Info("Info message with param: %s", "param");
//        logger.Warn("Warn message");

        ILog newSmartLogger = SmartLoggerFactory.createSmartLogger(logger);
//        newSmartLogger.Info("Now we're fucked");

        JeefoLogger.initPersistence(this);

//        logger.Error(new IllegalArgumentException("ArgExc"));
//        logger.Warn(new IllegalStateException("exc"), "Exception added: %s", "IllegalStateExc");

        ILog newLog = new ScopedLogger(logger, String.class, true);
//        newLog.Info("Added second class to trace");

        //JeefoLogger.addPersistentTag("USER", "username");

        ILog secondNewLog = new ScopedLogger(newLog, InternalError.class, false);
//        secondNewLog.Debug("Added another logger with no instance");
//        secondNewLog.Error("Encountered %d errors while doing %s for %.2f seconds", 0, "nothing", 3.237f);
        //String loggedTagUid = JeefoLogger.addPersistentTag("LOGGED", "FACEBOOK");
        //JeefoLogger.addPersistentTag("LOGGED", "SAME_KEY");
//        secondNewLog.Info("Message with fucked up placeholders: %s");
//        secondNewLog.Warn("Message with fucked up placeholders again: %d", "StringArgInsteadOfInt");
        //JeefoLogger.removeAllPersistentTagsFromKey("LOGGED");
//        secondNewLog.Error("Message with more placeholders than args %s %s", "onlyArg");

//        logger.Warn("Screwed up depth??");


        //acceptable message as the string.format works
//        secondNewLog.Info("Message with placeholders which will not be displayed. %s", "whatever", 2, 3.2, logger);

        runnable = new Runnable() {
            @Override
            public void run() {
                //logger.Debug("Inside the runnable");
                //ILog rLogger = SmartLoggerFactory.createSmartLogger(logger);
                //rLogger.Error("rLogger MainActivity");
                ILog ll = new LazyLogger();
                ll.Info("test");
            }
        };
        outerRunnable = new Runnable() {
            @Override
            public void run() {
                logger.Debug("MainActivity SmartLogger");
                ILog rLogger = SmartLoggerFactory.createSmartLogger(logger);
                rLogger.Error("new SmartLogger");
                ILog ll = new LazyLogger();
                ll.Info("LazyLogger Message");
            }
        };

        runnable.run();
        RunnableUtils.runMyRunnable(outerRunnable);
        RunnableUtils.callRunMyRunnable(outerRunnable);
        intermediateMethod();
    }

    void callMainRunnable() {
        outerRunnable.run();
    }

    void intermediateMethod() {
        callMainRunnable();
    }
}
