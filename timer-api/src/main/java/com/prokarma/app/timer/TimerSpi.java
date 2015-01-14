package com.prokarma.app.timer;

import com.prokarma.app.provider.Provider;
import com.prokarma.app.provider.ProviderFactory;
import com.prokarma.app.provider.ProviderSpi;

public class TimerSpi implements ProviderSpi {

    public String getName() {
        return "timer";
    }

    public Class<? extends Provider> getProviderClass() {
        return TimerProvider.class;
    }

    public Class<? extends ProviderFactory> getProviderFactoryClass() {
        return TimerProviderFactory.class;
    }
}
