package com.example.asm6_v1.model;

public class Result {
    private boolean isOk;
    private String message;

    public Result(boolean isOk, String message) {
        this.isOk = isOk;
        this.message = message;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


