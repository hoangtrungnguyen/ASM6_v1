package com.example.asm6_v1.controller;


import com.example.asm6_v1.model.*;
import com.example.asm6_v1.service.UserService;
import com.example.asm6_v1.service.UserServiceImpl;
import com.example.asm6_v1.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("uid")
public class AuthenticationController {
    private final UserService userService;

    public AuthenticationController(@Autowired UserServiceImpl userService) {
        this.userService = userService;
    }


    @GetMapping("/")
    public String landing() {
        return "landing";
    }

    @GetMapping("/login")
    public ModelAndView login(@ModelAttribute String uid) {

        if(uid != null){
            return accountView(userService, uid);
        }

        return loginView(new UserLoginForm());
    }


    /**
     *  if form is not valid
     *      return login page
     *
     *  if user information is not valid
     *      if id is not in db
     *          show a prompt
     *          return login page
     *      if pwd not valid
     *          if is third attempt
     *              show a prompt: lock account
     *              return login page
     *          else
     *              show a prompt: re-enter password
     *              return login page
     *  if first time login
     *      return sign-up page
     *
     *
     *  return account page
     */
    @PostMapping("/login")
    public ModelAndView loginSubmit(@ModelAttribute UserLoginForm loginForm) {
        System.out.println(loginForm);

        Result isFormValidResult = userService.isFormValid(loginForm);
        if(!isFormValidResult.isOk()){
            return loginView(isFormValidResult.getMessage());
        }
        Result result = userService.validateUser(loginForm);
        if(!result.isOk()) {
            return loginView(result.getMessage());

        }

        if(userService.isFirstTime(loginForm.getUid())){
           return signUpView(loginForm);
        }

        return accountView(userService,loginForm.getUid());
    }

    /**
     *      if  hint questions false
     *          show error message
     *          return
     *      if password is false
     *          show error message
     *          return
     *
     * move to account page
     */
    @PostMapping("/sign-up")
    public ModelAndView signUpSubmit(Model model){
        UserLoginForm loginForm =(UserLoginForm) model.getAttribute("loginForm");
        UserRegisterForm userRegisterForm = (UserRegisterForm) model.getAttribute(R.ModelName.REGISTER_FORM);

        assert loginForm != null;
        assert userRegisterForm != null;

        userRegisterForm.setUid(loginForm.getUid());

        Result result;
        try {
            result = this.userService.signUpUser(userRegisterForm);
        } catch (UserNotFoundException e) {
            return signUpView(loginForm, userRegisterForm);
        }

        if(!result.isOk()){

            if(result.getMessage().equalsIgnoreCase(R.SignUp.HINT_QUESTIONS_CAN_NOT_EMPTY)){
                userRegisterForm.setHintErrorMessage(result.getMessage());
                return signUpView(loginForm,userRegisterForm);
            }

            userRegisterForm.setPasswordErrorMessage(result.getMessage());
            return signUpView(loginForm, userRegisterForm);
        }

        return accountView(userService, loginForm.getUid());
    }

    static private ModelAndView loginView(UserLoginForm loginForm){
        return new ModelAndView("login", "loginForm", loginForm);
    }

    static private ModelAndView loginView(String message){
        return new ModelAndView("login", "loginForm", new UserLoginForm(message));
    }
    static private ModelAndView signUpView(UserLoginForm loginForm){
        final ModelAndView model = new ModelAndView("sign-up");
        model.addObject("loginForm", loginForm);
        model.addObject(R.Session.UID, loginForm.getUid());
        model.addObject(new ModelAndView( "sign-up","userRegisterForm", new UserRegisterForm()));
        return model;
    }
    static private ModelAndView signUpView(UserLoginForm loginForm,UserRegisterForm userRegisterForm ){
        final ModelAndView model = new ModelAndView("sign-up");
        model.addObject(R.ModelName.LOGIN_FORM, loginForm);
        model.addObject(R.Session.UID, loginForm.getUid());
        model.addObject(R.ModelName.REGISTER_FORM, userRegisterForm);
        return model;
    }

    static private ModelAndView accountView(UserService userService,String id){
        UserAccount account = userService.getUserById(id);
        return new ModelAndView("account", "account", account);
    }
}
