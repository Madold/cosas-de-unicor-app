package com.markusw.cosasdeunicorapp.data.model;

public class User {
    private String displayName;
    private String email;
    private String photoUrl;
    private String uid;

    public User() {
    }

    public User(String displayName, String email, String photoUrl, String uid) {
        this.displayName = displayName;
        this.email = email;
        this.photoUrl = photoUrl;
        this.uid = uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
