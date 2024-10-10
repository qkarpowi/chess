package dataaccess;

import chess.ChessGame;
import model.GameData;
import java.util.Collection;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    final private HashMap<Integer, GameData> games = new HashMap<>();
    private Integer nextId = 1;

    public void clear(){
        games.clear();
    }

    public Collection<GameData> getAllGames(){
        return games.values();
    }

    public GameData getGame(int id){
        return games.get(id);
    }

    public GameData createGame(String whiteUsername, String blackUsername, String gameName, ChessGame game){
        var gameData = new GameData(nextId++, whiteUsername, blackUsername, gameName, game);
        games.put(gameData.gameID(), gameData);
        return gameData;
    }

    public GameData updateGame(GameData gameData){
        games.put(gameData.gameID(), gameData);
        return gameData;
    }
}
