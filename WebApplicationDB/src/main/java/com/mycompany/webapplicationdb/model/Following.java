package com.mycompany.webapplicationdb.model;

import java.util.ArrayList;

import com.mycompany.webapplicationdb.exception.DatabaseConnectionFailedException;

public class Following extends ArrayList<Follows> {
    public Following() throws DatabaseConnectionFailedException {
        JDBCModel jdbcModel = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        for (Follows follows : jdbcModel.getFollows()) {
            this.add(follows);
        }
    }
    
    public boolean ifUsernameExists(String username){
        for (Follows follows : this) {
            if (follows.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public Follows getFollowsByUsername(String username) {
        for (Follows follows : this) {
            if (follows.getUsername().equals(username)) {
                return follows;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            JDBCModel jdbcModel = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
            //tester
            System.out.println("Successful jdbcmodel");
            for (Follows posts : jdbcModel.getFollows()) {
                System.out.println(posts);
            }
        } catch (DatabaseConnectionFailedException ex) {
            System.out.println("Failed");
        }
    }
}
