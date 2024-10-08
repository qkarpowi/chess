package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.*;

import java.util.Collection;

public class GameService {
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public Collection<GameData> getGames(String authtoken){
        return null;
    }

    public GameData createGame(String username){
        return null;
    }

    public GameData joinGame(String username, Integer gameId){
        return null;
    }
}
