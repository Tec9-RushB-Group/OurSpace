package com.tec9rushbgroup.ourspace;

public class Anniversary {
    private String description;
    private String Date;
    private String spaceUID;
    private String UID;

    public Anniversary() {
    }

    public Anniversary(String description, String Date,String spaceUID,String UID) {
        this.description = description;
        this.Date = Date;
        this.spaceUID = spaceUID;
        this.UID = UID;
    }

    public String getDate() {
        return Date;
    }

    public String getDescription() {
        return description;
    }

    public String getSpaceUID() {
        return spaceUID;
    }

    public String getUID() {
        return UID;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSpaceUID(String spaceUID) {
        this.spaceUID = spaceUID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
