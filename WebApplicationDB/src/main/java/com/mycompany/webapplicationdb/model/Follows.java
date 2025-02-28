package com.mycompany.webapplicationdb.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import com.mycompany.webapplicationdb.exception.AlreadyFollowedException;
import com.mycompany.webapplicationdb.exception.DatabaseConnectionFailedException;
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
        for (String user : follows) {
            if (user == null) {
                b = false;
            }
        }
        return b;
    }

    // check if follows are empty
    public boolean ifFollowsEmpty() {
        boolean b = true;
        for (String user : follows) {
            if (user != null) {
                b = false;
            }
        }
        return b;
    }

    // TODO: method to remove a follower
    public void removeFollow(String username) throws DatabaseConnectionFailedException, NoUserFoundException {

        // remove in object
        boolean found = false;
        for (int i = 0; i < follows.length; i++) {
            if (follows[i] != null && follows[i].equals(username)) {
                found = true;
                follows[i] = null;
                break;
            }
            
        }
        if(!found){
            throw new NoUserFoundException();
        }

        // identify which column contains the username
        String column = "";
        JDBCModel model = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        String query = "SELECT follow1,follow2,follow3 FROM follows WHERE username = ?";
        try (PreparedStatement stmt = model.getConnection().prepareStatement(query)) {
            stmt.setString(1, this.username);
            // tester
            System.out.println("query-identify:" + stmt);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                for (int i = 1; i <= 3; i++) {
                    String user = rs.getString("follow" + (i));
                    if (user == null) {
                        continue;
                    }
                    if (user.equals(username)) {
                        column = "follow" + i;
                        break;
                    }
                    // no username found in follows
                    column = null;
                }

            }
        } catch (SQLException e) {
            // show error message
            System.out.println("Error-removeFollow: " + e.getMessage() + " exception:" + e.getClass());
            throw new DatabaseConnectionFailedException();
        }

        // remove in database
        if (column == null) {
            // tester
            System.out.println("No user found=-removeFollow");
            throw new NoUserFoundException();
        }
        JDBCModel model1 = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        String query1 = "update follows set " + column + " = null where username = ?";
        try (PreparedStatement stmt = model1.getConnection().prepareStatement(query1)) {
            stmt.setString(1, this.username);
            // tester
            System.out.println("query-removeFollowdatabase:" + stmt);
            stmt.executeUpdate();
        } catch (SQLException e) {
            // show error message
            System.out.println("Error-removeFollowdatabase: " + e.getMessage() + " exception:" + e.getClass());
            throw new DatabaseConnectionFailedException();
        }

    }

    // TODO: method to add a follower
    public void addFollow(String username) throws DatabaseConnectionFailedException, FullFollowsException,
            NoUserFoundException, AlreadyFollowedException {
        // identify if follows are full
        if (ifFollowsFull()) {
            // tester
            System.out.println("Follows are full=-addFollow");
            throw new FullFollowsException();
        }

        //identify if already followed
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
        JDBCModel model = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        String query = "SELECT follow1,follow2,follow3 FROM follows WHERE username = ?";
        try (PreparedStatement stmt = model.getConnection().prepareStatement(query)) {
            stmt.setString(1, this.username);
            // tester
            System.out.println("query-identify:" + stmt);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                for (int i = 1; i <= 3; i++) {
                    Object user = rs.getObject("follow" + (i));
                    // tester
                    System.out.println("x\nx\nx:" + user);
                    if (user == null) {
                        column = "follow" + i;
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            // show error message
            System.out.println("Error-removeFollow: " + e.getMessage() + " exception:" + e.getClass());
            throw new DatabaseConnectionFailedException();
        }
        if (column == null) {
            // tester
            System.out.println("No column with null found");
            throw new FullFollowsException();
        }

        // add in database
        JDBCModel model1 = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        String query1 = "update follows set "+column+"= ? where username = ?";
        try (PreparedStatement stmt = model1.getConnection().prepareStatement(query1)) {
            stmt.setString(1, username);
            stmt.setString(2, this.username);
            // tester
            System.out.println("query-addFollowdatabase:" + stmt);
            stmt.executeUpdate();

        } catch (SQLException e) {
            // show error message
            System.out.println("Error-addFollowdatabase: " + e.getMessage() + " exception:" + e.getClass());
            throw new DatabaseConnectionFailedException();
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
