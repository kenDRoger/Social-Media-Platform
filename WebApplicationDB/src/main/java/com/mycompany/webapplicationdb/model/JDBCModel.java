package com.mycompany.webapplicationdb.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mycompany.webapplicationdb.exception.DatabaseConnectionFailedException;
import static com.mycompany.webapplicationdb.model.MySQLCredentials.DEFAULT_PASSWORD;
import static com.mycompany.webapplicationdb.model.MySQLCredentials.DEFAULT_PORT;
import static com.mycompany.webapplicationdb.model.MySQLCredentials.DEFAULT_SERVERHOST;
import static com.mycompany.webapplicationdb.model.MySQLCredentials.DEFAULT_USERNAME;

public class JDBCModel {

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
            throw new DatabaseConnectionFailedException();
        }
    }


    // Method to get the list of accounts from Connection
    public Accounts getAccounts() throws DatabaseConnectionFailedException {
        conn = renewConnection();
        Accounts accounts = new Accounts();
        String query = "SELECT * FROM account";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String u = rs.getString("username");
                String p = rs.getString("password");
                String role = rs.getString("user_role");
                accounts.add(new User(u, p, role));
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionFailedException();
        }
        return accounts;
    }

    // method to get Connection
    public Connection getConnection(){
        return conn;
    }


    public static void main(String[] args) {
        try {
            new JDBCModel("mp2");
        } catch (DatabaseConnectionFailedException ex) {
        }
    }
}
