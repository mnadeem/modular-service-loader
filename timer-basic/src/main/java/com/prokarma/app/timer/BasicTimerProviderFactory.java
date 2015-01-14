package com.prokarma.app.timer;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.prokarma.app.provider.AppSession;
import com.prokarma.app.provider.config.Config;
import com.prokarma.app.timer.TimerProvider;
import com.prokarma.app.timer.TimerProviderFactory;


public class BasicTimerProviderFactory implements TimerProviderFactory {

    private Timer timer;

    private ConcurrentMap<String, TimerTask> scheduledTasks = new ConcurrentHashMap<String, TimerTask>();

    public TimerProvider create(AppSession session) {
        return new BasicTimerProvider(timer, this);
    }

    public void init(Config.Scope config) {
        timer = new Timer();
    }

    public void close() {
        timer.cancel();
        timer = null;
    }

    public String getId() {
        return "basic";
    }

    protected TimerTask putTask(String taskName, TimerTask task) {
        return scheduledTasks.put(taskName, task);
    }

    protected TimerTask removeTask(String taskName) {
        return scheduledTasks.remove(taskName);
    }
}
