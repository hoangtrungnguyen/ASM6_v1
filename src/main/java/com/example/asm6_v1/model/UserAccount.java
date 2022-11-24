package com.example.asm6_v1.model;

public class UserAccount {
    private String uid;

    private String password;

    private int attempts;

    private int loginCounter;


    public UserAccount(String uid, String password, int attempts, int loginCounter) {
        this.uid = uid;
        this.password = password;
        this.attempts = attempts;
        this.loginCounter = loginCounter;
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

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public int getLoginCounter() {
        return loginCounter;
    }

    public void setLoginCounter(int loginCounter) {
        this.loginCounter = loginCounter;
    }


    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s", uid, password, attempts, loginCounter);
    }

}
