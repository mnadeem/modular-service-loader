package com.prokarma.app.mem;

import com.prokarma.app.model.UserModel;

public class UserAdapter implements UserModel {

	private UserEntity userEntity;

	public UserAdapter(UserEntity userEntity) {
		this.userEntity = userEntity;		
	}

	public String getUsername() {
		return this.userEntity.getUserName();
	}

	public void setUsername(String username) {
		this.userEntity.setUserName(username);		
	}

	public boolean isEnabled() {
		return this.userEntity.isEnabled();
	}

	public void setEnabled(boolean enabled) {
		this.userEntity.setEnabled(enabled);		
	}

	public String getFirstName() {
		return this.userEntity.getFirstName();
	}

	public void setFirstName(String firstName) {
		this.userEntity.setFirstName(firstName);
	}

	public String getLastName() {
		return this.userEntity.getLastName();
	}

	public void setLastName(String lastName) {
		this.userEntity.setLastName(lastName);
	}

	public String getEmail() {
		return this.userEntity.getEmail();
	}

	public void setEmail(String email) {
		this.userEntity.setEmail(email);
	}

}
