package com.example.asm6_v1;


import com.example.asm6_v1.data.DatabaseLayer;
import com.example.asm6_v1.model.Result;
import com.example.asm6_v1.model.UserLoginForm;
import com.example.asm6_v1.model.UserNotFoundException;
import com.example.asm6_v1.model.UserRegisterForm;
import com.example.asm6_v1.service.UserService;
import com.example.asm6_v1.service.UserServiceImpl;
import com.example.asm6_v1.util.R;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AuthenticationServiceTest {


        private UserService userService;


        @BeforeEach
        public void setUpService() throws IOException {
            userService = new UserServiceImpl(new DatabaseLayer("db/fake_users.txt"));
        }

        @AfterEach
        public void rollBackDB() throws Exception {
            Helper.copyContent(new ClassPathResource("db/users.txt").getFile(), new ClassPathResource("db/fake_users.txt").getFile());
        }


        /**
         * 3 TH
         * Test 1: uid is invalid and password is valid
         * Test 2: uid is valid and password is invalid
         * Test 3: both uid and password are valid
        **/
        @ParameterizedTest
        @MethodSource("provide_UidPassword_forFormatFunction")
        public void isFormValid(String uid, String password, Result expectedResult){
                Result result = userService.isFormValid(new UserLoginForm(uid, password));
                assertThat(result).usingRecursiveComparison()
                        .isEqualTo(expectedResult);
        }

        private static Stream<Arguments> provide_UidPassword_forFormatFunction(){
            return Stream.of(
                    Arguments.of("12345678901234562","12345678" ,
                            new Result(false,"Uid must be equal to 16" )),
              Arguments.of("0000000000000002","123456789" ,
                      new Result(false,"Password length is not equal to 8" )),
                    Arguments.of("0000000000000002","a1234567" ,
                            new Result(true,"Ok"))
            );
        }


        /***
         *
        **/
        @ParameterizedTest
        @MethodSource("provideData_forValidateHintQuestions")
        public void  signUpUser_validateHintQuestions(String question, String answer, Result expected) throws UserNotFoundException {
            UserRegisterForm form =  new UserRegisterForm();
            form.buildOldPassword("a1234567")
                    .buildUid("0000000000000001")
                    .buildPassword2("a123456q")
                    .buildPassword("a123456q");

            form.setQuestion1(question);
            form.setAnswer1(answer);

            Result result = userService.signUpUser(form);
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        }


        private static Stream<Arguments> provideData_forValidateHintQuestions(){
            return Stream.of(
                     Arguments.of("Question 1", "Answer 1", new Result(
                             true, R.OK
                     )),
                    Arguments.of("Question 1", null, new Result(false, R.SignUp.HINT_QUESTIONS_CAN_NOT_EMPTY)),
                    Arguments.of(null, "Answer 1", new Result(false, R.SignUp.HINT_QUESTIONS_CAN_NOT_EMPTY)),
                    Arguments.of(null, null, new Result(false, R.SignUp.HINT_QUESTIONS_CAN_NOT_EMPTY))
            );
        }


    @ParameterizedTest
    @MethodSource("provide_UidPassword_forSignUpUser")
    public void  signUpUser_validatePasswords( String oldPassword, String password0, String password2, Result expectedResult, String description) throws UserNotFoundException {
        UserRegisterForm form =  new UserRegisterForm();
        form.setUid("0000000000000001");
        form.setQuestion1("question 1");
        form.setAnswer1("answer 1");

        form.setOldPassword(oldPassword);
        form.setPassword(password0);
        form.setPassword2(password2);

        Result actual = userService.signUpUser(form);

        assertThat(actual).usingRecursiveComparison().describedAs(
                description
        ).isEqualTo(expectedResult);
    }


        private static Stream<Arguments> provide_UidPassword_forSignUpUser(){
            return Stream.of(
                    Arguments.of("a1234567", "a123456b","a123456b", new Result(true, R.OK),"Đúng"),
                    Arguments.of("a1234567","a123456","a123456b", new Result(false, R.SignUp.PASSWORD_ONE_IS_NOT_EQUAL_TO_8), "Password 1 sai"),
                    Arguments.of("a1234567","a123456b","a123456", new Result(false, R.SignUp.PASSWORD_TWO_IS_NOT_EQUAL_TO_8), "Password 2 sai"),
                    Arguments.of("a1234567", "a123456b","a123456c", new Result(false, R.SignUp.PASSWORDS_ARE_NOT_MATCHED),"Hai password không trùng nhau"),
                    Arguments.of("a1234562", "a123456b","a123456b", new Result(false, R.SignUp.OLD_PASSWORD_IS_NOT_VALID),"Mật mã nhập vào sai"),
                    Arguments.of("a1234562", "a123456b","a123456b", new Result(false, R.SignUp.OLD_PASSWORD_IS_NOT_VALID),"Mật mã nhập vào sai")


                    );
        }

    @ParameterizedTest
    @MethodSource("provide_UidPassword_forValidateUser_function")
    public void  validateUser(String uid, String password, Result expectedResult, String message){
        Result result = userService.validateUser(new UserLoginForm(uid, password));
        assertThat(result).usingRecursiveComparison()
                .describedAs(message)
                .isEqualTo(expectedResult);
    }


    private static Stream<Arguments> provide_UidPassword_forValidateUser_function(){
        return Stream.of(
                Arguments.of("0000000000000001","a1234567", new Result(
                        true, "Ok"
                ), "Id not found test case"),
                Arguments.of("0000000000000001","a1234564", new Result(
                        false, R.PASSWORD_IS_INVALID
                ), "Password is not valid"),
                Arguments.of("0000000000000005","a1234567", new Result(
                        false, R.ID_IS_LOCKED
                ), "Id not found test case"),
                Arguments.of("0000000000000008","a1234564", new Result(
                        false, R.ID_NOT_FOUND
                ), "Id not found test case")
        );
    }






}
