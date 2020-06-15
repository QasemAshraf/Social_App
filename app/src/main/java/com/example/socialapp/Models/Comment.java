package com.example.socialapp.Models;

import com.google.firebase.database.ServerValue;

public class Comment {

    private String content, userName, UserImg, commentDate;
    private User user;

    public Comment() {

    }


    public Comment(String content, String userName, String userImg, String commentDate) {
        this.content = content;
        this.userName = userName;
        this.UserImg = userImg;
        this.commentDate = commentDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImg() {
        return UserImg;
    }

    public void setUserImg(String userImg) {
        UserImg = userImg;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }
}
