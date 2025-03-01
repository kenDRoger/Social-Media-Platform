package com.mycompany.webapplicationdb.model;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mycompany.webapplicationdb.exception.DatabaseOperationException;

public class Following extends ArrayList<Follows> {

    public Following() throws DatabaseOperationException {
        try ( JDBCModel jdbcModel = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);) {
            for (Follows follows : jdbcModel.getFollows()) {
                this.add(follows);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Unable to get follow table", e);
        }

    }

    public boolean ifUsernameExists(String username) {
        for (Follows follows : this) {
            if (follows.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public Follows getFollowsByUsername(String username) {
        for (Follows follows : this) {
            if (follows.getUsername().equals(username)) {
                return follows;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        try {
            //tester
            try (JDBCModel jdbcModel = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE)) {
                //tester
                System.out.println("Successful jdbcmodel");
                for (Follows posts : jdbcModel.getFollows()) {
                    System.out.println(posts);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Failed");
        } catch (DatabaseOperationException ex) {
            Logger.getLogger(Following.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
