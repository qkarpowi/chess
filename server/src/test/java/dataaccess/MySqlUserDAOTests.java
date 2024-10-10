package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MySqlUserDAOTests {
    private static UserDAO userDAO;

    @BeforeAll
    public static void init() throws Exception {
        userDAO = new MySqlUserDAO();
    }

    @Test
    @Order(1)
    @DisplayName("createUser Positive Test")
    public void createUserSuccess() throws Exception {
        UserData userData = new UserData("username3", "pasword3", "email3");
        userDAO.createUser(userData);

        Assertions.assertEquals(userDAO.getUser(userData.username()), userData);

        userDAO.clear();
    }
    @Test
    @Order(2)
    @DisplayName("createUser Fail Test")
    public void createUserFail() throws Exception {
        String longString = "This is a very long string that goes on for more than 100 characters. "
                + "This is a very long string that goes on for more than 100 characters. "
                + "This is a very long string that goes on for more than 100 characters. "
                + "This is a very long string that goes on for more than 100 characters. ";
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.createUser(new UserData(longString, "password2", "email")));
    }
    @Test
    @Order(3)
    @DisplayName("getUser Positive Test")
    public void getUserSuccess() throws Exception {
        UserData userData = new UserData("username2", "pasword2", "email");
        userDAO.createUser(userData);
        Assertions.assertEquals(userDAO.getUser(userData.username()), userData);
        userDAO.clear();
    }
    @Test
    @Order(4)
    @DisplayName("getUser Fail Test")
    public void getUserFail() throws Exception {
        Assertions.assertNull(userDAO.getUser("null"));
    }
    @Test
    @Order(5)
    @DisplayName("clear table Test")
    public void clearTableSuccess() throws Exception {
        UserData userData = new UserData("username2", "pasword2", "email");
        userDAO.createUser(userData);

        userDAO.clear();

        Assertions.assertNull(userDAO.getUser(userData.username()));

    }
}
