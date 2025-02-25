package com.mycompany.webapplicationdb.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mycompany.webapplicationdb.exception.DatabaseConnectionFailedException;

public class Accounts extends ArrayList<User>{


    public Accounts() throws DatabaseConnectionFailedException {
        JDBCModel jdbcModel = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        for(User user : jdbcModel.getAccounts()){
            this.add(user);
        }
    }

    public Map<String,String> getCredentials(){
        Map<String,String> credentials = new HashMap<>();
        for(User user : this){
            credentials.put(user.getUsername(), user.getPassword());
        }
        return credentials;
    }

    public List<User> getAccountsByRole(String... userRole){
        List<User> users = new ArrayList<>();
        for(User user : this){
            for(String role : userRole){
                if(user.getUserRole().equals(role)){
                    users.add(user);
                    break;
                }
            }
        }
        return users;
    }

    public void addUser(User user) throws DatabaseConnectionFailedException {
        JDBCModel jdbcModel = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        String query = "INSERT INTO account (username, password, user_role) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = jdbcModel.getConnection().prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getUserRole());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException();
        }
        add(user);
    }

    public List<User> getUsers() {
        return this;
    }

    public User getUser(String username){
        for(User user : this){
            if(user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }

    public User findUserByUsername(String username) {
        for (User user : this) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}