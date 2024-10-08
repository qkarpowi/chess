package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.*;

import java.util.Collection;

public class GameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public Collection<GameData> getGames(String authtoken) throws Exception {
        if(!authDAO.validateAuth(authtoken)){
            throw new Exception("Unauthorized access");
        }

        return gameDAO.getAllGames();
    }

    public GameData createGame(String authtoken, String gameName) throws Exception {
        if(!authDAO.validateAuth(authtoken)){
            throw new Exception("Unauthorized access");
        }

        return gameDAO.createGame(null, null, gameName, new ChessGame());
    }

    public void joinGame(String authtoken, ChessGame.TeamColor teamColor, Integer gameId) throws Exception {
        if(!authDAO.validateAuth(authtoken)){
            throw new Exception("Unauthorized access");
        }

        String username = authDAO.getAuth(authtoken).username();

        var gameData = gameDAO.getGame(gameId);

        if (teamColor == ChessGame.TeamColor.BLACK) {
            if(gameData.blackUsername() != null){
                throw new Exception("already taken");
            }
            gameData  = new GameData(gameData.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
            gameDAO.updateGame(gameData);
        } else {
            if(gameData.whiteUsername() != null){
                throw new Exception("already taken");
            }
            gameData  = new GameData(gameData.gameID(), username, gameData.blackUsername(), gameData.gameName(), gameData.game());
            gameDAO.updateGame(gameData);
        }
    }
}
