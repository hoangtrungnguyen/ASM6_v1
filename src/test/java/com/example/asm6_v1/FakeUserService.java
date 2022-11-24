package com.example.asm6_v1;

import com.example.asm6_v1.model.Result;
import com.example.asm6_v1.model.UserAccount;
import com.example.asm6_v1.model.UserLoginForm;
import com.example.asm6_v1.model.UserRegisterForm;
import com.example.asm6_v1.service.UserService;

public class FakeUserService implements UserService {
    @Override
    public Result validateUser(UserLoginForm loginForm) {
        return null;
    }

    @Override
    public Result signUpUser(UserRegisterForm userRegisterForm) {
        return null;
    }

    @Override
    public UserAccount getUserById(String uid) {
        return null;
    }

    @Override
    public Result isFormValid(UserLoginForm loginForm) {
        return null;
    }

    @Override
    public boolean isFirstTime(String uid) {
        return false;
    }
}
