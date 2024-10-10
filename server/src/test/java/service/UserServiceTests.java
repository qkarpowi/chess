package service;
import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTests {
    private static UserService userService;
    private static UserDAO userDao;
    private static AuthDAO authDao;
    private static UserData userData;

    @BeforeAll
    public static void init() {
        authDao = new MemoryAuthDAO();
        userDao = new MemoryUserDAO();
        userService = new UserService(userDao, authDao);
        userData = new UserData("username1", "password1", "email");
    }

    @Test
    @Order(1)
    @DisplayName("register Positive Test")
    public void registerSuccess() throws Exception {
        var result = userService.register(userData);

        Assertions.assertTrue(result.isSuccess());

        var retrievedUserData = userDao.getUser(userData.username());
        Assertions.assertEquals(userData.username(), retrievedUserData.username());
    }

    @Test
    @Order(2)
    @DisplayName("register user Fail")
    public void registerUserFail() throws Exception {
        var userData = new UserData("", null, "email");
        var results = userService.register(userData);

        Assertions.assertFalse(results.isSuccess());
    }

    @Test
    @Order(3)
    @DisplayName("login positive test")
    public void loginSuccess() throws Exception {
        var result = userService.login(userData.username(), userData.password());
        Assertions.assertTrue(result.isSuccess());
    }

    @Test
    @Order(4)
    @DisplayName("login user fail")
    public void loginFail() throws Exception {
        var result = userService.login(userData.username(), null);

        Assertions.assertFalse(result.isSuccess());
    }

    @Test
    @Order(5)
    @DisplayName("logout user success")
    public void logoutSuccess() throws Exception {
        var authData = userService.login(userData.username(), userData.password());
        Assertions.assertTrue(authData.isSuccess());

        var result = userService.logout(authData.getData().authToken());

        Assertions.assertTrue(result.isSuccess());
        Assertions.assertNull(authDao.getAuth(authData.getData().authToken()));
    }

    @Test
    @Order(6)
    @DisplayName("logout user Fail")
    public void logoutFail() throws Exception {
        var result = userService.logout(null);
        Assertions.assertFalse(result.isSuccess());
    }
}
