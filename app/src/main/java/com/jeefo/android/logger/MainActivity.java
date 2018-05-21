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

import com.jeefo.android.log.ILog;
import com.jeefo.android.log.JeefoLogger;
import com.jeefo.android.log.ScopedLogger;

public class MainActivity extends AppCompatActivity {
    private ILog logger = new ScopedLogger(getClass());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logger.Debug("This is a debug message");
        logger.Info("Info message with param: %s", "param");
        logger.Warn("Warn message");

        JeefoLogger.initPersistence(this);

        logger.Error(new IllegalArgumentException("ArgExc"));
        logger.Warn(new IllegalStateException("exc"), "Exception added: %s", "IllegalStateExc");

        ILog newLog = new ScopedLogger(logger, String.class, true);
        newLog.Info("Added second class to trace");

        ILog secondNewLog = new ScopedLogger(newLog, InternalError.class, false);
        secondNewLog.Debug("Added another logger with no instance");
    }
}
