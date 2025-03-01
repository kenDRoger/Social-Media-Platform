package com.mycompany.webapplicationdb.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mycompany.webapplicationdb.exception.DatabaseOperationException;

public class Accounts extends ArrayList<Account> {

    public Accounts() throws DatabaseOperationException {
        try ( JDBCModel jdbcModel = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);) {
            for (Account user : jdbcModel.getAccounts()) {
                this.add(user);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Unable to get Account table", e);
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

    public void addUser(Account user) throws DatabaseOperationException {
        String insertAccountQuery = "INSERT INTO account (username, password, user_role) VALUES (?, ?, ?)";
        String insertPostsQuery = "INSERT INTO posts (username) VALUES (?)";
        String insertFollowsQuery = "INSERT INTO follows (username) VALUES (?)";

        try ( JDBCModel jdbcModel = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);  Connection conn = jdbcModel.getConnection()) {// Use a single connection){

            // Start transaction
            conn.setAutoCommit(false);

            // Insert into account table
            try ( PreparedStatement stmtInsertAccount = conn.prepareStatement(insertAccountQuery);  PreparedStatement stmtInsertPosts = conn.prepareStatement(insertPostsQuery);  PreparedStatement stmtInsertFollows = conn.prepareStatement(insertFollowsQuery);) {

                stmtInsertAccount.setString(1, user.getUsername());
                stmtInsertAccount.setString(2, user.getPassword());
                stmtInsertAccount.setString(3, user.getUserRole());
                System.out.println("Query-into-account: " + stmtInsertAccount);
                stmtInsertAccount.executeUpdate();
                // If user role is "user", insert into posts and follows tables
                if ("user".equals(user.getUserRole())) {
                    try ( PreparedStatement stmt = conn.prepareStatement(insertPostsQuery)) {
                        stmt.setString(1, user.getUsername());
                        System.out.println("Query-into-posts: " + stmt);
                        stmt.executeUpdate();
                    }

                    try ( PreparedStatement stmt = conn.prepareStatement(insertFollowsQuery)) {
                        stmt.setString(1, user.getUsername());
                        System.out.println("Query-into-follows: " + stmt);
                        stmt.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                conn.rollback();  // Undo all changes if something goes wrong
                System.out.println("Transaction failed! Rolling back...");
                e.printStackTrace();
            }

            // Commit transaction
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Add user to the application (assuming `add(user)` is a method to track users in memory)
        add(user);
    }

    public void deleteUser(Account user) throws DatabaseOperationException {

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

    public Account findAccountByUsername(String username) {
        for (Account user : this) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Accounts{");
        for (Account user : this) {
            sb.append(user.getUsername()).append(",");
        }
        sb.setLength(sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }
    
}
