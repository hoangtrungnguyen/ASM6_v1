package com.example.asm6_v1.service;

import com.example.asm6_v1.model.Result;
import com.example.asm6_v1.model.UserAccount;
import com.example.asm6_v1.model.UserLoginForm;
import com.example.asm6_v1.model.UserRegisterForm;

public interface UserService {
    Result isUserValid(UserLoginForm loginForm);
    Result signUpUser(UserRegisterForm userRegisterForm);

    UserAccount getUserById(String uid);

    Result isFormValid(UserLoginForm loginForm);

    boolean isFirstTime(String uid);
}
