package com.mycompany.webapplicationdb.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

import com.mycompany.webapplicationdb.exception.DatabaseOperationException;
import static com.mycompany.webapplicationdb.model.MySQLCredentials.DEFAULT_PASSWORD;
import static com.mycompany.webapplicationdb.model.MySQLCredentials.DEFAULT_PORT;
import static com.mycompany.webapplicationdb.model.MySQLCredentials.DEFAULT_SERVERHOST;
import static com.mycompany.webapplicationdb.model.MySQLCredentials.DEFAULT_USERNAME;

public class JDBCModel implements AutoCloseable {

    private Connection conn;
    private final String jdbcUrl;
    private final String userName;
    private final String password;

    // Default dont change, if you want to change values for your database change in
    // MySQLCredentials
    public static final String SERVERHOST = "localhost";
    public static final String DATABASE = "mp2";
    public static final String PORT = "3306";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "1234";

    public JDBCModel(String database) throws SQLException {
        String serverHost = SERVERHOST.isEmpty() ? DEFAULT_SERVERHOST : SERVERHOST;
        String port = PORT.isEmpty() ? DEFAULT_PORT : PORT;
        String databaseName = database.isEmpty() ? DATABASE : database;
        userName = USERNAME.isEmpty() ? DEFAULT_USERNAME : USERNAME;
        password = PASSWORD.isEmpty() ? DEFAULT_PASSWORD : PASSWORD;
        jdbcUrl = "jdbc:mysql://" + serverHost + ":" + port + "/" + databaseName
                + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        conn = renewConnection();
    }

    // method to renew connection, always use this method in each method
    private Connection renewConnection() throws SQLException {
        try {
            if (conn != null) {
                conn.close();
            }
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection c = DriverManager.getConnection(jdbcUrl, userName, password);
            // tester
            System.out.println("Successful Databse Connection using: " + jdbcUrl);
            return c;
        } catch (ClassNotFoundException e) {
            System.out.println("Failed Databse Connection");
            if (e instanceof ClassNotFoundException) {
                System.out.println("Driver not found");
            }
        }
        return null;
    }

    // Method to get the list of accounts from Connection
    public ArrayList<Account> getAccounts() throws DatabaseOperationException {
        
        ArrayList<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM account";
        try ( Statement stmt = renewConnection().createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String u = rs.getString("username");
                String p = rs.getString("password");
                String role = rs.getString("user_role");
                accounts.add(new Account(u, p, role));
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Unable to fetch Account Table",e);
        }
        return accounts;
    }

    // method to get the list of Follows from connection
    public ArrayList<Follows> getFollows() throws DatabaseOperationException {
        ArrayList<Follows> following = new ArrayList<>();
        String query = "SELECT * from follows";
        try ( Statement stmt = renewConnection().createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String username = rs.getString("username");

                String follow1 = rs.getString("follow1");
                String follow2 = rs.getString("follow2");
                String follow3 = rs.getString("follow3");

                following.add(new Follows(username, follow1, follow2, follow3));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            throw new DatabaseOperationException("Unable to fetch Follows table",e);
        }
        return following;
    }

    // Method to get the list of accounts from Connection
    public ArrayList<Posts> getPosts() throws DatabaseOperationException { // for Entry
        ArrayList<Posts> entry = new ArrayList<>();
        HashMap<Integer, PostData> posts = getPost();
        String query = "SELECT * from posts";
        try ( Statement stmt = renewConnection().createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String username = rs.getString("username");

                int post1 = rs.getInt("post1");
                int post2 = rs.getInt("post2");
                int post3 = rs.getInt("post3");
                int post4 = rs.getInt("post4");
                int post5 = rs.getInt("post5");

                entry.add(new Posts(username, posts.get(post1), posts.get(post2), posts.get(post3), posts.get(post4),
                        posts.get(post5)));
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Unable to fetch Posts table",e);
        }

        return entry;
    }

    // method to get the list of post from Connection
    public HashMap<Integer, PostData> getPost() throws DatabaseOperationException {// helper method only for
        // getPosts
        HashMap<Integer, PostData> posts = new HashMap<>();
        String query = "SELECT * FROM post";
        try ( Statement stmt = renewConnection().createStatement();  ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String title = rs.getString("title");
                String content = rs.getString("content");
                Integer id = rs.getInt("id");
                Timestamp date_created = rs.getTimestamp("date_created");
                String username = rs.getString("username");
                posts.put(id, new PostData(title, content, date_created, username).setId(id));
            }

        } catch (SQLException e) {

            throw new DatabaseOperationException("Unable to fetch Post table",e);
        }
        return posts;
    }

    // method to get the list of messages from connection
    public ArrayList<Message> getMessages() throws DatabaseOperationException {
        ArrayList<Message> messages = new ArrayList<>();
        String query = "SELECT * FROM messages";
        try (Statement stmt = renewConnection().createStatement(); 
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String username = rs.getString("username");
                String subject = rs.getString("subject");
                String content = rs.getString("content");
                Timestamp time = rs.getTimestamp("date_created");
                messages.add(new Message(username, subject, content, time));
            }
        } catch (SQLException e) {
            System.err.println("Error-getMessages: " + e.getMessage());
            throw new DatabaseOperationException("Unable to fetch Messages Table", e);
        }
        return messages;
    }

    // method to get Connection
    public Connection getConnection() {
        return conn;
    }

    public static void main(String[] args) {
        
    }

    @Override
    public void close() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
            System.out.println("JDBCModel connection closed.");
        }
    }
}
