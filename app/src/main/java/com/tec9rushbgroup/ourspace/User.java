package com.tec9rushbgroup.ourspace;

public class User {
    String email;
    String userName;
    String Uid;
    String photoPath;


    public User() {
    }

    public User(String email, String userName,String uid,String photoPath) {
        this.email = email;
        this.userName = userName;
        this.Uid = uid;
        this.photoPath = photoPath;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
