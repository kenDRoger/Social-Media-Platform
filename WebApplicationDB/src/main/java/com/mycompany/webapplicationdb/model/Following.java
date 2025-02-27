package com.mycompany.webapplicationdb.model;

import java.util.HashMap;

import com.mycompany.webapplicationdb.exception.DatabaseConnectionFailedException;

public class Following extends HashMap<String, String[]> {
    public Following() throws DatabaseConnectionFailedException {
        JDBCModel jdbcModel = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        for (Follows follows : jdbcModel.getFollows()) {
            this.put(follows.getUsername(), follows.getFollows());
        }
    }

    public String[] getFollowsByUsername(String username) {
        return this.get(username);
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
