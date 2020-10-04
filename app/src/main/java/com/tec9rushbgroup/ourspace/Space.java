package com.tec9rushbgroup.ourspace;

public class Space {
    String user1;
    String user2;
    String folder;
    String name;
    Boolean stat1;
    Boolean stat2;
    Boolean status;

    public Space(){}

    public Space(String user1, String user2, String folder, String name, Boolean stat1, Boolean stat2, Boolean status) {
        this.user1 = user1;
        this.user2 = user2;
        this.folder = folder;
        this.name = name;
        this.stat1 = stat1;
        this.stat2 = stat2;
        this.status = status;
    }
}
