/**
 * mema nalang mang kopya
 * @author ken
 */
package com.mycompany.webapplicationdb.model;

public class Account {
    private final String username;
    private final String password;
    private final String userRole; // Renamed from user_role (Java naming convention)

    public Account(String username, String password, String userRole) {
        this.username = username;
        this.password = password;
        this.userRole = userRole;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getUserRole() { return userRole; }
}