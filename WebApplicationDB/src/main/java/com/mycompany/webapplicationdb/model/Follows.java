package com.mycompany.webapplicationdb.model;

import java.util.Arrays;

public class Follows {
    private String username;
    private String[] follows = new String[3];

    public Follows(String username, String follow1, String follow2, String follow3) {
        this.username = username;
        this.follows[0] = follow1;
        this.follows[1] = follow2;
        this.follows[2] = follow3;
    }

    public String getUsername() {
        return username;
    }

    public String[] getFollows() {
        return follows;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFollows(String[] follows) {
        this.follows = follows;
    }

    @Override
    public String toString() {
        return "Follows{" + "username=" + username + ", follows=" + Arrays.toString(follows) + '}';
    }
}
