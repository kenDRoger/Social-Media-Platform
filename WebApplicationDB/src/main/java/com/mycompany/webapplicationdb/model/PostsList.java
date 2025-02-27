package com.mycompany.webapplicationdb.model;

import java.util.HashMap;

import com.mycompany.webapplicationdb.exception.DatabaseConnectionFailedException;

public class PostsList extends HashMap<String, Posts> {
    public PostsList() throws DatabaseConnectionFailedException {
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
            //tester
            PostsList postsList = new PostsList();
            System.out.println(postsList);
        } catch (DatabaseConnectionFailedException ex) {
            System.out.println("Failed");
        }
    }
}
