package com.example.asm6_v1.service;

import com.example.asm6_v1.data.DatabaseLayer;
import com.example.asm6_v1.model.Result;
import com.example.asm6_v1.model.UserAccount;
import com.example.asm6_v1.model.UserLoginForm;
import com.example.asm6_v1.model.UserRegisterForm;
import com.example.asm6_v1.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    private final DatabaseLayer databaseLayer;

    public UserServiceImpl(@Autowired DatabaseLayer databaseLayer){
        this.databaseLayer = databaseLayer;
    }

    @Override
    public Result isUserValid(UserLoginForm loginForm) {
       UserAccount foundUser = databaseLayer.getUserById(loginForm.getUid());
       if(foundUser == null){
           return new Result(false, R.ID_NOT_FOUND);
       }

        if(foundUser.getAttempts() >= 3){
            return new Result(false, R.ID_IS_LOCKED);
        }

       if(!foundUser.getPassword().equals(loginForm.getPassword())){
           int newAttempt = foundUser.getAttempts() + 1;
           foundUser.setAttempts(newAttempt);
           this.databaseLayer.update(foundUser);
           return new Result(false, R.PASSWORD_IS_INVALID);
       }

        int newLoginCounter = foundUser.getLoginCounter() + 1;
        foundUser.setLoginCounter(newLoginCounter);
        this.databaseLayer.update(foundUser);

        return new Result(true, "Ok");
    }

    @Override
    public Result signUpUser(UserRegisterForm userRegisterForm){
        if(!userRegisterForm.isHasAnswer()){
            return new Result(false, R.SignUp.HINT_QUESTIONS_CAN_NOT_EMPTY);
        }

        UserAccount foundUser = getUserById(userRegisterForm.getUid());

        if(!userRegisterForm.getOldPassword().equals(foundUser.getPassword())){
            return new Result(false, R.SignUp.OLD_PASSWORD_IS_NOT_VALID);
        }

        if(!userRegisterForm.arePasswordsEqual()){
            return new Result(false, R.SignUp.PASSWORDS_ARE_NOT_MATCHED);
        }
        return new Result(true, "OK");
    }

    @Override
    public UserAccount getUserById(String uid) {
        return databaseLayer.getUserById(uid);
    }


    @Override
    public Result isFormValid(UserLoginForm loginForm) {
        String pwd = loginForm.getPassword();
        if(pwd.length() != 8){
            return new Result(false, R.PASSWORD_LENGTH_NOT_EQUAL_TO_8);
        } else if(loginForm.getUid().length() != 16){
            return new Result(false, R.ID_LENGTH_NOT_EQUAL_TO_16);
        }
        return new Result(true, "Ok");
    }


    @Override
    public boolean isFirstTime(String uid) {
        return getUserById(uid).getLoginCounter() == 1;
    }

//    public UserAccount getUserAccount(){
//        databaseLayer
//    }
}
