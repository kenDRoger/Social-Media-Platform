package com.mycompany.webapplicationdb.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mycompany.webapplicationdb.exception.DatabaseConnectionFailedException;

public class Accounts extends ArrayList<Account> {

    public Accounts() throws DatabaseConnectionFailedException {
        JDBCModel jdbcModel = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        for (Account user : jdbcModel.getAccounts()) {
            this.add(user);
        }
    }

    public Map<String, String> getCredentials() {
        Map<String, String> credentials = new HashMap<>();
        for (Account user : this) {
            credentials.put(user.getUsername(), user.getPassword());
        }
        return credentials;
    }

    public ArrayList<Account> getAccountsByRole(String... userRole) {
        ArrayList<Account> users = new ArrayList<>();
        for (Account user : this) {
            for (String role : userRole) {
                if (user.getUserRole().equals(role)) {
                    users.add(user);
                    break;
                }
            }
        }
        return users;
    }

    public void addUser(Account user) throws DatabaseConnectionFailedException {
        JDBCModel jdbcModel = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        String query = "INSERT INTO account (username, password, user_role) VALUES (?, ?, ?)";
        try ( PreparedStatement stmt = jdbcModel.getConnection().prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getUserRole());
            //tester
            System.out.println("Query-intoaccount:" + stmt);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException();
        }
        if (user.getUserRole().equals("user")) {
            JDBCModel model1 = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
            String query1 = "INSERT INTO posts (username) VALUES (?)";
            try ( PreparedStatement stmt = model1.getConnection().prepareStatement(query1)) {
                stmt.setString(1, user.getUsername());

                //tester
                System.out.println("Query-intoposts:" + stmt);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DatabaseConnectionFailedException();
            }
            JDBCModel model2 = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
            String query2 = "INSERT INTO follows (username) VALUES (?)";
            try ( PreparedStatement stmt = model2.getConnection().prepareStatement(query2)) {
                stmt.setString(1, user.getUsername());

                //tester
                System.out.println("Query-intofollows:" + stmt);
                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new DatabaseConnectionFailedException();
            }
        }
        add(user);
    }
    
    public void deleteUser(Account user) throws DatabaseConnectionFailedException{
        
    }

    public ArrayList<Account> getUsers() {
        return this;
    }

    public Account getUser(String username) {
        for (Account user : this) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public Account findUserByUsername(String username) {
        for (Account user : this) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
