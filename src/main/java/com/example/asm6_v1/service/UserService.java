package com.example.asm6_v1.service;

import com.example.asm6_v1.model.*;

public interface UserService {
    Result validateUser(UserLoginForm loginForm);
    Result signUpUser(UserRegisterForm userRegisterForm) throws UserNotFoundException;

    UserAccount getUserById(String uid);

    Result isFormValid(UserLoginForm loginForm);

    boolean isFirstTime(String uid);
}
