package com.tec9rushbgroup.ourspace;

public class Logs {
    String user;
    String title;
    String date;
    String content;

    public Logs() {
    }

    public Logs(String user, String title, String date, String content) {
        this.user = user;
        this.title = title;
        this.date = date;
        this.content = content;
    }

    public String getUser(){ return user;}
    public String getTitle(){ return title;}
    public String getDate(){ return date;}
    public String getContent(){ return content;}

    public void setUser(String user) {
        this.user = user;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
