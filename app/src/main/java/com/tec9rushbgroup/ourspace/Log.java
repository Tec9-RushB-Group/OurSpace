package com.tec9rushbgroup.ourspace;

public class Log extends Space{
    private String title;
    private String description;
    private String content;

    public Log() {
    }

    public Log(String t, String d, String c){
        this.title = t;
        this.description = d;
        this.content = c;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
