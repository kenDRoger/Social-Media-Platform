package com.mycompany.webapplicationdb.model;

import java.sql.Timestamp;

public class PostData {
    private String title;// 30
    private String content;// 200char
    private int id;
    private Timestamp date_created;

    public PostData(String title, String content, Timestamp date_created) {
        this.title = title;
        this.content = content;
        this.date_created = date_created;
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

    @Override
    public String toString() {
        return "PostData{" + "title=" + title + ", content=" + content + ", id=" + id + ", date_created=" + date_created + '}';
    }

    
    // TODO: wait for email ni sir(idk if post can be modified)
}
