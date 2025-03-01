/**
 * mema nalang mang kopya
 * @author ken
 */
package com.mycompany.webapplicationdb.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import com.mycompany.webapplicationdb.exception.AlreadyFollowedException;
import com.mycompany.webapplicationdb.exception.DatabaseOperationException;
import com.mycompany.webapplicationdb.exception.FullFollowsException;
import com.mycompany.webapplicationdb.exception.NoUserFoundException;

public class Follows {
    private String username;
    private String[] follows = new String[3];

    public Follows(String username, String follow1, String follow2, String follow3) {
        this.username = username;
        this.follows[0] = follow1;
        this.follows[1] = follow2;
        this.follows[2] = follow3;
    }

    public boolean ifFollowsFull() {
        boolean b = true;
        for (String account : follows) {
            if (account == null) {
                b = false;
            }
        }
        return b;
    }

    // check if follows are empty
    public boolean ifFollowsEmpty() {
        boolean b = true;
        for (String account : follows) {
            if (account != null) {
                b = false;
            }
        }
        return b;
    }

    public void removeFollow(String username) throws DatabaseOperationException, NoUserFoundException {

        // remove in object
        boolean found = false;
        for (int i = 0; i < follows.length; i++) {
            if (follows[i] != null && follows[i].equals(username)) {
                found = true;
                follows[i] = null;
                break;
            }

        }
        if (!found) {
            throw new NoUserFoundException();
        }
        // identify which column contains the username
        String column = null;

        String selectQuery = "SELECT follow1, follow2, follow3 FROM follows WHERE username = ?";
        String updateQuery = "UPDATE follows SET %s = null WHERE username = ?";

        try (JDBCModel model = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
             Connection conn = model.getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            try (PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
                selectStmt.setString(1, this.username);
                System.out.println("query-identify:" + selectStmt);
                ResultSet rs = selectStmt.executeQuery();
                if (rs.next()) {
                    for (int i = 1; i <= 3; i++) {
                        String account = rs.getString("follow" + i);
                        if (account != null && account.equals(username)) {
                            column = "follow" + i;
                            break;
                        }
                    }
                }

                if (column == null) {
                    System.out.println("No user found=-removeFollow");
                    throw new NoUserFoundException();
                }

                try (PreparedStatement updateStmt = conn.prepareStatement(String.format(updateQuery, column))) {
                    updateStmt.setString(1, this.username);
                    System.out.println("query-removeFollowdatabase:" + updateStmt);
                    updateStmt.executeUpdate();
                }

            } catch (SQLException e) {
                try {
                    conn.rollback(); // Rollback transaction
                } catch (SQLException e2) {
                    System.out.println("Error-removeFollow-rollback: " + e2.getMessage() + " exception:" + e2.getClass());
                }
                System.out.println("Error-removeFollow: " + e.getMessage() + " exception:" + e.getClass());
                throw new DatabaseOperationException("Unable to remove follow", e);
            }

            conn.commit(); // Commit transaction

        } catch (SQLException e) {
            System.out.println("Error-removeFollow: " + e.getMessage() + " exception:" + e.getClass());
            throw new DatabaseOperationException("Unable to remove follow", e);
        }

    }

    public void addFollow(String username) throws DatabaseOperationException, FullFollowsException,
            NoUserFoundException, AlreadyFollowedException {
        // identify if follows are full
        if (ifFollowsFull()) {
            // tester
            System.out.println("Follows are full=-addFollow");
            throw new FullFollowsException();
        }

        // identify if already followed
        for (String follow : follows) {
            System.out.println("Comparing: " + follow + " with " + username);
            if (follow != null && follow.equals(username)) {
                throw new AlreadyFollowedException();
            }
        }
        // add in object
        for (int i = 0; i < follows.length; i++) {

            if (follows[i] == null) {
                follows[i] = username;
                break;
            }
        }

        // identify which column is null(empty) in the database
        String column = null;
        try (JDBCModel model = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
                Connection conn = model.getConnection()) {
            conn.setAutoCommit(false);
            String query = "SELECT follow1,follow2,follow3 FROM follows WHERE username = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, this.username);
                // tester
                System.out.println("query-identify:" + stmt);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    for (int i = 1; i <= 3; i++) {
                        Object account = rs.getObject("follow" + (i));
                        // tester
                        System.out.println("x\nx\nx:" + account);
                        if (account == null) {
                            column = "follow" + i;
                            break;
                        }
                    }
                }

                if (column == null) {
                    // tester
                    System.out.println("No column with null found");
                    throw new FullFollowsException();
                }

                // add in database
                String query1 = "update follows set " + column + "= ? where username = ?";
                try (PreparedStatement stmt1 = conn.prepareStatement(query1)) {
                    stmt1.setString(1, username);
                    stmt1.setString(2, this.username);
                    // tester
                    System.out.println("query-addFollowdatabase:" + stmt1);
                    stmt1.executeUpdate();
                    conn.commit();
                } catch (SQLException e) {
                    conn.rollback(); // Undo all changes if something goes wrong
                    System.out.println("Transaction failed! Rolling back...");
                    System.out.println("Error-addFollowdatabase: " + e.getMessage() + " exception:" + e.getClass());
                    throw new DatabaseOperationException("Unable to add follow", e);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error-addFollow: " + e.getMessage() + " exception:" + e.getClass());
            throw new DatabaseOperationException("Unable to add follow", e);
        }

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
