package com.mycompany.webapplicationdb.model;

import java.util.ArrayList;
import java.util.List;

public class Accounts extends ArrayList<User>{

    public Accounts() {
    }

    public void addUser(User user) {
        add(user);
    }

    public List<User> getUsers() {
        return this;
    }

    public User findUserByUsername(String username) {
        for (User user : this) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null; // TODO: User not found
    }
}