package com.example.asm6_v1.service;

import com.example.asm6_v1.model.*;

public interface UserService {
    Result validateUser(UserLoginForm loginForm);
    Result signUpUser(UserRegisterForm userRegisterForm) throws UserNotFoundException;

    UserAccount getUserById(String uid);

    Result validateForm(UserLoginForm loginForm);

    boolean isFirstTime(String uid);

    Result validateHintQuestions(UserRegisterForm userRegisterForm);
    Result validatePasswords(UserRegisterForm userRegisterForm, UserAccount foundUser);

}
