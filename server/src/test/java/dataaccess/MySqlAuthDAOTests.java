package dataaccess;
import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import service.UserService;

import java.sql.SQLException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MySqlAuthDAOTests {
    private static AuthDAO authDao;

    @BeforeAll
    public static void init() throws Exception {
        authDao = new MySqlAuthDAO();
    }

    @Test
    @Order(1)
    @DisplayName("createAuth Positive Test")
    public void createAuthSuccess() throws Exception {
        AuthData authData = new AuthData("authToken", "username");
        authDao.createAuth(authData);

        Assertions.assertEquals(authDao.getAuth(authData.authToken()), authData);

        authDao.deleteAuth(authData.authToken());
    }
    @Test
    @Order(2)
    @DisplayName("createAuth Fail Test")
    public void createAuthFail() throws Exception {
        String longString = "This is a very long string that goes on for more than 100 characters. "
                + "This is a very long string that goes on for more than 100 characters. "
                + "This is a very long string that goes on for more than 100 characters. "
                + "This is a very long string that goes on for more than 100 characters. ";
        Assertions.assertThrows(DataAccessException.class, () -> authDao.createAuth(new AuthData("authToken", longString)));
    }
    @Test
    @Order(3)
    @DisplayName("deleteAuth Positive Test")
    public void deleteAuthSuccess() throws Exception {
        authDao.createAuth(new AuthData("authToken", "username"));

        authDao.deleteAuth("authToken");

        Assertions.assertNull(authDao.getAuth("authToken"));
    }
    @Test
    @Order(4)
    @DisplayName("deleteAuth Fail Test")
    public void deleteAuthFail() {
        Assertions.assertThrows(DataAccessException.class, () -> authDao.deleteAuth(null));
    }
    @Test
    @Order(5)
    @DisplayName("validateAuth Positive Test")
    public void validateAuthSuccess() throws Exception {
        authDao.createAuth(new AuthData("authToken", "username"));

        Assertions.assertTrue(authDao.validateAuth("authToken"));

        authDao.deleteAuth("authToken");
    }
    @Test
    @Order(6)
    @DisplayName("validateAuth Fail Test")
    public void validateAuthFail() throws Exception {
        Assertions.assertFalse(authDao.validateAuth(null));
    }
    @Test
    @Order(7)
    @DisplayName("getAuth Positive Test")
    public void getAuthSuccess() throws Exception {
        AuthData authData = new AuthData("authToken", "username");
        authDao.createAuth(authData);

        Assertions.assertEquals(authDao.getAuth(authData.authToken()), authData);

        authDao.deleteAuth(authData.authToken());
    }
    @Test
    @Order(8)
    @DisplayName("getAuth Fail Test")
    public void getAuthFail() throws Exception {
        Assertions.assertNull(authDao.getAuth("Authtoken that doesn't exist"));
    }
    @Test
    @Order(9)
    @DisplayName("clear Positive Test")
    public void clearSuccess() throws Exception {
        AuthData authData = new AuthData("authToken", "username");
        authDao.createAuth(authData);

        authDao.clear();

        Assertions.assertNull(authDao.getAuth("authToken"));
    }
}
