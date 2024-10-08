package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class DatabaseService {
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private UserDAO userDAO;

    public DatabaseService(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    public void clear() throws DataAccessException {
        try{
            authDAO.clear();
            gameDAO.clear();
            userDAO.clear();
            throw new RuntimeException("Testing");
        } catch(Exception e){
            throw e;
        }
    }
}
