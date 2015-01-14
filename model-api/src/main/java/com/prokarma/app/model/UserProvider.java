package com.prokarma.app.model;

import com.prokarma.app.provider.Provider;

public interface UserProvider extends Provider {

    UserModel addUser(UserModel user);
    boolean removeUser(UserModel user);
    UserModel getUserByUsername(String username);   
    void close();
}