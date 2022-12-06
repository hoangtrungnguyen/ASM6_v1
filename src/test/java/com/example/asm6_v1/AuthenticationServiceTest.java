package com.example.asm6_v1;


import com.example.asm6_v1.data.DatabaseLayer;
import com.example.asm6_v1.model.*;
import com.example.asm6_v1.service.UserService;
import com.example.asm6_v1.service.UserServiceImpl;
import com.example.asm6_v1.util.R;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
        void setUpService() throws IOException {
            userService = new UserServiceImpl(new DatabaseLayer("db/fake_users.txt"));
        }

        @AfterEach
        void rollBackDB() throws Exception {
            Helper.copyContent(new ClassPathResource("db/users.txt").getFile(), new ClassPathResource("db/fake_users.txt").getFile());
        }


    /**
     *
     * <a href="https://docs.google.com/spreadsheets/d/1oyqL4dWLqDEEbQs0EqJA6cF9r-tmNSJ3/edit#gid=769130491">Sheet to testcase</a>
     */
    @ParameterizedTest
    @MethodSource("provideDataFor_IsFirstTimeLogin")
    public void isFirstTimeLogin(String uid, boolean expected){
        boolean actual = userService.isFirstTime(uid);
        assertEquals(expected,actual);
    }

        /**
         * 3 TH
         * Test 1: uid is invalid and password is valid
         * Test 2: uid is valid and password is invalid
         * Test 3: both uid and password are valid
         * <a href="https://docs.google.com/spreadsheets/d/1oyqL4dWLqDEEbQs0EqJA6cF9r-tmNSJ3/edit#gid=740294220">Sheet to testcase</a>
         **/
        @ParameterizedTest
        @MethodSource("provide_UidPassword_forFormatFunction")
        public void validateForm(String uid, String password, Result expectedResult){
                Result result = userService.validateForm(new UserLoginForm(uid, password));
                assertThat(result).usingRecursiveComparison()
                        .isEqualTo(expectedResult);}

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
    @Nested
    class SignUpTest{

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
            @MethodSource("provideUidPasswordFor_ValidatePassword")
            public void  validatePassword( String oldPassword, String password0, String password2, Result expectedResult, String description) throws UserNotFoundException {
                UserRegisterForm form =  new UserRegisterForm();
                form.setUid("0000000000000001");
                form.setQuestion1("question 1");
                form.setAnswer1("answer 1");

                form.setOldPassword(oldPassword);
                form.setPassword(password0);
                form.setPassword2(password2);

                Result actual = userService.validatePasswords(form, userService.getUserById(form.getUid()));

                assertThat(actual).usingRecursiveComparison().describedAs(
                        description
                ).isEqualTo(expectedResult);
            }

            private static Stream<Arguments> provideUidPasswordFor_ValidatePassword(){
                return Stream.of(
                        Arguments.of("a1234567", "a123456b","a123456b", new Result(true, R.OK),"Đúng"),
                        Arguments.of("a1234567","a123456","a123456b", new Result(false, R.SignUp.PASSWORD_ONE_IS_NOT_EQUAL_TO_8), "Password 1 sai"),
                        Arguments.of("a1234567","a123456b","a123456", new Result(false, R.SignUp.PASSWORD_TWO_IS_NOT_EQUAL_TO_8), "Password 2 sai"),
                        Arguments.of("a1234567", "a123456b","a123456c", new Result(false, R.SignUp.PASSWORDS_ARE_NOT_MATCHED),"Hai password không trùng nhau"),
                        Arguments.of("a1234562", "a123456b","a123456b", new Result(false, R.SignUp.OLD_PASSWORD_IS_NOT_VALID),"Mật mã nhập vào sai"),
                        Arguments.of("a1234562", "a123456b","a123456b", new Result(false, R.SignUp.OLD_PASSWORD_IS_NOT_VALID),"Mật mã nhập vào sai")


                );
            }

        }


    /**
     * <a href="https://docs.google.com/spreadsheets/d/1oyqL4dWLqDEEbQs0EqJA6cF9r-tmNSJ3/edit#gid=403360878">path to test case</a>
     */
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


    private static Stream<Arguments> provideDataFor_IsFirstTimeLogin(){
            return Stream.of(
                    Arguments.of("0000000000000006",true),
                    Arguments.of("0000000000000002", false)
            );
    }

    @ParameterizedTest
    @MethodSource("provideDataFor_validateHinQuestionTest")
    public void validateHintQuestions(String question, String answer, Result expected){
        UserRegisterForm form = new UserRegisterForm();
        form.setAnswer1(answer);
        form.setQuestion1(question);
        Result result = userService.validateHintQuestions(form);
        assertThat(result).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    private static Stream<Arguments> provideDataFor_validateHinQuestionTest(){
        return Stream.of(
                Arguments.of(null, null,new Result(false, R.SignUp.HINT_QUESTIONS_CAN_NOT_EMPTY)),
                Arguments.of("Do you have a dog?", null,new Result(false, R.SignUp.HINT_QUESTIONS_CAN_NOT_EMPTY)),
                Arguments.of(null, "Yes",new Result(false, R.SignUp.HINT_QUESTIONS_CAN_NOT_EMPTY)),
                Arguments.of("Do you have a dog?", "Yes",new Result(true, R.OK))
        );
    }


    @ParameterizedTest
    @MethodSource("provideDataFor_validatePasswords")
    public void validatePasswords(String oldPassword, String pwd1, String pwd2, Result expected){
        UserRegisterForm form = new UserRegisterForm();
        form.setQuestion1("question 1");
        form.setQuestion1("answer 1");
        form.setPassword(pwd1);
        form.setPassword2(pwd2);
        form.setOldPassword(oldPassword);
        Result result = userService.validatePasswords(form,
                new UserAccount("0000000000000001",
                        "a1234567", 3, 0));
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);

    }


    private static Stream<Arguments> provideDataFor_validatePasswords(){
        return Stream.of(
            Arguments.of("a1234562","a123456b","a123456b", new Result(false, R.SignUp.OLD_PASSWORD_IS_NOT_VALID)),
            Arguments.of("a1234567","a123456a","a1232456", new Result(false, R.SignUp.PASSWORDS_ARE_NOT_MATCHED)),
            Arguments.of("a1234567","a123456","a123456b", new Result(false, R.SignUp.PASSWORD_ONE_IS_NOT_EQUAL_TO_8)),
            Arguments.of("a1234567","a123456a","a123456b", new Result(false, R.SignUp.PASSWORDS_ARE_NOT_MATCHED)),
            Arguments.of("a1234567","a123456b","a123456b", new Result(true, R.OK))
        );
    }
}
