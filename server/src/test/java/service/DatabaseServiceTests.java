package service;
import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import passoff.model.*;
import server.Server;
import service.DatabaseService;
import service.GameService;
import service.UserService;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DatabaseServiceTests {
    private static DatabaseService databaseService;
    private static GameDAO gameDao;
    private static UserDAO userDao;
    private static AuthDAO authDao;

    @BeforeAll
    public static void init() {
        gameDao = new MemoryGameDAO();
        authDao = new MemoryAuthDAO();
        userDao = new MemoryUserDAO();

        databaseService = new DatabaseService(authDao, userDao, gameDao);
    }
    @Test
    @Order(1)
    @DisplayName("clear database Test")
    public void clearDatabase() throws Exception {
        userDao.createUser(new UserData("username", "password", "email"));
        GameData data = gameDao.createGame("", "", "game", new ChessGame());
        authDao.createAuth(new AuthData("authtoken", "username"));

        databaseService.clear();

        Assertions.assertNull(userDao.getUser("username"));
        Assertions.assertNull(authDao.getAuth("authtoken"));
        Assertions.assertNull(gameDao.getGame(data.gameID()));
    }
}
