package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void clear() throws DataAccessException;

    Collection<GameData> getAllGames() throws DataAccessException;

    GameData getGame(int id) throws DataAccessException;

    GameData createGame(String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException;

    GameData updateGame(GameData gameData)  throws DataAccessException;
}
