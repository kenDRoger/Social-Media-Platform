/**
 * mema nalang mang kopya
 * @author ken
 */
package com.mycompany.webapplicationdb.model;

import java.sql.SQLException;
import java.util.HashMap;

import com.mycompany.webapplicationdb.exception.DatabaseOperationException;

public class PostsList extends HashMap<String, Posts> {

    public PostsList() throws DatabaseOperationException {
        try ( JDBCModel jdbcModel = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);) {
            for (Posts posts : jdbcModel.getPosts()) {
                this.put(posts.getUsername(), posts);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Unable to get Posts table", e);
        }

    }

    public Posts getPostsByUsername(String username) {
        return this.get(username);
    }

    //method to check if hashmap contains all null
    public boolean containsAllNull() {
        boolean b = true;
        for (Posts posts : this.values()) {
            if (posts != null) {
                b = false;
            }
        }
        return b;
    }

    public static void main(String[] args) {
        try {
            //tester
            PostsList postsList = new PostsList();
            System.out.println(postsList);
        } catch (DatabaseOperationException ex) {
            System.out.println("Failed");
        }
    }
}
