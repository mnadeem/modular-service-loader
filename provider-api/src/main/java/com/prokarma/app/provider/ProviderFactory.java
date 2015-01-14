package com.prokarma.app.provider;

import com.prokarma.app.provider.config.Config;

public interface ProviderFactory<T extends Provider> {

	public T create();
	public void init(Config.Scope config);
	public void close();
	public String getId();
}
