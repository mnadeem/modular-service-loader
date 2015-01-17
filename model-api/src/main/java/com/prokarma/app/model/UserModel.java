package com.prokarma.app.model;

public interface UserModel {

    String getUsername();
    void setUsername(String username);
    boolean isEnabled();
    void setEnabled(boolean enabled); 
    String getFirstName();
    void setFirstName(String firstName);
    String getLastName();
    void setLastName(String lastName);
    String getEmail();
    void setEmail(String email);  
}
