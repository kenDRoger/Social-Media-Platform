package com.mycompany.webapplicationdb.model;

import java.sql.Timestamp;

public class PostData {
    private final String title;// 30
    private final String content;// 200char
    private int id;
    private final Timestamp date_created;
    private final String username;

    public PostData(String title, String content, Timestamp date_created, String username) {
        this.title = title;
        this.content = content;
        this.date_created = date_created;
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getDate_created() {
        return date_created;
    }

    public int getId() {
        return id;
    }

    public PostData setId(int id){
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getUsername(){
        return username;
    }

    @Override
    public String toString() {
        return "PostData{" + "title=" + title + ", content=" + content + ", id=" + id + ", date_created=" + date_created + '}';
    }

    
    // TODO: wait for email ni sir(idk if post can be modified)
}
