package com.example.asm6_v1.model;

import org.apache.catalina.User;

public class UserLoginForm {

    private String uid;
    private String password;

    private String errorMessage;

    public UserLoginForm(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public UserLoginForm(){

    }



    public UserLoginForm(String uid, String password){
        this.uid = uid;
        this.password =password;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "email='" + uid + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
