package com.prokarma.app.jpa;

import com.prokarma.app.jpa.entity.UserEntity;
import com.prokarma.app.model.UserModel;


public class UserAdapter implements UserModel {

    private UserEntity user;
    
    public UserAdapter() {
		this.user = new UserEntity();
	}

    public UserAdapter(UserEntity user) {
        this.user = user;
    }

    public UserEntity getUser() {
        return user;
    }

    public String getUsername() {
        return user.getUsername();
    }

    public void setUsername(String username) {
        user.setUsername(username);
    }

    public boolean isEnabled() {
        return user.isEnabled();
    }

    public void setEnabled(boolean enabled) {
        user.setEnabled(enabled);
    }

  
    public String getFirstName() {
        return user.getFirstName();
    }

    public void setFirstName(String firstName) {
        user.setFirstName(firstName);
    }

    public String getLastName() {
        return user.getLastName();
    }

    public void setLastName(String lastName) {
        user.setLastName(lastName);
    }

    public String getEmail() {
        return user.getEmail();
    }

    public void setEmail(String email) {
        user.setEmail(email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof UserModel)) return false;

        UserModel that = (UserModel) o;
        return that.getUsername().equals(getUsername());
    }

    @Override
    public int hashCode() {
        return getUsername().hashCode();
    }

}
