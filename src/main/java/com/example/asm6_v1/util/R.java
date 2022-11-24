package com.example.asm6_v1.util;

import java.util.Map;

public class R {


    static public final String PASSWORD_IS_INVALID = "Password is not valid";
    public static final String ID_IS_LOCKED = "Id is locked";
    public static final String ID_NOT_FOUND = "Id is not found";


    public static final String PASSWORD_LENGTH_NOT_EQUAL_TO_8 = "Password length is not equal to 8";
    public static final String ID_LENGTH_NOT_EQUAL_TO_16 = "Uid must be equal to 16";


    public static class ModelName{
        static public final String LOGIN_FORM = "loginForm";
        static public final String REGISTER_FORM = "userRegisterForm";
        public static final String ACCOUNT = "account";
    }

    public static class SignUp {
        static public final String HINT_QUESTIONS_CAN_NOT_EMPTY = "Hint questions can not be empty";
        static public final String PASSWORDS_ARE_NOT_MATCHED = "Passwords are not matched";
        static public final String OLD_PASSWORD_IS_NOT_VALID= "Old password is not valid";
    }


    public static class View {
        static public final String ACCOUNT = "account";
        public static final String SIGNUP = "sign-up";
    }
    public static class Session {
        static public final String UID = "uid";
    }

}
