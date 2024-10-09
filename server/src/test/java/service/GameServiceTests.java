package service;
import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameServiceTests {
    private static GameService gameService;
    private static UserDAO userDao;
    private static AuthDAO authDao;
    private static GameDAO gameDao;
    private static UserData userData;
    private static AuthData authData;

    @BeforeAll
    public static void init() {
        authDao = new MemoryAuthDAO();
        userDao = new MemoryUserDAO();
        gameDao = new MemoryGameDAO();
        gameService = new GameService(authDao, gameDao);
        UserService userService = new UserService(userDao, authDao);
        userData = new UserData("username", "password", "email");
        authData = userService.register(userData).getData();
    }

    @Test
    @Order(1)
    @DisplayName("getGames Positive Test")
    public void getGamesSuccess() throws Exception {
        var result = gameService.getGames(authData.authToken());
        Assertions.assertTrue(result.isSuccess());
        Assertions.assertTrue(result.getData().games().isEmpty());
    }

    @Test
    @Order(2)
    @DisplayName("getGames user Fail")
    public void getGamesUserFail() throws Exception {
        var result = gameService.getGames(null);
        Assertions.assertFalse(result.isSuccess());
    }

    @Test
    @Order(3)
    @DisplayName("createGame positive test")
    public void createGameSuccess() throws Exception {
        var result = gameService.createGame(authData.authToken(), "game name");
        Assertions.assertTrue(result.isSuccess());
        Assertions.assertEquals(result.getData().gameID(), 1);
    }

    @Test
    @Order(4)
    @DisplayName("createGame user fail")
    public void createGameFail() throws Exception {
        var result = gameService.createGame(authData.authToken(), null);
        Assertions.assertFalse(result.isSuccess());
    }

    @Test
    @Order(5)
    @DisplayName("joinGame user success")
    public void joinGameSuccess() throws Exception {
        var gameID = gameService.createGame(authData.authToken(), "game for joining").getData().gameID();
        var result = gameService.joinGame(authData.authToken(), ChessGame.TeamColor.BLACK, gameID);

        Assertions.assertTrue(result.isSuccess());

        Assertions.assertEquals(gameDao.getGame(gameID).blackUsername(), userData.username());
    }

    @Test
    @Order(6)
    @DisplayName("joinGame user Fail")
    public void joinGameFail() throws Exception {
        var result = gameService.joinGame(null, ChessGame.TeamColor.BLACK, 1);

        Assertions.assertFalse(result.isSuccess());
    }
}
