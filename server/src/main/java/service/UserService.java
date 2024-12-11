package service;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.*;
import org.mindrot.jbcrypt.BCrypt;
import util.*;

import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
    public Result<AuthData> register(UserData userData) {
        //Check data integrity
        if(userData.username() == null || userData.password() == null || userData.username().isEmpty() || userData.password().isEmpty()){
            return new Result<AuthData>(false, 400, "bad request", null);
        }

        //Check if user exists
        UserData user;
        try{
            user = userDAO.getUser(userData.username());
        } catch (Exception e){
            return new Result<AuthData>(false, 500, e.getMessage(), null);
        }
        if(user != null){
            return new Result<AuthData>(false, 403, "already taken", null);
        }

        String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());

        //create User
        try{
            userDAO.createUser(new UserData(userData.username(), hashedPassword, userData.email()));
        } catch (Exception e){
            return new Result<AuthData>(false, 500, e.getMessage(), null);
        }

        //Create Auth token
        AuthData authtoken = new AuthData(UUID.randomUUID().toString(), userData.username());
        try {
            authtoken = authDAO.createAuth(authtoken);
        } catch (DataAccessException e) {
            return new Result<AuthData>(false, 500, e.getMessage(), null);
        }

        //Success
        return new Result<AuthData>(true, 200, "created", authtoken);
    }

    public Result<AuthData> login(String username, String password) {
        //Check data integrity
        if(username == null || username.isEmpty() || password == null || password.isEmpty()){
            return new Result<AuthData>(false, 400, "bad request", null);
        }

        //Check if user exists
        UserData user;
        try{
            user = userDAO.getUser(username);
        } catch (Exception e){
            return new Result<AuthData>(false, 500, e.getMessage(), null);
        }

        //check that password matches
        if( user == null || !BCrypt.checkpw(password, user.password())){
            return new Result<AuthData>(false, 401, "unauthorized", null);
        }

        //create auth data
        AuthData authtoken;
        try{
            authtoken = authDAO.createAuth(new AuthData(UUID.randomUUID().toString(), username));
        }catch(DataAccessException e){
            return new Result<AuthData>(false, 500, e.getMessage(), null);
        }

        //Success
        return new Result<AuthData>(true, 200, "success", authtoken);
    }

    public Result logout(String authtoken) {
        AuthData data;

        try{
            data = authDAO.getAuth(authtoken);
        } catch (DataAccessException e) {
            return new Result<>(false, 500, e.getMessage(), null);
        }

        if(data == null){
            return new Result<>(false, 401, "unauthorized", null);
        }

        try {
            authDAO.deleteAuth(authtoken);
        } catch (DataAccessException e) {
            return new Result<>(false, 500, e.getMessage(), null);
        }

        return new Result<>(true, 200, "success", null);
    }

    public AuthData getAuthData(String authtoken) throws DataAccessException {
        return authDAO.getAuth(authtoken);
    }
}
