package com.prokarma.app.mem;

import java.util.concurrent.ConcurrentHashMap;

import com.prokarma.app.model.UserModel;
import com.prokarma.app.model.UserProvider;

public class MemUserProvider implements UserProvider {

	private ConcurrentHashMap<String, UserEntity> userSessions;

	public MemUserProvider(ConcurrentHashMap<String, UserEntity> userSessions) {
		this.userSessions = userSessions;
	}

	public UserModel addUser(UserModel user) {
		this.userSessions.put(user.getUsername(), newUserEntity(user));
		return user;
	}

	private UserEntity newUserEntity(UserModel user) {
		return new UserEntity();
	}

	public boolean removeUser(UserModel user) {
		this.userSessions.remove(user.getUsername());
		return true;
	}

	public UserModel getUserByUsername(String username) {
		return newUserModel(username);
	}

	private UserModel newUserModel(String username) {
		UserAdapter adapter = new UserAdapter(this.userSessions.get(username));
		return adapter;
	}

	public void close() {
		// TODO Auto-generated method stub		
	}
}
