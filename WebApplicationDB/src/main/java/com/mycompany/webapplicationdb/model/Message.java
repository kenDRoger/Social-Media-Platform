/**
 * mema nalang mang kopya
 * @author ken
 */
package com.mycompany.webapplicationdb.model;


import java.sql.Timestamp;

public class Message {
    private final String username;
    private final String subject;
    private final String content;
    private final Timestamp date_created;

    public Message(String username, String subject, String content, Timestamp date_created){
        this.username = username;
        this.subject = subject;
        this.content = content;
        this.date_created = date_created;
    }
    public Message(String username, String subject, String content){
        this.username = username;
        this.subject = subject;
        this.content = content;
        this.date_created = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "Message{" + "username=" + username + ", subject=" + subject + ", content=" + content + ", date_created=" + date_created + '}';
    }

    public Timestamp getDate_created() {
        return date_created;
    }

    public String getContent() {
        return content;
    }

    public String getSubject() {
        return subject;
    }

    public String getUsername() {
        return username;
    }
}
