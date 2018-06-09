package com.jeefo.android.logger;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeefo.android.jeefologger.ILog;
import com.jeefo.android.jeefologger.LazyLogger;
import com.jeefo.android.jeefologger.SmartLoggerFactory;
import com.jeefo.android.logger.utils.OnMyExampleEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("ConstantConditions")
public class BlankFragment extends Fragment {
    public static final String BUNDLE_LOGGER = "com.jeefo.android.logger.bundle.logger";

    private ILog logger;

    public BlankFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            final ILog log = (ILog) getArguments().get(BUNDLE_LOGGER);

            logger = SmartLoggerFactory.createSmartLogger(log, true); // we create a smart logger by extending the previous one
            // NOTICE THAT WE ALSO ADDED AN INSTANCE TAG TO IT. THIS FEATURE IS SUPPORTED BY SMART LOGGER AND SCOPED LOGGER
            // The instance tag will provide an uid which will be added to all the messages for this logger. They can be useful if, for example, we create
            // multiple instances of those fragment and we want to know which instance is the one that logged a particular message

            logger.Info("We now entered the fragment and we have a new logger constructed using the one passed from the activity");
            logger.Debug("As you can see, the message now adds both classes to the trace");

            // logging messages in a different method, will automatically change the method tag
            logMessageInDifferentMethod();

            // Logging a message inside runnable will blow you away. It will correctly identify
            // the fact that it's inside a runnable as well as providing you where this runnable was
            // triggered: CLASS$METHOD()
            logMessageInsideRunnable();

            // Now this is even better. You can actually use any anonymous class and it still works
            logMessageInsideAnyAnonymousClass(new OnMyExampleEventListener() {
                @Override
                public void onFirstEventHappened() {
                    logger.Info("First Event Happened in my anonymous class");
                }

                @Override
                public void onCrashAndBurn() {
                    try {
                        @SuppressWarnings("NumericOverflow")
                        int z = 10 / 0;
                    } catch (Exception exception) {
                        logger.Error(exception, "Did I mention you can log exceptions as well? Placeholders still work also: %s", "argument");
                        logger.Info("When you log exceptions, their message appears at the end after ' :: '");
                    }
                }
            });

            // We're not done yet. We can even pass those anonymous classes outside this fragment and it will all be fine
            ((MainActivity) getActivity()).calledFromFragment(new OnMyExampleEventListener() {
                @Override
                public void onFirstEventHappened() {

                }

                @Override
                public void onCrashAndBurn() {
                    // now we'll be very hardcore and create a runnable inside as well which will
                    // log the message
                    new Runnable() {
                        @Override
                        public void run() {
                            logger.Error("Called the crash and burn using the SmartLogger");
                        }
                    }.run();

                }
            });

            // The OH-NO case: you are already quite deep in the development and you cannot afford to create
            //                  a loggers trace; yet you really want to see the trace for a particular method
            ((MainActivity) getActivity()).calledFromFragmentWithLazyLogger(new OnMyExampleEventListener() {
                @Override
                public void onFirstEventHappened() {

                }

                @Override
                public void onCrashAndBurn() {
                    new Runnable() {
                        @Override
                        public void run() {
                            // IMPORTANT: After testing the performance, it was noticed that the lazy logger
                            //              has less of an impact than I initially expected and is safe to
                            //              be used extensively (one log message would still take less then
                            //              half a millisecond, which is only 3-4 times slower than the standard
                            //              Android logging)
                            LazyLogger.Error("Called the crash and burn using the static LazyLogger");
                        }
                    }.run();

                }
            });

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    private void logMessageInDifferentMethod() {
        logger.Warn("As you can see, the SmartLogger automatically changes the tag for the method!");
    }

    private void logMessageInsideRunnable() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // The tags explained:
                // [Class MainActivity]                 -> where the initial logger was created
                // [Class BlankFragment]                -> where the logger used for the current message is created
                // [Method run                          -> the runnable method
                // BlankFragment#                       -> the class which ran the runnable
                // logMessageInsideRunnable()       -> the method which ran the runnable
                logger.Error("I am inside a runnable now! Check the comment above for explanation");
            }
        };

        runnable.run();
    }

    private void logMessageInsideAnyAnonymousClass(OnMyExampleEventListener anonClass) {
        anonClass.onFirstEventHappened();
        anonClass.onCrashAndBurn();
    }

}
