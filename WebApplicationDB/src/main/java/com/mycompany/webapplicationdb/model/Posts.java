package com.mycompany.webapplicationdb.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import com.mycompany.webapplicationdb.exception.DatabaseConnectionFailedException;

public class Posts {
    private String username;
    private PostData[] posts = new PostData[5];

    public Posts(String username, PostData post1, PostData post2, PostData post3, PostData post4, PostData post5)
            throws DatabaseConnectionFailedException {
        this.username = username;
        posts[0] = post1;
        posts[1] = post2;
        posts[2] = post3;
        posts[3] = post4;
        posts[4] = post5;

        System.out.println("Successful");
    }

    // method to get the highest id in the post table database
    public int getLatestID() throws DatabaseConnectionFailedException {
        // query in the database in table post for the highest id
        int highestID = 0;
        String query = "SELECT MAX(id) FROM post";
        JDBCModel model = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        try (Statement stmt = model.getConnection().createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                highestID = rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException();
        }
        return highestID;
    }

    public String getColumnNameWithLowestID() throws DatabaseConnectionFailedException {
        JDBCModel model = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        // select post1-5 from posts where username matches
        String query1 = "SELECT post1, post2, post3, post4, post5 FROM posts WHERE username = ?";
        int lowestID = Integer.MAX_VALUE;
        int lowestIndex = -1;
        try (PreparedStatement stmt = model.getConnection().prepareStatement(query1)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // retrieve posts IDs and update local posts array

                for (int i = 1; i <= 5; i++) {
                    int id = rs.getInt("post" + (i));
                    if (id < lowestID) {
                        lowestID = id;
                        lowestIndex = i;
                    }
                }
            }
        } catch (SQLException e) {
            // show error message
            System.out.println("Error-getColumnnamewithlowestid: " + e.getMessage());
            throw new DatabaseConnectionFailedException();
        }
        System.out.println("Determineed lowest column name: post"+lowestIndex);
        return "post" + lowestIndex;
    }

    public int getLowestLocalID() {
        // get the least int in posts id
        int lowestID = Integer.MAX_VALUE;
        for (PostData post : posts) {
            if (post != null) {
                if(post.getId()<lowestID)
                    lowestID = post.getId();
                //tester
                System.out.println("ID:"+post.getId());
            }
        }
        System.out.println("Determined id:"+lowestID);
        
        return lowestID;
    }

    public void updatePostOrder() throws DatabaseConnectionFailedException { // Only called in addpost and deletepost
        // Sorts the posts array with the PostData having the largest id first, treating
        // nulls as smallest
        Arrays.sort(posts, (a, b) -> {
            if (a == null && b == null) {
                return 0;
            }
            if (a == null) {
                return -1;
            }
            if (b == null) {
                return 1;
            }
            if (a.getId() == b.getId()) {
                return Integer.compare(b.getId(), a.getId());
            }
            return Integer.compare(b.getId(), a.getId());
        });
    }

    public void addPost(String title, String content, Timestamp date_created) throws DatabaseConnectionFailedException {
        updatePostOrder();
        int newID = getLatestID() + 1;

        String lowestColumn = getColumnNameWithLowestID();

        // add post in post table with the new id and the parameters, title, content,
        // date_created
        JDBCModel model2 = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        String query3 = "INSERT INTO post (title, content, date_created) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = model2.getConnection().prepareStatement(query3);) {
            stmt.setString(1, title);
            stmt.setString(2, content);
            stmt.setTimestamp(3, date_created);
            System.out.println("queryquery3: "+stmt);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error-query3: " + e.getMessage());
            throw new DatabaseConnectionFailedException();
        }

        // update post5(id) in post table where username = this.username in database(id
        // is autoincremented)
        JDBCModel model1 = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        String query2 = "UPDATE posts SET " + lowestColumn + " = ? WHERE username = ?";
        try (PreparedStatement stmt = model1.getConnection().prepareStatement(query2);) {
            stmt.setInt(1, newID);
            stmt.setString(2, username);
            System.out.println("queryquery2: "+stmt);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error-query2: " + e.getMessage());
            throw new DatabaseConnectionFailedException();
        }

        // delete the dereferenced post(with an id of getLowestLocalID)
        int lowestID = getLowestLocalID();
        deleteFromPostWhereID(lowestID);

        // update the posts array(replace the lowestid with the newest id)
        int lowestIndex = getIndexOfLowestLocalID();
        posts[lowestIndex] = new PostData(title, content, date_created);
        posts[lowestIndex].setId(newID);

        updatePostOrder();
    }

    public void deleteFromPostWhereID(int id) throws DatabaseConnectionFailedException {
        JDBCModel model3 = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        String query4 = "DELETE FROM post WHERE id = ?";
        try (PreparedStatement stmt = model3.getConnection().prepareStatement(query4);) {
            stmt.setInt(1, id);
            System.out.println("Queryquery4: " + stmt);
            stmt.executeUpdate();
        } catch (SQLException e) {

            System.out.println("Error-query4: " + e.getMessage());
            throw new DatabaseConnectionFailedException();
        }
        // remove post with the given id from the posts array
        for (int i = 0; i < posts.length; i++) {
            if (posts[i] != null && posts[i].getId() == id) {
                posts[i] = null;
                break;
            }
        }
    }

    private int getIndexOfLowestLocalID() {
        // get the index of the lowest id in posts array
        int lowestIndex = -1;
        int lowestID = Integer.MAX_VALUE;
        for (int i = 0; i < posts.length; i++) {
            if (posts[i] != null && posts[i].getId() < lowestID) {
                lowestID = posts[i].getId();
                lowestIndex = i;
            }
        }
        return lowestIndex;
    }

    public void updatePost(PostData post) throws DatabaseConnectionFailedException {
        updatePostOrder();

        //update databasee
        JDBCModel model = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        String query = "UPDATE post SET title = ?, content = ? where id = ?";
        try (PreparedStatement stmt = model.getConnection().prepareStatement(query);) {
            stmt.setString(1, post.getTitle());
            stmt.setString(2, post.getContent());
            stmt.setInt(3, post.getId());

            System.out.println("Query: " + stmt);
            stmt.executeUpdate();
        } catch (SQLException e) {

            System.out.println("Error-updatepost: " + e.getMessage());
            throw new DatabaseConnectionFailedException();
        }
        //update postdataobject here
        for (int i = 0; i < posts.length; i++) {
            if (posts[i] != null && posts[i].getId() == post.getId()) {
                posts[i] = post;
                break;
            }
        }
        updatePostOrder();
    }

    public void deletePost(PostData post) throws DatabaseConnectionFailedException {
        deleteFromPostWhereID(post.getId());
    }

    public static void main(String[] args) {

        try {
            JDBCModel jdbcModel = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
            ArrayList<Posts> allPosts = jdbcModel.getPosts();
            Posts guest1 = null;
            // Test getPosts method
            for (Posts posts : allPosts) {
                if (posts.getUsername().equals("guest1")) {
                    guest1 = posts;
                }
            }
            System.out.println(guest1);

            // Assuming addPost, updatePost, and deletePost methods are already correctly
            // implemented

            // Test addPost
            PostData newPost = new PostData("New Post", "This is a new post",
                    new Timestamp(System.currentTimeMillis()));
            guest1.addPost("New Post", "This is a new post", new Timestamp(System.currentTimeMillis()));
            System.out.println("After adding a new post:");
            System.out.println(guest1);
/* 
            // Test updatePost
            newPost = new PostData("Updated Post", "This is an updated post",
                    new Timestamp(System.currentTimeMillis()));
            allPosts.get(0).updatePost(newPost);
            System.out.println("After updating the post:");
            for (PostData post : guest1.getPosts()) {
                System.out.println(post);
            }

            // Test deletePost
            allPosts.get(0).deletePost(newPost);
            System.out.println("After deleting the post:");
            for (PostData post : guest1.getPosts()) {
                System.out.println(post);
            } */

        } catch (DatabaseConnectionFailedException ex) {
            System.out.println("Failed");
        }

    }

    @Override
    public String toString() {
        return "Posts{" + "username=" + username + ", post1=" + posts[0] + ", post2=" + posts[1] + ", post3=" + posts[2]
                + ", post4=" + posts[3] + ", post5=" + posts[4] + '}';
    }

    public PostData[] getPosts() {
        return posts;
    }

    public void setPosts(PostData[] posts) {
        this.posts = posts;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
