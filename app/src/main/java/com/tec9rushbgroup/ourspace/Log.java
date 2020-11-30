package com.tec9rushbgroup.ourspace;

import kotlin.UInt;

public class Log {
    private String title;
    private String content;
    private String spaceUID;
    private String UID;

    public Log() {
    }

    public Log(String t, String c,String spaceUID,String UID){
        this.title = t;
        this.spaceUID = spaceUID;
        this.content = c;
        this.UID= UID;
    }

    public String getTitle() {
        return title;
    }

    public String getUID() {
        return UID;
    }

    public String getSpaceUID() {
        return spaceUID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setSpaceUID(String spaceUID) {
        this.spaceUID = spaceUID;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public void setContent(String content) {
        this.content = content;
    }
}
