package dataaccess;

import org.junit.jupiter.api.*;

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
    }
    @Test
    @Order(2)
    @DisplayName("createUser Fail Test")
    public void createUserFail() throws Exception {

    }
    @Test
    @Order(3)
    @DisplayName("getUser Positive Test")
    public void getUserSuccess() throws Exception {

    }
    @Test
    @Order(4)
    @DisplayName("getUser Fail Test")
    public void getUserFail() {
    }
    @Test
    @Order(5)
    @DisplayName("clear table Test")
    public void clearTableSuccess() throws Exception {
    }
}
