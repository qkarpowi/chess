package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.*;
import util.Result;

import java.util.Collection;

public class GameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public Result<GameList> getGames(String authtoken) {
        boolean isValid;
        try {
            isValid = authDAO.validateAuth(authtoken);
        } catch (Exception e) {
            return new Result<GameList>(false, 500, e.getMessage(), null);
        }
        if(!isValid) {
            return new Result<GameList>(false, 401, "unauthorized", null);
        }
        Collection<GameData> gameData;
        try{
            gameData = gameDAO.getAllGames();
        } catch (Exception e) {
            return new Result<GameList>(false, 500, e.getMessage(), null);
        }

        return new Result<GameList>(true, 200, "success", new GameList(gameData));
    }

    public Result<GameID> createGame(String authtoken, String gameName) {
        //Validate user
        boolean isValid;
        try {
            isValid = authDAO.validateAuth(authtoken);
        } catch (Exception e) {
            return new Result<GameID>(false, 500, e.getMessage(), null);
        }
        if(!isValid) {
            return new Result<GameID>(false, 401, "unauthorized", null);
        }

        //validate game data
        if(gameName == null || gameName.isEmpty()) {
            return new Result<GameID>(false, 400, "bad request", null);
        }

        //create game
        GameData gameData;
        try {
            gameData = gameDAO.createGame(null, null, gameName, new ChessGame());
        } catch (Exception e) {
            return new Result<GameID>(false, 500, e.getMessage(), null);
        }

        //Success
        return new Result<GameID>(true, 200, "success", new GameID(gameData.gameID()));
    }

    public Result joinGame(String authtoken, ChessGame.TeamColor teamColor, Integer gameId) {
        //Validate user
        boolean isValid;
        try {
            isValid = authDAO.validateAuth(authtoken);
        } catch (Exception e) {
            return new Result<>(false, 500, e.getMessage(), null);
        }
        if(!isValid) {
            return new Result<>(false, 401, "unauthorized", null);
        }

        //validate request data
        if(gameId == null || gameId < 0 || teamColor == null){
            return new Result<>(false, 400, "bad request", null);
        }

        //get username
        String username;
        try {
            username = authDAO.getAuth(authtoken).username();
        } catch (Exception e) {
            return new Result<>(false, 500, e.getMessage(), null);
        }
        if(username == null || username.isEmpty()) {
            return new Result<>(false, 400, "bad request", null);
        }

        //get game
        GameData gameData;
        try{
            gameData = gameDAO.getGame(gameId);
        } catch (Exception e) {
            return new Result<>(false, 500, e.getMessage(), null);
        }
        if(gameData == null) {
            return new Result<>(false, 400, "bad request", null);
        }

        //validate team not taken
        if (teamColor == ChessGame.TeamColor.BLACK) {
            if(gameData.blackUsername() != null){
                return new Result<>(false, 403, "already taken", null);
            }
            gameData  = new GameData(gameData.gameID(), gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
        } else {
            if(gameData.whiteUsername() != null){
                return new Result<>(false, 403, "already taken", null);
            }
            gameData  = new GameData(gameData.gameID(), username, gameData.blackUsername(), gameData.gameName(), gameData.game());
        }

        try {
            gameDAO.updateGame(gameData);
            return new Result<>(true, 200, "success", null);
        } catch (Exception e) {
            return new Result<>(false, 500, e.getMessage(), null);
        }
    }

    public GameData getGame(Integer gameId) throws DataAccessException {
        return gameDAO.getGame(gameId);
    }
}
