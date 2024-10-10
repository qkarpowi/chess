package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class MySqlGameDAO implements GameDAO {
    public MySqlGameDAO() throws DataAccessException {
        configureDatabase();
    }

    public void clear() throws DataAccessException {
        try (var conn=DatabaseManager.getConnection()) {
            try (var preparedStatement=conn.prepareStatement("DELETE FROM Game;")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("unable to drop table");
        }
    }

    public Collection<GameData> getAllGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT GameID, WhiteUsername, BlackUsername, GameName, Game FROM Game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(new GameData(rs.getInt("GameID"),
                                rs.getString("WhiteUsername"),
                                rs.getString("BlackUsername"),
                                rs.getString("GameName"),
                                (new Gson().fromJson(rs.getString("Game"), ChessGame.class))));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    public GameData getGame(int id) throws DataAccessException {
        try (var conn=DatabaseManager.getConnection()) {
            var statement="SELECT GameID, WhiteUsername, BlackUsername, GameName, Game FROM Game WHERE GameID=?";
            try (var ps=conn.prepareStatement(statement)) {
                ps.setInt(1, id);
                try (var rs=ps.executeQuery()) {
                    if (rs.next()) {
                        return new GameData(rs.getInt("GameID"),
                                rs.getString("WhiteUsername"),
                                rs.getString("BlackUsername"),
                                rs.getString("GameName"),
                                (new Gson().fromJson(rs.getString("Game"), ChessGame.class)));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public GameData createGame(String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException {
        try (var conn=DatabaseManager.getConnection()) {
            try (var preparedStatement=conn.prepareStatement(
                    "INSERT INTO Game (WhiteUsername, BlackUsername, GameName, Game) VALUES(?, ?, ?, ?)")) {
                preparedStatement.setString(1, whiteUsername);
                preparedStatement.setString(2, blackUsername);
                preparedStatement.setString(3, gameName);
                var json = new Gson().toJson(game);
                preparedStatement.setString(4, json);

                var id = preparedStatement.executeUpdate();
                return new GameData(id, whiteUsername, blackUsername, gameName, game);
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to insert data: %s", e.getMessage()));
        }
    }

    public GameData updateGame(GameData gameData) throws DataAccessException {
        try (var conn=DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(
                    "UPDATE Game SET WhiteUsername=?, BlackUsername=?, GameName=?, Game=? WHERE GameID=?")) {
                preparedStatement.setString(1, gameData.whiteUsername());
                preparedStatement.setString(2, gameData.blackUsername());
                preparedStatement.setString(3, gameData.gameName());
                preparedStatement.setString(4, new Gson().toJson(gameData.game()));
                preparedStatement.setInt(5, gameData.gameID());

                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to update data: %s", e.getMessage()));
        }
        return gameData;
    }

    private final String[] createStatements={
            """
            CREATE TABLE IF NOT EXISTS `Game` (
                 `GameID` int NOT NULL AUTO_INCREMENT,
                 `WhiteUsername` varchar(100) DEFAULT NULL,
                 `BlackUsername` varchar(100) DEFAULT NULL,
                 `GameName` varchar(100) DEFAULT NULL,
                 `Game` json DEFAULT NULL,
                 PRIMARY KEY (`GameID`),
                 KEY `fk_whiteusername_idx` (`WhiteUsername`),
                 KEY `fk_blackusername_idx` (`BlackUsername`),
                 CONSTRAINT `fk_whiteusername` FOREIGN KEY (`WhiteUsername`) REFERENCES `User` (`Username`),
                 CONSTRAINT `fk_blackusername` FOREIGN KEY (`BlackUsername`) REFERENCES `User` (`Username`)
               ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn=DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement=conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException | DataAccessException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
