package service;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.*;

public class UserService {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
    public AuthData register(UserData userData) throws DataAccessException {
        try{
            userDAO.getUser(userData.username());
        } catch (Exception e){
            return null;
        }
        try{
            userDAO.createUser(userData);
        } catch (Exception e){
            return null;
        }
        AuthData authtoken = new AuthData("0000", userData.username());
        try {
            authtoken = authDAO.createAuth(authtoken);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return authtoken;
    }

    public AuthData login(UserData user) {

        return new AuthData("", "");
    }

    public void logout(UserData user) {

    }
}
