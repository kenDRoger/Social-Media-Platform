package com.mycompany.webapplicationdb.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import com.mycompany.webapplicationdb.exception.DatabaseConnectionFailedException;

public class Posts {
    private String username;
    private PostData[] posts = new PostData[5];
    private static int newID = getLatestID();

    public boolean ifPostsNoNull() {
        boolean b = true;
        for (PostData post : posts) {
            if (post == null) {
                b = false;
            }
        }
        return b;
    }
    
    public boolean ifPostsNull(){
        boolean b = true;
        for (PostData post : posts) {
            if (post != null) {
                b = false;
            }
        }
        return b;
    }

    public Posts(String username, PostData post1, PostData post2, PostData post3, PostData post4, PostData post5)
            throws DatabaseConnectionFailedException {
        this.username = username;
        posts[0] = post1;
        posts[1] = post2;
        posts[2] = post3;
        posts[3] = post4;
        posts[4] = post5;
    }

    // method to get the highest id in the post table database
    public static int getLatestID() {
        // query in the database in table post for the highest id
        int highestID = 0;
        String query = "SELECT max(id) from post";
        try {
            Statement stmt = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE).getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                highestID = rs.getInt(1);
            }
        } catch (SQLException | DatabaseConnectionFailedException e) {
            System.out.println("Error in getLatestID method");
        }

        if (newID > highestID) {
            return newID;
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
                    if (rs.getObject("post" + (i)) == null) {
                        return "post" + i;
                    }
                    Integer id = rs.getInt("post" + (i));
                    if (id < lowestID) {
                        lowestID = id;
                        lowestIndex = i;
                    }
                }
            }
        } catch (SQLException e) {
            // show error message
            System.out.println("Error-getColumnnamewithlowestid: " + e.getMessage() + " exception:" + e.getClass());
            throw new DatabaseConnectionFailedException();
        }
        return "post" + lowestIndex;
    }

    public Integer getLowestLocalID() {
        // get the least int in posts id
        int lowestID = Integer.MAX_VALUE;
        for (PostData post : posts) {
            if (post != null) {
                if (post.getId() < lowestID)
                    lowestID = post.getId();
                // tester
                System.out.println("ID:" + post.getId());
            } else {
                return null;
            }
        }
        // tester
        System.out.println("Determined id:" + lowestID);

        return lowestID;
    }

    public void updatePostOrder() throws DatabaseConnectionFailedException { // Only called in addpost and deletepost
        // Sorts the posts array with the PostData having the largest id first, treating
        // nulls as smallest
        Arrays.sort(posts, Comparator.nullsLast(
                Comparator.comparing(PostData::getId, Comparator.nullsFirst(Comparator.naturalOrder())).reversed()));
    }

    public void addPost(String title, String content) throws DatabaseConnectionFailedException {
        updatePostOrder();

        // update newstid value

        // add post in post table with the new id and the parameters, title, content,
        // date_created
        JDBCModel model2 = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        String query3 = "INSERT INTO post (title, content, date_created, username) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = model2.getConnection().prepareStatement(query3);) {
            stmt.setString(1, title);
            stmt.setString(2, content);
            stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            stmt.setString(4, username);
            // tester
            System.out.println("queryquery3: " + stmt);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error-query3: " + e.getMessage() + " exception:" + e.getClass());
            throw new DatabaseConnectionFailedException();
        }

        newID = getLatestID();
        String lowestColumn = getColumnNameWithLowestID();

        // update post5(id) in post table where username = this.username in database(id
        // is autoincremented)
        JDBCModel model1 = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        String query2 = "UPDATE posts SET " + lowestColumn + " = ? WHERE username = ?";
        try (PreparedStatement stmt = model1.getConnection().prepareStatement(query2);) {
            stmt.setInt(1, newID);
            stmt.setString(2, username);
            // tester
            System.out.println("queryquery2: " + stmt);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error-query2: " + e.getMessage() + " exception:" + e.getClass());
            throw new DatabaseConnectionFailedException();
        }

        // delete the dereferenced post(with an id of getLowestLocalID)
        Integer lowestID = getLowestLocalID();
        if (lowestID != null) {
            deleteFromPostWhereID(lowestID);
        }
        // update the posts array(replace the lowestid with the newest id)
        int lowestIndex = getIndexOfLowestLocalID();
        // tester
        System.out.println("index:" + lowestIndex);
        posts[lowestIndex] = new PostData(title, content, new Timestamp(System.currentTimeMillis()), username);
        posts[lowestIndex].setId(newID++);

        updatePostOrder();
    }

    public void deleteFromPostWhereID(int id) throws DatabaseConnectionFailedException {
        // delete the post in post table
        JDBCModel model3 = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        String query4 = "DELETE FROM post WHERE id = ?";
        try (PreparedStatement stmt = model3.getConnection().prepareStatement(query4);) {
            stmt.setInt(1, id);
            // tester
            System.out.println("Querywhereid: " + stmt);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error-whereid: " + e.getMessage() + " exception:" + e.getClass());
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

    public void deleteFromPostsOfUser() throws DatabaseConnectionFailedException {
        JDBCModel model3 = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        String query4 = "DELETE FROM post WHERE username = ?";
        try (PreparedStatement stmt = model3.getConnection().prepareStatement(query4);) {
            stmt.setString(1, username);
            // tester
            System.out.println("Queryofuser: " + stmt);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error-ofuser: " + e.getMessage() + " exception:" + e.getClass());
            throw new DatabaseConnectionFailedException();
        }
        // remove post with the given user from the posts array
        for (int i = 0; i < posts.length; i++) {
            if (posts[i] != null && posts[i].getUsername().equals(username)) {
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
            if (posts[i] == null) {
                lowestIndex = i;
                break;
            }
            if (posts[i] != null && posts[i].getId() < lowestID) {
                lowestID = posts[i].getId();
                lowestIndex = i;
            }
        }
        return lowestIndex;
    }

    public void updatePost(int id, PostData post) throws DatabaseConnectionFailedException {
        updatePostOrder();

        JDBCModel model = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        String query = "UPDATE post SET title = ?, content = ? where id = ?";
        try (PreparedStatement stmt = model.getConnection().prepareStatement(query);) {
            stmt.setString(1, post.getTitle());
            stmt.setString(2, post.getContent());
            stmt.setInt(3, id);
            // tester
            System.out.println("Query: " + stmt);
            stmt.executeUpdate();
        } catch (SQLException e) {
            // tester
            System.out.println("Error-updatepost: " + e.getMessage() + " exception:" + e.getClass());
            throw new DatabaseConnectionFailedException();
        }

        // update postdataobject here
        for (int i = 0; i < posts.length; i++) {
            if (posts[i] != null && posts[i].getId() == id) {
                posts[i] = post;
                break;
            }
        }
        updatePostOrder();
    }

    public void deletePost() throws DatabaseConnectionFailedException {

    }

    public void deletePost(int id) throws DatabaseConnectionFailedException {
        // delete the id of post in posts tablee
        // get the column name with value id
        JDBCModel model3 = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        String query1 = "SELECT post1, post2, post3, post4, post5 FROM posts WHERE username = ?";

        String column = "";
        try (PreparedStatement stmt = model3.getConnection().prepareStatement(query1)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt("post1") == id) {
                    column = "post1";
                } else if (rs.getInt("post2") == id) {
                    column = "post2";
                } else if (rs.getInt("post3") == id) {
                    column = "post3";
                } else if (rs.getInt("post4") == id) {
                    column = "post4";
                } else if (rs.getInt("post5") == id) {
                    column = "post5";
                }
            }
        } catch (SQLException e) {
            System.out.println("Error-integermaxvalue: " + e.getMessage() + " exception:" + e.getClass());
            throw new DatabaseConnectionFailedException();
        }

        JDBCModel model2 = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        String query3 = "UPDATE posts SET " + column + " = NULL WHERE username = ?";
        try (PreparedStatement stmt = model2.getConnection().prepareStatement(query3);) {
            stmt.setString(1, username);
            // tester
            System.out.println("Queryset0: " + stmt);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error-set0: " + e.getMessage() + " exception:" + e.getClass());
            throw new DatabaseConnectionFailedException();
        }

        deleteFromPostWhereID(id);
    }

    public static void main(String[] args) {

        try {
            JDBCModel jdbcModel = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
            ArrayList<Posts> allPosts = jdbcModel.getPosts();
            Posts user1 = null;
            // Test getPosts method
            for (Posts posts : allPosts) {
                if (posts.getUsername().equals("user1")) {
                    user1 = posts;
                }
            }
            System.out.println(user1);
            // Assuming addPost, updatePost, and deletePost methods are already correctly
            // implemented

            // Test addPost
            PostData newPost = new PostData("New Post", "This is a new post",
                    new Timestamp(System.currentTimeMillis()), "user1");
            user1.addPost("New Post", "This is a new post");
            System.out.println("After adding a new post:");
            // tester
            System.out.println(
                    "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            for (PostData d : user1.posts) {
                System.out.println(d);
            }

            // Test updatePost
            newPost = new PostData("Updated Post", "This is an updated post",
                    new Timestamp(System.currentTimeMillis()), "user1").setId(16);// from form data(hidden value
                                                                                   // siguro) kapag nagmomodify
            user1.updatePost(newPost.getId(), newPost);
            System.out.println("After updating the post:");
            // tester
            System.out.println(
                    "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            for (PostData d : user1.posts) {
                System.out.println(d);
            }

            // Test deletePost
            user1.deletePost(newPost.getId());
            System.out.println("After deleting the post:");
            // tester
            System.out.println(
                    "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
            for (PostData d : user1.posts) {
                System.out.println(d);
            }

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
