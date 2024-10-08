package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void clear();

    Collection<GameData> getAllGames();

    GameData getGame(int id);

    GameData createGame(GameData game);
}
