package com.prokarma.app.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="USER_ENTITY")
public class UserEntity {
    @Id
    @Column(name = "USERNAME")
    protected String username;
    @Column(name = "FIRST_NAME")
    protected String firstName;
    @Column(name = "LAST_NAME")
    protected String lastName;
    @Column(name = "EMAIL")
    protected String email;
    @Column(name = "ENABLED")
    protected boolean enabled;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
  
}
