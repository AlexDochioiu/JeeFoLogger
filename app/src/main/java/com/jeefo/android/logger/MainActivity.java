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
import com.jeefo.android.jeefologger.SmartLogger;
import com.jeefo.android.logger.utils.SimpleUtils;


public class MainActivity extends AppCompatActivity {
    private ILog logger = new SmartLogger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JeefoLogger.initDebugLogger(this);

        SimpleClassWithPassedLog classOne = new SimpleClassWithPassedLog(logger);
        classOne.tellMeSomethingNasty();

        SimpleClassWithInstantiatedLog classTwo = new SimpleClassWithInstantiatedLog();
        classTwo.tellMeSomethingNice();

        SimpleUtils.printMeALog(logger);

//        logger.Debug("This is a debug message");
//        logger.Info("Info message with param: %s", "param");
//        logger.Warn("Warn message");
//
//        ILog newSmartLogger = new SmartLogger(logger);
//        newSmartLogger.Info("Now we're fucked");
//
//        //JeefoLogger.initPersistence(this);
//
//        logger.Error(new IllegalArgumentException("ArgExc"));
//        logger.Warn(new IllegalStateException("exc"), "Exception added: %s", "IllegalStateExc");
//
//        ILog newLog = new ScopedLogger(logger, String.class, true);
//        newLog.Info("Added second class to trace");
//
//        //JeefoLogger.addPersistentTag("USER", "username");
//
//        ILog secondNewLog = new ScopedLogger(newLog, InternalError.class, false);
//        secondNewLog.Debug("Added another logger with no instance");
//        secondNewLog.Error("Encountered %d errors while doing %s for %f seconds", 0, "nothing", 3.0f);
//        //String loggedTagUid = JeefoLogger.addPersistentTag("LOGGED", "FACEBOOK");
//        //JeefoLogger.addPersistentTag("LOGGED", "SAME_KEY");
//        secondNewLog.Info("Message with fucked up placeholders: %s");
//        secondNewLog.Warn("Message with fucked up placeholders again: %d", "StringArgInsteadOfInt");
//        //JeefoLogger.removeAllPersistentTagsFromKey("LOGGED");
//        secondNewLog.Error("Message with more placeholders than args %s %s", "onlyArg");
//
//        logger.Warn("Screwed up depth??");
//
//
//        //acceptable message as the string.format works
//        secondNewLog.Info("Message with placeholders which will not be displayed. %s", "whatever", 2, 3.2, logger);
    }
}
