package com.prokarma.app.timer;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.prokarma.app.timer.TimerProvider;

public class BasicTimerProvider implements TimerProvider {

    private static final Logger logger = LoggerFactory.getLogger(BasicTimerProvider.class);

    private final Timer timer;
    private final BasicTimerProviderFactory factory;

    public BasicTimerProvider(Timer timer, BasicTimerProviderFactory factory) {
        this.timer = timer;
        this.factory = factory;
    }

    public void schedule(final Runnable runnable, final long interval, String taskName) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        };

        TimerTask existingTask = factory.putTask(taskName, task);
        if (existingTask != null) {
            logger.debug("Existing timer task '{}' found. Cancelling it", taskName);
            existingTask.cancel();
        }

        logger.debug("Starting task '{}' with interval '{}'", taskName, interval);
        timer.schedule(task, interval, interval);
    }

    public void cancelTask(String taskName) {
        TimerTask existingTask = factory.removeTask(taskName);
        if (existingTask != null) {
            logger.debug("Cancelling task '{}'", taskName);
            existingTask.cancel();
        }
    }

    public void close() {
        // do nothing
    }

}
