package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MySqlGameDAOTests {
    private static GameDAO gameDAO;

    @BeforeAll
    public static void init() throws Exception {
        gameDAO = new MySqlGameDAO();
    }
    @Test
    @Order(1)
    @DisplayName("updateGame Positive Test")
    public void updateGameSuccess() throws Exception {
        GameData gameData = gameDAO.createGame(null, null, "gameName", new ChessGame());

        GameData newGameData = new GameData(gameData.gameID(), "NewUsername", null, gameData.gameName(), gameData.game());

        gameData = gameDAO.updateGame(newGameData);

        Assertions.assertEquals(gameData.gameID(), newGameData.gameID());

        gameDAO.clear();
    }
    @Test
    @Order(2)
    @DisplayName("updateGame Fail Test")
    public void updateGameFail() throws Exception {
        GameData gameData = gameDAO.createGame(null, null, "gameName", new ChessGame());

        GameData newGameData = new GameData(gameData.gameID(), null, null, null, null);

        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.updateGame(newGameData));

        gameDAO.clear();
    }
    @Test
    @Order(3)
    @DisplayName("createGame Positive Test")
    public void createGameSuccess() throws Exception {
        GameData gameData = gameDAO.createGame(null, null, "gameName", new ChessGame());

        Assertions.assertEquals(gameData.gameID(), gameDAO.getGame(gameData.gameID()).gameID());

        gameDAO.clear();
    }
    @Test
    @Order(4)
    @DisplayName("createGame Fail Test")
    public void createGameFail() throws Exception {
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.createGame(null, null, null, null));
        gameDAO.clear();
    }
    @Test
    @Order(5)
    @DisplayName("getGame Positive Test")
    public void getGameSuccess() throws Exception {
        GameData gameData = gameDAO.createGame("null", "null", "gameName", new ChessGame());

        Assertions.assertEquals(gameData.gameName(), gameDAO.getGame(gameData.gameID()).gameName());

        gameDAO.clear();
    }
    @Test
    @Order(6)
    @DisplayName("getGame Fail Test")
    public void getGameFail() throws Exception {
        Assertions.assertNull(gameDAO.getGame(1));

        gameDAO.clear();
    }
    @Test
    @Order(7)
    @DisplayName("getAllGames Positive Test")
    public void getAllGamesuccess() throws Exception {
        gameDAO.createGame(null, null, "gameName", new ChessGame());

        Assertions.assertEquals(1, gameDAO.getAllGames().size());

        gameDAO.clear();
    }
    @Test
    @Order(8)
    @DisplayName("getAllGames Fail Test")
    public void getAllGamesFail() throws Exception {
        gameDAO.getAllGames();

        Assertions.assertEquals(0, gameDAO.getAllGames().size());

        gameDAO.clear();
    }
    @Test
    @Order(9)
    @DisplayName("clear Positive Test")
    public void clearSuccess() throws Exception {
        gameDAO.createGame(null, null, "gameName", new ChessGame());

        gameDAO.clear();

        Assertions.assertEquals(0, gameDAO.getAllGames().size());
    }

}
