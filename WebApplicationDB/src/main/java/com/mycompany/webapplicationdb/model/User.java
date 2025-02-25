package com.mycompany.webapplicationdb.model;

public class User {
    private String username;
    private String password;
    private String userRole; // Renamed from user_role (Java naming convention)

    public User(String username, String password, String userRole) {
        this.username = username;
        this.password = password;
        this.userRole = userRole;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getUserRole() { return userRole; }
}