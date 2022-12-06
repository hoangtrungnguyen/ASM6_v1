package com.example.asm6_v1.model;

import org.springframework.lang.Nullable;

public class UserRegisterForm {

    private String uid;

    private String oldPassword;
    private String password;
    private String password2;

    private String passwordErrorMessage;

    private String question1;
    private String question2;
    private String question3;

    private String answer1;
    private String answer2;
    private String answer3;


    private String hintErrorMessage;



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public String getQuestion2() {
        return question2;
    }

    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    public String getQuestion3() {
        return question3;
    }

    public void setQuestion3(String question3) {
        this.question3 = question3;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getPasswordErrorMessage() {
        return passwordErrorMessage;
    }

    public void setPasswordErrorMessage(String passwordErrorMessage) {
        this.passwordErrorMessage = passwordErrorMessage;
    }

    public String getHintErrorMessage() {
        return hintErrorMessage;
    }

    public void setHintErrorMessage(String hintErrorMessage) {
        this.hintErrorMessage = hintErrorMessage;
    }

    public boolean isHasAnswer() {
        return (
                isNotNullOrEmpty(answer1)  && isNotNullOrEmpty(question1)

        ) || (
                isNotNullOrEmpty(answer2)  && isNotNullOrEmpty(question2)
        ) || (
                isNotNullOrEmpty(answer3)  && isNotNullOrEmpty(question3)
        );
    }

    private boolean isNotNullOrEmpty(@Nullable String value){
        if(value == null) return false;
        return !value.isEmpty();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean arePasswordsEqual() {
        return password.equals(password2);
    }

    public UserRegisterForm buildPassword(String password){
        this.password = password;
        return this;
    }

    public UserRegisterForm buildPassword2(String password){
        this.password2 = password;
        return this;
    }
    public UserRegisterForm buildOldPassword(String password){
        this.oldPassword = password;
        return this;
    }

    public UserRegisterForm buildUid(String uid){
        this.uid = uid;
        return this;
    }

    public boolean isPasswordOneValid() {
        return password.length() == 8;
    }

    public boolean isPasswordTwoValid() {
        return password2.length() == 8;
    }
}
