package dataaccess;

import model.GameData;
import java.util.Collection;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    final private HashMap<Integer, GameData> games = new HashMap<>();
    private Integer nextId = 1;

    @Override
    public void clear(){
        games.clear();
    }

    @Override
    public Collection<GameData> getAllGames(){
        return games.values();
    }

    @Override
    public GameData getGame(int id){
        return games.get(id);
    }

    @Override
    public GameData createGame(GameData game){
        games.put(nextId++, game);
        return game;
    }
}
