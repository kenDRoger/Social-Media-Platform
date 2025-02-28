package com.mycompany.webapplicationdb.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mycompany.webapplicationdb.exception.DatabaseConnectionFailedException;

public class Messages extends ArrayList<Message> {
    public Messages() throws DatabaseConnectionFailedException{
        JDBCModel jdbcModel = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        for (Message message : jdbcModel.getMessages()) {
            this.add(message);
        }
    }
    public Message getMessageByUsername(String username){
        for(Message message : this){
            if(message.getUsername().equals(username)){
                return message;
            }
        }
        return null;
    }   

    public void addMessage(String username, String subject, String content) throws DatabaseConnectionFailedException{
        
        Message newMessage = new Message(username, subject, content);
        
        JDBCModel jdbcModel = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
        String query3 = "INSERT INTO messages (username, subject, content) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = jdbcModel.getConnection().prepareStatement(query3);) {
            stmt.setString(1, newMessage.getUsername());
            stmt.setString(2, newMessage.getSubject());
            stmt.setString(3, newMessage.getContent());

            //tester
            System.out.println("Query-addMessage:"+stmt);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error-addMessage: " + e.getMessage() + " exception:" + e.getClass());
            throw new DatabaseConnectionFailedException();
        }
        this.add(newMessage);
    }

    public static void main(String[] args) throws DatabaseConnectionFailedException {
        Messages messages = new Messages();
        messages.addMessage("username", "subject", "content");
        for (Message message : messages) {
            System.out.println(message);
        }
    }
    
}
