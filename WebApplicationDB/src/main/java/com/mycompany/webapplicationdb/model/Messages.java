/**
 * mema nalang mang kopya
 * @author ken
 */
package com.mycompany.webapplicationdb.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;

import com.mycompany.webapplicationdb.exception.DatabaseOperationException;

public class Messages extends ArrayList<Message> {
    public Messages() throws DatabaseOperationException {
        try (JDBCModel jdbcModel = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE)) {
            for (Message message : jdbcModel.getMessages()) {
                this.add(message);
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Unable to get Messages table", e);
        }

    }

    public Message getMessageByUsername(String username) {
        for (Message message : this) {
            if (message.getUsername().equals(username)) {
                return message;
            }
        }
        return null;
    }

    public void addMessage(String username, String subject, String content) throws DatabaseOperationException {
        Message newMessage = new Message(username, subject, content);

        String query3 = "INSERT INTO messages (username, subject, content) VALUES (?, ?, ?)";
        try (JDBCModel jdbcModel = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE);
                Connection conn = jdbcModel.getConnection()) {

            conn.setAutoCommit(false); // Start transaction

            try (PreparedStatement stmt = conn.prepareStatement(query3)) {
                stmt.setString(1, newMessage.getUsername());
                stmt.setString(2, newMessage.getSubject());
                stmt.setString(3, newMessage.getContent());

                // tester
                System.out.println("Query-addMessage:" + stmt);

                stmt.executeUpdate();
                conn.commit(); // Commit transaction
            } catch (SQLException e) {
                conn.rollback(); // Rollback transaction
                System.out.println("Error-addMessage: " + e.getMessage() + " exception:" + e.getClass());
                throw new DatabaseOperationException("Unable to add message", e);
            }
        } catch (SQLException e) {
            System.out.println("Error-connection: " + e.getMessage() + " exception:" + e.getClass());
            throw new DatabaseOperationException("Unable to establish connection", e);
        }
        this.add(newMessage);
    }

    public void deleteMessage(String subject) throws DatabaseOperationException {
        String query = "delete from messages where subject = ?";
        try (JDBCModel model = new JDBCModel(MySQLCredentials.DEFAULT_DATABASE); Connection conn = model.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, subject);
                stmt.executeUpdate();
            } catch (SQLException e) {
                conn.rollback();
                throw new DatabaseOperationException("Unable to delete message", e);
            }
            conn.commit();
        } catch (SQLException ex) {
            throw new DatabaseOperationException("Unable to establish connection", ex);
        }
    }

    public ArrayList<Message> get5LatestMessages() {
        ArrayList<Message> latestMessages = new ArrayList<>();
        int i = 0;
        for (Message message : this) {
            if (message == null) {
                continue;
            }
            if (i < 5) {
                latestMessages.add(message);
            } else {
                int oldestIndex = 0;
                for (int j = 1; j < 5; j++) {
                    if (latestMessages.get(j).getDate_created()
                            .after(latestMessages.get(oldestIndex).getDate_created())) {
                        oldestIndex = j;
                    }
                }
                if (message.getDate_created().after(latestMessages.get(oldestIndex).getDate_created())) {
                    latestMessages.set(oldestIndex, message);
                }
            }
            i++;
        }
        // Sort the messages array to have the latest message at index 0
        latestMessages.sort(Comparator.nullsLast(Comparator.comparing(Message::getDate_created).reversed()));
        return latestMessages;
    }

    public static void main(String[] args) throws DatabaseOperationException {
        Messages messages = new Messages();
        messages.addMessage("username", "subject", "content");
        for (Message message : messages) {
            System.out.println(message);
        }
    }

    @Override
    public String toString() {
        return "Messages{" + "messages=" + super.toString() + '}';
    }

}
