package com.mycompany.webapplicationdb.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mycompany.webapplicationdb.exception.DatabaseOperationException;
import com.mycompany.webapplicationdb.exception.NoPostFoundException;

public class Posts {

    private String username;
    private PostData[] posts = new PostData[5];
    private static int newID;

    public boolean ifPostsNoNull() {
        boolean b = true;
        for (PostData post : posts) {
            if (post == null) {
                b = false;
            }
        }
        return b;
    }

    public boolean ifPostsNull() {
        boolean b = true;
        for (PostData post : posts) {
            if (post != null) {
                b = false;
            }
        }
        return b;
    }

    public Posts(String username, PostData post1, PostData post2, PostData post3, PostData post4, PostData post5)
            throws DatabaseOperationException {
        this.username = username;
        posts[0] = post1;
        posts[1] = post2;
        posts[2] = post3;
        posts[3] = post4;
        posts[4] = post5;
        newID = getLatestID();
    }

    // method to get the highest id in the post table database
    public int getLatestID() throws DatabaseOperationException {
        // query in the database in table post for the highest id
        int highestID = 0;
        String query = "SELECT max(id) from post";

        try (JDBCModel model = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
                Connection conn = model.getConnection();
                Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    highestID = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Unable to get latest ID", e);
        }

        if (newID > highestID) {
            return newID;
        }
        return highestID;
    }

    public String getColumnNameWithLowestID() throws DatabaseOperationException {

        // select post1-5 from posts where username matches
        String query1 = "SELECT post1, post2, post3, post4, post5 FROM posts WHERE username = ?";
        int lowestID = Integer.MAX_VALUE;
        int lowestIndex = -1;
        try (JDBCModel model = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
                PreparedStatement stmt = model.getConnection().prepareStatement(query1);) {
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
            throw new DatabaseOperationException("Unable to get ColumnNameWithLowestID", e);
        }

        return "post" + lowestIndex;
    }

    public Integer getLowestLocalID() {
        // get the least int in posts id
        int lowestID = Integer.MAX_VALUE;
        for (PostData post : posts) {
            if (post != null) {
                if (post.getId() < lowestID) {
                    lowestID = post.getId();
                }
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

    public void updatePostOrder() throws DatabaseOperationException { // Only called in addpost and deletepost
        // Sorts the posts array with the PostData having the largest id first, treating
        // nulls as smallest
        Arrays.sort(posts, Comparator.nullsLast(
                Comparator.comparing(PostData::getId, Comparator.nullsFirst(Comparator.naturalOrder())).reversed()));
    }

    public void addPost(String title, String content) throws DatabaseOperationException {
        updatePostOrder();

        // update newstid value

        // add post in post table with the new id and the parameters, title, content,
        // date_created
        /**
         * 1. "INSERT INTO post (title, content, date_created, username) VALUES (?, ?,
         * ?, ?)"
         * 2. "UPDATE posts SET " + lowestColumn + " = ? WHERE username = ?"
         */

        String intoPostQuery = "INSERT INTO post (title, content, username) VALUES (?, ?, ?)";
        String updatePostsQuery = "UPDATE posts SET " + getColumnNameWithLowestID() + " = ? WHERE username = ?";

        try (JDBCModel model = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
                Connection conn = model.getConnection();) {
            conn.setAutoCommit(false);

            try (PreparedStatement intoPostStmt = conn.prepareStatement(intoPostQuery);
                    PreparedStatement updatePostStmt = conn.prepareStatement(updatePostsQuery);) {
                intoPostStmt.setString(1, title);
                intoPostStmt.setString(2, content);
                intoPostStmt.setString(3, username);
                // tester
                System.out.println("query1: " + intoPostStmt);
                intoPostStmt.executeUpdate();
                conn.commit();

                newID = getLatestID();

                updatePostStmt.setInt(1, newID);
                updatePostStmt.setString(2, username);
                // tester
                System.out.println("query2: " + updatePostStmt);
                updatePostStmt.executeUpdate();

            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Transaction failed! Rolling back...");
                throw new DatabaseOperationException("Unable to add post", e);
            }

            conn.commit();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Unable to add post", e);
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

    public void deleteFromPostWhereID(int id) throws DatabaseOperationException {
        // delete the post in post table

        String query4 = "DELETE FROM post WHERE id = ?";
        try (JDBCModel model3 = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
                Connection conn = model3.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = model3.getConnection().prepareStatement(query4)) {
                stmt.setInt(1, id);
                // tester
                System.out.println("Querywhereid: " + stmt);
                stmt.executeUpdate();
            } catch (SQLException e) {
                conn.rollback();
                throw new DatabaseOperationException("Unable to delete post id:" + id, e);
            }
            conn.commit();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Unable to delete post id:" + id, e);
        }
        // remove post with the given id from the posts array
        for (int i = 0; i < posts.length; i++) {
            if (posts[i] != null && posts[i].getId() == id) {
                posts[i] = null;
                break;
            }
        }
    }

    public void deleteFromPostsOfUser() throws DatabaseOperationException {
        String query4 = "DELETE FROM post WHERE username = ?";
        try (JDBCModel model3 = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
                Connection conn = model3.getConnection();) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = model3.getConnection().prepareStatement(query4);) {
                stmt.setString(1, username);
                // tester
                System.out.println("Queryofuser: " + stmt);
                stmt.executeUpdate();
            } catch (SQLException e) {
                conn.rollback();
                throw new DatabaseOperationException("Unable to delete post of user", e);
            }
            conn.commit();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Unable to delete post of user", e);
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

    public void updatePost(int id, PostData post) throws DatabaseOperationException {
        updatePostOrder();

        String query = "UPDATE post SET title = ?, content = ? where id = ?";
        try (JDBCModel model = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
                Connection conn = model.getConnection();) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = model.getConnection().prepareStatement(query);) {
                stmt.setString(1, post.getTitle());
                stmt.setString(2, post.getContent());
                stmt.setInt(3, id);
                // tester
                System.out.println("Query: " + stmt);
                stmt.executeUpdate();
            } catch (SQLException e) {
                conn.rollback();
                throw new DatabaseOperationException("Unable to update post", e);
            }
            conn.commit();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Unable to update post", e);
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

    public void deletePost(int id) throws DatabaseOperationException, NoPostFoundException {
        // delete the id of post in posts tablee
        // get the column name with value id

        String selectQuery = "SELECT post1, post2, post3, post4, post5 FROM posts WHERE username = ?";
        String updateQuery = "UPDATE posts SET %s = NULL WHERE username = ?";
        try (JDBCModel model = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
                Connection conn = model.getConnection();) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = model.getConnection().prepareStatement(selectQuery);) {
                stmt.setString(1, username);
                // tester
                System.out.println("Query: " + stmt);
                ResultSet rs = stmt.executeQuery();
                String column = null;
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
                if (column == null || column.equals("")) {
                    throw new NoPostFoundException(
                            "No post with id " + id + " found in posts table of user " + username);
                }
                try (PreparedStatement stmt2 = model.getConnection()
                        .prepareStatement(String.format(updateQuery, column));) {
                    stmt2.setString(1, username);
                    // tester
                    System.out.println("Query: " + stmt2);
                    stmt2.executeUpdate();
                }
            } catch (SQLException e) {
                conn.rollback();
                throw new DatabaseOperationException("Unable to delete post", e);
            }
            conn.commit();
        } catch (SQLException e) {
            throw new DatabaseOperationException("Unable to delete post", e);
        }

        deleteFromPostWhereID(id);
    }

    public static void main(String[] args) {
        // Tester
        try {
            try (JDBCModel jdbcModel = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE)) {
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
                if (user1 != null)
                    user1.addPost("New Post", "This is a new post");
                System.out.println("After adding a new post:");
                // tester
                System.out.println(
                        "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
                for (PostData d : user1.posts) {
                    System.out.println(d);
                }

                // Test updatePost
                PostData newPost = new PostData("Updated Post", "This is an updated post",
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
            }

        } catch (SQLException ex) {
            System.out.println("Failed");
        } catch (DatabaseOperationException ex) {
            Logger.getLogger(Posts.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoPostFoundException e) {
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
