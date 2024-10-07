package service;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.*;

public class UserService {
    private UserDAO userDAO;
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    public AuthData register(String username, String password, String email){
        UserData data = new UserData(username, password, email);
        try{
            userDAO.getUser(username);
        } catch (Exception e){
            return null;
        }
        try{
            userDAO.createUser(data);
        } catch (Exception e){
            return null;
        }


        return new AuthData("", "");
    }

    public AuthData login(UserData user) {
        return new AuthData("", "");
    }

    public void logout(UserData user) {

    }
}
