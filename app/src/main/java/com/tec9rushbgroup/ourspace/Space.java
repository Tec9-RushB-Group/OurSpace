package com.tec9rushbgroup.ourspace;

public class Space {
    String user1;
    String user2;
    String folder;
    String name;
    Boolean user1_stat;
    Boolean user2_stat;
    Boolean space_stat;
    String spaceUid;

    public Space() {
        this.user1 = "";
        this.user2 = "";
        this.folder = "";
        this.name = "";
        this.spaceUid = "";
        this.user1_stat = false;
        this.user2_stat = false;
        this.space_stat = false;

    }

    public Space(String spaceUid, String user1, String user2, String folder, String name, Boolean user1_stat, Boolean user2_stat, Boolean status) {
        this.user1 = user1;
        this.user2 = user2;
        this.folder = folder;
        this.name = name;
        this.spaceUid = spaceUid;
        this.user1_stat = user1_stat;
        this.user2_stat = user2_stat;
        this.space_stat = status;
    }

    public String getUser1() {
        return user1;
    }

    public String getUser2() {
        return user2;
    }

    public String getFolder() {
        return folder;
    }

    public String getName() {
        return name;
    }

    public Boolean getSpace_stat() {
        return space_stat;
    }

    public Boolean getUser1_stat() {
        return user1_stat;
    }

    public Boolean getUser2_stat() {
        return user2_stat;
    }

    public String getSpaceUid() {
        return spaceUid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUser1(String user) {
        this.user1 = user;
    }

    public void setUser2(String user) {
        this.user2 = user;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public void setSpace_stat(Boolean space_stat) {
        this.space_stat = space_stat;
    }

    public void setSpaceUid(String spaceUid) {
        this.spaceUid = spaceUid;
    }

    public void setUser1_stat(Boolean user1_stat) {
        this.user1_stat = user1_stat;
    }

    public void setUser2_stat(Boolean user2_stat) {
        this.user2_stat = user2_stat;
    }

    public void space_stat(Boolean space_stat) {
        this.space_stat = space_stat;
    }

}
