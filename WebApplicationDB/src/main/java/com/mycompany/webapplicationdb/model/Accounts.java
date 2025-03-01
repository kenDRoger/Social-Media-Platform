/**
 * mema nalang mang kopya
 * @author ken
 */
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
        try (JDBCModel jdbcModel = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);) {
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

    public void addAccount(Account account) throws DatabaseOperationException {
        String insertAccountQuery = "INSERT INTO account (username, password, user_role) VALUES (?, ?, ?)";
        String insertPostsQuery = "INSERT INTO posts (username) VALUES (?)";
        String insertFollowsQuery = "INSERT INTO follows (username) VALUES (?)";

        try (JDBCModel jdbcModel = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
                Connection conn = jdbcModel.getConnection()) {// Use a single connection){

            // Start transaction
            conn.setAutoCommit(false);

            // Insert into account table
            try (PreparedStatement stmtInsertAccount = conn.prepareStatement(insertAccountQuery);) {
                stmtInsertAccount.setString(1, account.getUsername());
                stmtInsertAccount.setString(2, account.getPassword());
                stmtInsertAccount.setString(3, account.getUserRole());
                System.out.println("Query-into-account: " + stmtInsertAccount);
                stmtInsertAccount.executeUpdate();
                // If user role is "user", insert into posts and follows tables
                if ("user".equals(account.getUserRole())) {
                    try (PreparedStatement stmt = conn.prepareStatement(insertPostsQuery)) {
                        stmt.setString(1, account.getUsername());
                        System.out.println("Query-into-posts: " + stmt);
                        stmt.executeUpdate();
                    }

                    try (PreparedStatement stmt = conn.prepareStatement(insertFollowsQuery)) {
                        stmt.setString(1, account.getUsername());
                        System.out.println("Query-into-follows: " + stmt);
                        stmt.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                conn.rollback(); // Undo all changes if something goes wrong
                System.out.println("Transaction failed! Rolling back...");
                throw new DatabaseOperationException("Unable to add account:" + account.getUsername(), e);
            }

            // Commit transaction
            conn.commit();

        } catch (SQLException e) {
            throw new DatabaseOperationException("Unable to add account:" + account.getUsername(), e);
        }

        // Add user to the application (assuming `add(user)` is a method to track users
        // in memory)
        add(account);
    }

    public void deleteBulkAccounts(Object[] usernames) throws DatabaseOperationException {
        // Convert Object[] to String[]
        String[] stringUsernames = new String[usernames.length];
        for (int i = 0; i < usernames.length; i++) {
            stringUsernames[i] = (String) usernames[i];
        }
        deleteBulkAccounts(stringUsernames);
    }

    public void deleteBulkAccounts(String[] usernames) throws DatabaseOperationException {
        for (String username : usernames) {
            if (username != null) {
                try {
                    Account user = this.stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
                    if (user != null) {
                        deleteAccount(user);
                    } else {
                        System.out.println("Account not found: " + username);
                    }
                } catch (DatabaseOperationException e) {
                    System.out.println("Failed to delete account: " + username);
                    throw new DatabaseOperationException("Failed to delete account: " + username, e);
                }
            }
        }
    }

    public void deleteAccount(Account account) throws DatabaseOperationException {
        String deleteMessagesQuery = "DELETE FROM messages WHERE username = ?";
        String deleteFollowsQuery = "DELETE FROM follows WHERE username = ?";
        String updateFollows = "update follows set follow1 = nullif(follow1, ?), follow2 = nullif(follow2, ?), follow3 = nullif(follow3, ?) where follow1 = ? or follow2 = ? or follow3 = ?";
        String deletePostsQuery = "DELETE FROM posts WHERE username = ?";
        String deletePostQuery = "DELETE FROM post WHERE username = ?";
        // only runs this query if userRole is admin
        String deleteAccountQuery = "DELETE FROM account WHERE username = ?";

        try (JDBCModel jdbcModel = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
                Connection conn = jdbcModel.getConnection()) {
            conn.setAutoCommit(false);
            if (account.getUserRole().equals("user")) {
                try (PreparedStatement stmt = conn.prepareStatement(deleteMessagesQuery);
                        PreparedStatement stmt2 = conn.prepareStatement(deleteFollowsQuery);
                        PreparedStatement stmt3 = conn.prepareStatement(updateFollows);
                        PreparedStatement stmt4 = conn.prepareStatement(deletePostsQuery);
                        PreparedStatement stmt5 = conn.prepareStatement(deletePostQuery)) {
                    stmt.setString(1, account.getUsername());
                    System.out.println("Query-delete-messages: " + stmt);
                    stmt.executeUpdate();

                    stmt2.setString(1, account.getUsername());
                    System.out.println("Query-delete-follows: " + stmt2);
                    stmt2.executeUpdate();

                    stmt3.setString(1, account.getUsername());
                    stmt3.setString(2, account.getUsername());
                    stmt3.setString(3, account.getUsername());
                    stmt3.setString(4, account.getUsername());
                    stmt3.setString(5, account.getUsername());
                    stmt3.setString(6, account.getUsername());
                    System.out.println("Query-update-follows: " + stmt3);
                    stmt3.executeUpdate();

                    stmt4.setString(1, account.getUsername());
                    System.out.println("Query-delete-posts: " + stmt4);
                    stmt4.executeUpdate();

                    stmt5.setString(1, account.getUsername());
                    System.out.println("Query-delete-post: " + stmt5);
                    stmt5.executeUpdate();
                } catch (SQLException e) {
                    conn.rollback(); // Undo all changes if something goes wrong
                    System.out.println("Transaction failed! Rolling back...");
                    throw new DatabaseOperationException("Unable to delete in other tables", e);
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(deleteAccountQuery)) {
                stmt.setString(1, account.getUsername());
                System.out.println("Query-delete-account: " + stmt);
                stmt.executeUpdate();
            } catch (SQLException e) {
                conn.rollback(); // Undo all changes if something goes wrong
                System.out.println("Transaction failed! Rolling back...");
                throw new DatabaseOperationException("Unable to delete in Account table:", e);
            }

            conn.commit();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Unable to get Account table", e);
        }

        // Remove user from the application (assuming `remove(user)` is a method to track users
        // in memory)
        remove(account);
    }

    public void updateAccount(String oldUsername,Account newAccount) throws DatabaseOperationException {
        //TODO: Test
        /**
         * 1. user to user
         * 2. user to admin
         * 3. admin to user
         */
        //! user to user
        if (!newAccount.getUserRole().equals("user") && this.getUser(oldUsername).getUserRole().equals("user")){
            deleteAccount(this.getUser(oldUsername));
            addAccount(newAccount);
            return;
        }
        //user to user
        String setForeignCheck0 = "SET FOREIGN_KEY_CHECKS = 0;";
        String updatePostQuery = "UPDATE post SET username = ? WHERE username = ?";
        String updatePostsQuery = "UPDATE posts SET username = ? WHERE username = ?";
        String updateFollowsQuery = "UPDATE follows SET username = ? WHERE username = ?";
        String updateFollows1Query = "UPDATE follows SET follow1 = ? WHERE follow1 = ?";
        String updateFollows2Query = "UPDATE follows SET follow2 = ? WHERE follow2 = ?";
        String updateFollows3Query = "UPDATE follows SET follow3 = ? WHERE follow3 = ?";
        String updateMessagesQuery = "UPDATE messages SET username = ? WHERE username = ?";

        String updateAccountQuery = "UPDATE account SET username = ?, password = ?,  user_role = ? WHERE username = ?";

        String setForeignCheck1 = "SET FOREIGN_KEY_CHECKS = 1;";

        try (JDBCModel jdbcModel = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
                Connection conn = jdbcModel.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(setForeignCheck0)) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                conn.rollback(); // Undo all changes if something goes wrong
                System.out.println("Transaction failed! Rolling back...");
                throw new DatabaseOperationException("Unable to check foreign key checks:", e);
            }
            conn.commit();
            try (PreparedStatement stmt = conn.prepareStatement(updatePostQuery);
                    PreparedStatement stmt2 = conn.prepareStatement(updatePostsQuery);
                    PreparedStatement stmt3 = conn.prepareStatement(updateFollowsQuery);
                    PreparedStatement stmt4 = conn.prepareStatement(updateFollows1Query);
                    PreparedStatement stmt5 = conn.prepareStatement(updateFollows2Query);
                    PreparedStatement stmt6 = conn.prepareStatement(updateFollows3Query);
                    PreparedStatement stmt7 = conn.prepareStatement(updateMessagesQuery)) {
                stmt.setString(1, newAccount.getUsername());
                stmt.setString(2, oldUsername);
                System.out.println("Query-update-post: " + stmt);
                stmt.executeUpdate();

                stmt2.setString(1, newAccount.getUsername());
                stmt2.setString(2, oldUsername);
                System.out.println("Query-update-posts: " + stmt2);
                stmt2.executeUpdate();

                stmt3.setString(1, newAccount.getUsername());
                stmt3.setString(2, oldUsername);
                System.out.println("Query-update-follows: " + stmt3);
                stmt3.executeUpdate();

                stmt4.setString(1, newAccount.getUsername());
                stmt4.setString(2, oldUsername);
                System.out.println("Query-update-follows1: " + stmt4);
                stmt4.executeUpdate();

                stmt5.setString(1, newAccount.getUsername());
                stmt5.setString(2, oldUsername);
                System.out.println("Query-update-follows2: " + stmt5);
                stmt5.executeUpdate();

                stmt6.setString(1, newAccount.getUsername());
                stmt6.setString(2, oldUsername);
                System.out.println("Query-update-follows3: " + stmt6);
                stmt6.executeUpdate();

                stmt7.setString(1, newAccount.getUsername());
                stmt7.setString(2, oldUsername);
                System.out.println("Query-update-messages: " + stmt7);
                stmt7.executeUpdate();
            } catch (SQLException e) {
                conn.rollback(); // Undo all changes if something goes wrong
                System.out.println("Transaction failed! Rolling back...");
                throw new DatabaseOperationException("Unable to update in other tables", e);
            }

            try (PreparedStatement stmt = conn.prepareStatement(updateAccountQuery)) {
                stmt.setString(1, newAccount.getUsername());
                stmt.setString(2, newAccount.getPassword());
                stmt.setString(3, newAccount.getUserRole());
                stmt.setString(4, oldUsername);
                System.out.println("Query-update-account: " + stmt);
                stmt.executeUpdate();
            } catch (SQLException e) {
                conn.rollback(); // Undo all changes if something goes wrong
                System.out.println("Transaction failed! Rolling back...");
                System.out.println("Error type:"+e.getClass()+" Message:"+e.getMessage());
                throw new DatabaseOperationException("Unable to update in Account table:", e);
            }

            try (PreparedStatement stmt = conn.prepareStatement(setForeignCheck1)) {
                stmt.executeUpdate();
            } catch (SQLException e) {
                conn.rollback(); // Undo all changes if something goes wrong
                System.out.println("Transaction failed! Rolling back...");
                throw new DatabaseOperationException("Unable to check foreign key checks:", e);
            }

            conn.commit();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Unable to get Account table", e);
        }

        // Update user in the application (assuming `update(user)` is a method to track users
        // in memory)
        remove(this.getUser(oldUsername));
        add(newAccount);
    }

    public boolean ifUpdatedAccount(Account newAccount, Account oldAccount) {
        if (newAccount.getUsername().equals(oldAccount.getUsername())) {
            return true;
        }
        if (newAccount.getPassword().equals(oldAccount.getPassword())) {
            return true;
        }
        if (!newAccount.getUserRole().equals(oldAccount.getUserRole())) {
        } else {
            return true;
        }        
        return false;
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
