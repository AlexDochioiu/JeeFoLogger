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

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jeefo.android.jeefologger.ILog;
import com.jeefo.android.jeefologger.JeefoLogger;
import com.jeefo.android.jeefologger.SmartLoggerFactory;
import com.jeefo.android.logger.utils.LazyLoggerDemonstrationUtils;
import com.jeefo.android.logger.utils.OnMyExampleEventListener;
import com.jeefo.android.logger.utils.ScopedLoggerDemonstrationUtils;
import com.jeefo.android.logger.utils.SmartLoggerDemonstrationUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    // Create an instance of the smart logger!
    private ILog logger = SmartLoggerFactory.createSmartLogger();

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd", Locale.UK);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // NOTE: WHEN LOOKING AT THE LOG MESSAGES IN LOGCAT, I RECOMMEND FILTERING BY "JeeFo" SO YOU GET ONLY THE RELEVANT LOG MESSAGES

        logger.Info("This message is logged before initializing persistence so it will not appear in the log file");


        // Setup the persistence for the logger
        // NOTE: Keep in mind that the log files are not stored securely and they can be easily
        //          accessed using a file manager software. Secured storage is planned for one
        //          of the future releases. Until then, you might want to keep persistence only
        //          for in-house testing and turn it off in the release (by removing the following
        //          line only - nothing else will be affected by it)
        //
        // Setup the lazy logger
        // NOTE: I would always add this line as it can never do harm. Without it, the lazy logger
        // will not work as expected. If you check the implementation of that method, you
        // will see that it's doing nothing more than identifying the base package of the module
        new JeefoLogger.Builder(this)
                .withPersistence(true)
                .withLazyLogger(true)
                .buildAndInit();

        // To fetch the log files at any point, use the following. This method currently works only after buildAndInit() but this will change in a future version
        // JeefoLogger.getAllLogFiles(); // THIS RETURNES AN ARRAY WITH THE LOG FILES ON THE DEVICE

        logger.Debug("Persistence was initialized so from now on, all the log messages will be stored in the file as well");
        logger.Info("The name of today's log file is: %s_Log.txt", simpleDateFormat.format(new Date()));
        logger.Info("As you've noticed above, the log messages have built-in support for String formatting. Let's see some examples!!");
        logger.Debug("All the extra arguments will be ignored: %s, %.2f", "first_argument", 3.51232, "extra_argument");

        // If you do this by mistake, it will keep the message as it is and add all the arguments at the end of it
        logger.Warn("Adding more placeholders than arguments is no biggie either %.2f, %s.", 3.51232);

        // Wrong argument types (not matching placeholders) will also keep the message intact while adding the arguments at the end
        logger.Debug("Using the wrong placeholder is also not the end of the world: %.2f", "string_argument");

        logger.Warn("-----------------------------------------------------------------------------------------------------------------");
        logger.Error("------------------------------------Loggers Comparison for Identical Task----------------------------------------");
        // Quick demonstration of using smart logger inside utils classes
        SmartLoggerDemonstrationUtils.printMeALog(logger);

        ScopedLoggerDemonstrationUtils.printMeALog(logger); // this requires the most explicit tags but provides the most control in exchange

        LazyLoggerDemonstrationUtils.printMeALog();

        logger.Error("-----------------------------------------------------------------------------------------------------------------");
        logger.Warn("-----------------------------------------------------------------------------------------------------------------");

        // we can add persistent tags which will appear in all future log messages (logged by ALL ILog instances created on any thread)
        String persistentTagUid = JeefoLogger.addPersistentTag("USER", "john"); // see method docs for the return
        logger.Debug("This message has the persistent tag in front, as you can see");
        JeefoLogger.removePersistentTag(persistentTagUid);
        logger.Info("We now removed the tag");

        // the key of the tag does not have to be unique; we also do not necessarily need the uid in order to remove persistent tags
        JeefoLogger.addPersistentTag("PT", "1");
        JeefoLogger.addPersistentTag("PT", "2");
        JeefoLogger.addPersistentTag("USER", "billy@gmail.com");
        logger.Debug("This message has %d persistent tags", 3);
        JeefoLogger.removeAllPersistentTagsFromKey("PT");
        logger.Debug("We now removed all the persistent tags which had the key: %s", "PT");
        JeefoLogger.removeAllPersistentTagsFromKey("USER");
        logger.Info("We also removed the last persistent tag");


        // Create a bundle object and put the logger in there (the ILog interface is Serializable)
        Bundle bundle = new Bundle();
        bundle.putSerializable(BlankFragment.BUNDLE_LOGGER, logger);

        // Put the bundle as args for the new fragment
        Fragment fragment = new BlankFragment();
        fragment.setArguments(bundle);

        // Start the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main_frameLayout, fragment)
                .commit();
    }

    void calledFromFragment(OnMyExampleEventListener exampleEventListener) {
        logger.Info("Now using the logger defined in MainActivity. It still works.");

        // The tags explained:
        // [Class MainActivity]                 -> where the initial logger was created
        // [Class BlankFragment]                -> class where the method we are running was defined
        // [Instance ******]                    -> see comment added in BlankFragment#onCreate() when we created the SmartLogger instance
        // [Method onCrashAndBurn               -> the method we are running
        // MainActivity#                        -> the class which triggered the method from the anonymous class
        // calledFromFragment()             -> the method which triggered the method from the anonymous class
        exampleEventListener.onCrashAndBurn();
    }

    void calledFromFragmentWithLazyLogger(OnMyExampleEventListener exampleEventListener) {
        logger.Info("The next message you will see is constructed with the LazyLogger");

        // NOTE: you can look at it as such:
        // [Class BlankFragment][Instance ******][Method onCreate][Method run()                         -> is what we are running and where it was defined
        //  <- BlankFragment#onCrashAndBurn() <- MainActivity#calledFromFragmentWithLazyLogger()        -> where this anonymous class had the method ran and how we got there
        exampleEventListener.onCrashAndBurn();

        // The above tags explained:
        // [Class BlankFragment]                -> class where the anonymous OnMyExampleEventListener was defined
        // [Instance ******]                    -> see comment added in BlankFragment#onCreate() when we created the SmartLogger instance
        // [Method onCreate]                    -> method where the anonymous OnMyExampleEventListener was defined
        // [Method run()                        -> the method we are running
        // <- BlankFragment#                    -> class where the anonymous Runnable was defined
        // #onCrashAndBurn()                    -> method where the anonymous Runnable was defined
        // <- MainActivity                      -> class where the Runnable is run
        // #calledFromFragmentWithLazyLogger()  -> the method where the runnable is run
    }
}
