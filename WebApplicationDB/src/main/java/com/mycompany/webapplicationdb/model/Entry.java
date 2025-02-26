package com.mycompany.webapplicationdb.model;

import java.util.HashMap;

import com.mycompany.webapplicationdb.exception.DatabaseConnectionFailedException;

public class Entry extends HashMap<String, Posts> {
    public Entry() throws DatabaseConnectionFailedException {
        JDBCModel jdbcModel = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        for (Posts posts : jdbcModel.getPosts()) {
            this.put(posts.getUsername(), posts);
        }
    }

    public Posts getPostsByUsername(String username) {
        return this.get(username);
    }

    public static void main(String[] args) {
        try {
            JDBCModel jdbcModel = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
            //tester
            System.out.println("Successful jdbcmodel");
            for (Posts posts : jdbcModel.getPosts()) {
                System.out.println(posts);
            }
        } catch (DatabaseConnectionFailedException ex) {
            System.out.println("Failed");
        }
    }
}
