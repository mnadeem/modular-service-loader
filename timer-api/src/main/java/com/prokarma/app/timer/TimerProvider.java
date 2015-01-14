package com.prokarma.app.timer;

import com.prokarma.app.provider.Provider;

public interface TimerProvider extends Provider {

    public void schedule(Runnable runnable, long interval, String taskName);

    public void cancelTask(String taskName);

}
