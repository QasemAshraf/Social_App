package com.example.socialapp.Models;


public class Post {

    private String userName;
    private String userImg;
    private String postKey;
    private String title;
    private String description;
    private String picture;
    private String date;
    private User user;
    private String UserPostId;


    public Post () {

    }

    public Post(String userName, String userImg, String UserPostId, String title, String description, String picture, String date  ) {

        this.userName = userName;
        this.userImg = userImg;
        this.UserPostId = UserPostId;
        this.title = title;
        this.description = description;
        this.picture = picture;
        this.date = date;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getUserPostId() {
        return UserPostId;
    }

    public void setUserPostId(String userPostId) {
        UserPostId = userPostId;
    }

    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}