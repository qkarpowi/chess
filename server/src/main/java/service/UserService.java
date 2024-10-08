package service;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.*;

import java.util.UUID;

public class UserService {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
    public AuthData register(UserData userData) throws DataAccessException {
        UserData user;
        try{
            user = userDAO.getUser(userData.username());
        } catch (Exception e){
            return null;
        }
        if(user != null){
            throw new DataAccessException("Username already exists");
        }

        try{
            userDAO.createUser(userData);
        } catch (Exception e){
            return null;
        }

        AuthData authtoken = new AuthData(UUID.randomUUID().toString(), userData.username());
        try {
            authtoken = authDAO.createAuth(authtoken);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        return authtoken;
    }

    public AuthData login(String username, String password) throws Exception{
        var userData = userDAO.getUser(username);
        if(userData == null || !userData.password().equals(password)){
            throw new DataAccessException("Invalid username or password");
        }
        return authDAO.createAuth(new AuthData(UUID.randomUUID().toString(), username));
    }

    public void logout(String authtoken) throws Exception {
        if(authDAO.getAuth(authtoken) == null){
            throw new DataAccessException("Invalid authtoken");
        }
        authDAO.deleteAuth(authtoken);
    }
}
