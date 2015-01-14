package com.prokarma.app.mem;

import java.util.concurrent.ConcurrentHashMap;

import com.prokarma.app.model.UserProvider;
import com.prokarma.app.model.UserProviderFactory;
import com.prokarma.app.provider.config.Config.Scope;

public class MemUserProviderFactory implements UserProviderFactory {
	
	public static final String ID = "mem";
	private ConcurrentHashMap<String, UserEntity> userSessions = new ConcurrentHashMap<String, UserEntity>();

	public UserProvider create() {
		return new MemUserProvider(userSessions);
	}

	public void init(Scope config) {
		// TODO Auto-generated method stub		
	}

	public void close() {
		userSessions.clear();		
	}

	public String getId() {
		return ID;
	}
}
