package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import util.Result;

public class DatabaseService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;
    private final UserDAO userDAO;

    public DatabaseService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    public Result clear() {
        try{
            authDAO.clear();
            gameDAO.clear();
            userDAO.clear();
            return new Result<>(true, 200, null, null);
        } catch(Exception e){
            return new Result<>(false, 500, e.getMessage(), null);
        }
    }
}
