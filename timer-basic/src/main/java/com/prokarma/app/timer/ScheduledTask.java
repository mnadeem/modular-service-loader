package com.prokarma.app.timer;

import com.prokarma.app.provider.AppSession;

public interface ScheduledTask {
	public void run(AppSession session);
}
