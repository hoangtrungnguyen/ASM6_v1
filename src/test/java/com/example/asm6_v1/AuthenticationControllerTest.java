package com.example.asm6_v1;


import com.example.asm6_v1.controller.AuthenticationController;
import com.example.asm6_v1.data.DatabaseLayer;
import com.example.asm6_v1.model.UserAccount;
import com.example.asm6_v1.model.UserLoginForm;
import com.example.asm6_v1.model.UserRegisterForm;
import com.example.asm6_v1.service.UserService;
import com.example.asm6_v1.service.UserServiceImpl;
import com.example.asm6_v1.util.R;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationControllerTest {

    @Autowired
    private AuthenticationController authenticationController;

    private UserService userService;


    @Test
    public void contextLoads() throws Exception {
        assertThat(authenticationController).isNotNull();
    }



    @Nested
    class LoginPost{

        @BeforeEach
        public void setUp() throws IOException {
            userService = new UserServiceImpl(
                    new DatabaseLayer("db/fake_users.txt")
            );
            authenticationController = new AuthenticationController((UserServiceImpl) userService);
        }


        @AfterEach
        public void cleanUp() throws Exception {
            Helper.copyContent(new ClassPathResource("db/users.txt").getFile(), new ClassPathResource("db/fake_users.txt").getFile());
        }


        @Test
        public void logInFormFormatIsInValid_invalidPassword(){
            final UserLoginForm model = new UserLoginForm();
            model.setUid("12345678901234562");
            model.setPassword("123456789");
            ModelAndView modelAndView = authenticationController.loginSubmit(model);
            String errorMessage = ((UserLoginForm) modelAndView.getModel().get(R.ModelName.LOGIN_FORM)).getErrorMessage();
            assertEquals(R.ID_LENGTH_NOT_EQUAL_TO_16, errorMessage);
        }
        @Test
        public void logInFormFormatIsInValid_invalidID(){
            final UserLoginForm model = new UserLoginForm();
            model.setUid("0000000000000002");
            model.setPassword("a1234567");
            ModelAndView modelAndView = authenticationController.loginSubmit(model);
            String errorMessage = ((UserLoginForm) modelAndView.getModel().get(R.ModelName.LOGIN_FORM)).getErrorMessage();
            assertEquals(R.PASSWORD_LENGTH_NOT_EQUAL_TO_8, errorMessage);
        }


        @Test
        public void attemptsIncreaseByOneIfAfterEnterInvalidPassword(){
            final UserLoginForm model = new UserLoginForm();
            model.setUid("0000000000000002");
            model.setPassword("a1234563");
            authenticationController.loginSubmit(model);
            assertEquals(1, userService.getUserById(model.getUid()).getAttempts());
        }


        /**
         * Id is not
         */
        @Test
        public void idIsNotInDatabase(){
            final UserLoginForm model = new UserLoginForm();
            model.setUid("0000000000000008");
            model.setPassword("a1234567");
            ModelAndView modelAndView = authenticationController.loginSubmit(model);
            UserLoginForm userLoginForm = (UserLoginForm) modelAndView.getModel().get("loginForm");
            assertEquals( R.ID_NOT_FOUND, userLoginForm.getErrorMessage());
        }


        @Test
        public void idIsNotInDatabase_and_idIsLocked(){
            final UserLoginForm model = new UserLoginForm();
            model.setUid("0000000000000005");
            model.setPassword("a1234567");
            ModelAndView modelAndView = authenticationController.loginSubmit(model);
            UserLoginForm userLoginForm = (UserLoginForm) modelAndView.getModel().get("loginForm");
            assertEquals( R.ID_IS_LOCKED, userLoginForm.getErrorMessage());
        }

        /**
         * User is not in database. In this case, web redirect user to sign up page
         */
        @Test
        public void idIsInDatabase_and_passwordIsInvalid(){
            final UserLoginForm model = new UserLoginForm();
            model.setUid("0000000000000001");
            model.setPassword("a1234563");
            final ModelAndView modelAndView =  authenticationController.loginSubmit(model);
            assertEquals( "login",modelAndView.getViewName());
            assertEquals(R.PASSWORD_IS_INVALID, ((UserLoginForm) modelAndView.getModel().get("loginForm")).getErrorMessage());
        }


        /**
         * User is not in database. In this case, web redirect user to sign up page
         */
        @Test
        public void idIsInDatabase_and_passwordIsValid_and_isTheFirstTime(){
            final UserLoginForm model = new UserLoginForm();
            model.setUid("0000000000000001");
            model.setPassword("a1234567");
            final String viewName =  authenticationController.loginSubmit(model).getViewName();
            assertEquals( "sign-up",viewName);
        }

        @Test
        public void idIsInDatabase_and_passwordIsValid(){
            final UserLoginForm model = new UserLoginForm();
            model.setUid("1234567890123456");
            model.setPassword("a1234567");
            final UserAccount actual = (UserAccount) authenticationController.loginSubmit(model).getModel().get("account");

            assertThat(actual.getUid()).isNotBlank();
        }

        @Test
        public void userMoveToAccountPagePasswordIsValid(){
            final UserLoginForm model = new UserLoginForm();
            model.setUid("1234567890123456");
            model.setPassword("a1234567");
            final String view =  authenticationController.loginSubmit(model).getViewName();

            assertEquals ("account", view);
        }

    }

    /**
     * Pre-condition:
     *  1. uid and password are valid
     *  2. user
     */

    @Nested
    class SignUpPage{

        private UserAccount fakeAccount = new UserAccount(
                "0000000000000002",
                "a1234567",
                0,
                23
        );

        @Test
        public void thereAreNoAnswer(){
            UserRegisterForm form = new UserRegisterForm();

            Model model = new ExtendedModelMap();
            model.addAttribute(R.ModelName.REGISTER_FORM,form);
            model.addAttribute(R.ModelName.LOGIN_FORM,new UserLoginForm(fakeAccount.getUid(), fakeAccount.getPassword()));

            ModelAndView model0 = authenticationController.signUpSubmit(model);

            form.setHintErrorMessage(R.SignUp.HINT_QUESTIONS_CAN_NOT_EMPTY);

            assertThat(model0.getModel().get(R.ModelName.REGISTER_FORM)).isEqualTo(
                    form
            );

        }

        @Test
        public void oldPasswordIsNotValid(){
            UserRegisterForm form = new UserRegisterForm();

            form.setPassword("a1234563");

            Model model = new ExtendedModelMap();
            model.addAttribute(R.ModelName.REGISTER_FORM,form);
            model.addAttribute(R.ModelName.LOGIN_FORM,new UserLoginForm(fakeAccount.getUid(), fakeAccount.getPassword()));
            ModelAndView model0 = authenticationController.signUpSubmit(model);

            form.setPasswordErrorMessage(R.SignUp.OLD_PASSWORD_IS_NOT_VALID);

            assertThat(model0.getModel().get(R.ModelName.REGISTER_FORM)).isEqualTo(
                    form
            );
        }

        @Test
        public void passwordsAreNotMatched(){

        }

        @Test
        public void passwordsAreMatched(){

        }

        @Test
        public void signUpIsOk(){
            UserRegisterForm form = new UserRegisterForm();
            form.setAnswer1("Answer 1");
            form.setQuestion1("Question 1");
            form.setUid(fakeAccount.getUid());
            form.setOldPassword(fakeAccount.getPassword());

            Model model = new ExtendedModelMap();
            model.addAttribute(R.ModelName.REGISTER_FORM,form);
            model.addAttribute(R.ModelName.LOGIN_FORM,new UserLoginForm(fakeAccount.getUid(), fakeAccount.getPassword()));


            ModelAndView model0 = authenticationController.signUpSubmit(model);

            UserAccount account = (UserAccount) model0.getModel().get(R.ModelName.ACCOUNT);
            assertThat(model0.getViewName()).isEqualTo(R.View.ACCOUNT);
        }
    }


}
