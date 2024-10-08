package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void clear();

    Collection<GameData> getAllGames();

    GameData getGame(int id);

    GameData createGame(String whiteUsername, String blackUsername, String gameName, ChessGame game);

    GameData updateGame(GameData gameData);
}
