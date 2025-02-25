package com.mycompany.webapplicationdb.model;
import java.sql.Date;
public class post {
    private String title;//30
    private String content;//200char
    private int id;
    private Date date_created;

    public post(String title, String content, int id, Date date_created) {
        this.title = title;
        this.content = content;
        this.id = id;
        this.date_created = date_created;
    }

    public String getContent() {
        return content;
    }

    public Date getDate_created() {
        return date_created;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
    //TODO: wait for email ni sir(idk if post can be modified)
}
