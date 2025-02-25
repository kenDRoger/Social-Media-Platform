package com.mycompany.webapplicationdb.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.mycompany.webapplicationdb.exception.DatabaseConnectionFailedException;
import static com.mycompany.webapplicationdb.model.MySQLCredentials.DEFAULT_PASSWORD;
import static com.mycompany.webapplicationdb.model.MySQLCredentials.DEFAULT_PORT;
import static com.mycompany.webapplicationdb.model.MySQLCredentials.DEFAULT_SERVERHOST;
import static com.mycompany.webapplicationdb.model.MySQLCredentials.DEFAULT_USERNAME;

public class JDBCModel {

    private Connection conn;
    private String jdbcUrl;
    private String userName;
    private String password;

    // Default dont change, if you want to change values for your database change in
    // MySQLCredentials
    public static final String SERVERHOST = "localhost";
    public static final String DATABASE = "mp2";
    public static final String PORT = "3306";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "1234";

    public JDBCModel(String database) throws DatabaseConnectionFailedException {
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
    private Connection renewConnection() throws DatabaseConnectionFailedException {
        try {
            if (conn != null) {
                conn.close();
            }
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection c = DriverManager.getConnection(jdbcUrl, userName, password);
            // tester
            System.out.println("Successful Databse Connection using: " + jdbcUrl);
            return c;
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Failed Databse Connection");
            if (e instanceof ClassNotFoundException) {
                System.out.println("Driver not found");
            } if (e instanceof SQLException) {
                System.out.println("SQL Exception");
            } else {
                System.out.println("Unknown Exception");
            }
            // TODO: handle this exception(in web.xml then add an error page)
            throw new DatabaseConnectionFailedException();
        }
    }

    // Method to get a HashMap of Usernames and passwords from Connection
    public Map<String, String> getCredentials() throws DatabaseConnectionFailedException {
        conn = renewConnection();
        Map<String, String> credentials = new HashMap<>();
        String query = "SELECT username, password FROM account";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String u = rs.getString("username");
                String p = rs.getString("password");
                credentials.put(u, p);
            }
        } catch (SQLException e) {
            //TODO: handle query exception
        }

        return credentials;
    }

    // Method to get the user_role using username from Connection
    public String getUserRole(String username) throws DatabaseConnectionFailedException {
        conn = renewConnection();
        String role = "";
        String query = "SELECT user_role FROM account WHERE username = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    role = rs.getString("user_role");
                }
            }
        } catch (SQLException e) {
            //TODO: handle query exception
        }

        return role;
    }

    // Method to get the list of accounts based on user_role from Connection
    public Accounts getAccountsByRole(String... userRole) throws DatabaseConnectionFailedException {
        conn = renewConnection();
        Accounts accounts = new Accounts();
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM account WHERE user_role IN (");
        for (int i = 0; i < userRole.length; i++) {
            queryBuilder.append("?");
            if (i < userRole.length - 1) {
                queryBuilder.append(",");
            }
        }
        queryBuilder.append(")");
        String query = queryBuilder.toString();

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            for (int i = 0; i < userRole.length; i++) {
                pstmt.setString(i + 1, userRole[i]);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String u = rs.getString("username");
                    String p = rs.getString("password");
                    String role = rs.getString("user_role");
                    accounts.add(new User(u, p, role));
                }
            }
        } catch (SQLException e) {
            //TODO: handle query exception
        }

        return accounts;
    }

    public static void main(String[] args) {
        try {
            new JDBCModel("mp2");
        } catch (DatabaseConnectionFailedException ex) {
        }
    }
}
