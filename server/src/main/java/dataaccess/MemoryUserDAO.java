package dataaccess;

import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    final private HashMap<String, UserData> users = new HashMap<>();
    public MemoryUserDAO() {}

    public void clear() throws DataAccessException {
        users.clear();
    }

    public UserData createUser(UserData user) throws DataAccessException {
        users.put(user.username(), user);
        return user;
    }

    public UserData getUser(String username) throws DataAccessException {
        return users.get(username);
    }
}
