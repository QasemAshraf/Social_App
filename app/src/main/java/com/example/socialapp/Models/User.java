package com.example.socialapp.Models;

public class User {

    private String userPhoto;
    private String userName;
    private String userEmail;
    private String pass1;
    private String pass2;
    public String userID;

    public User() {

    }

    public User(String userPhoto, String userName, String userEmail, String pass1, String pass2) {
        this.userID = userID;
        this.userPhoto = userPhoto;
        this.userName = userName;
        this.userEmail = userEmail;
        this.pass1 = pass1;
        this.pass2 = pass2;

    }
//
//    public User(String userPhoto, String userName, String userEmail) {
//        this.userPhoto = userPhoto;
//        this.userName = userName;
//        this.userEmail = userEmail;
//    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


    public String getUserPhoto() {
        return  userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPass1() {
        return pass1;
    }

    public void setPass1(String pass1) {
        this.pass1 = pass1;
    }

    public String getPass2() {
        return pass2;
    }

    public void setPass2(String pass2) {
        this.pass2 = pass2;
    }
}
