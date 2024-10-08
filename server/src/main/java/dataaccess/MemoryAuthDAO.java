package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    final private HashMap<String, AuthData> auths = new HashMap<>();

    public AuthData createAuth(AuthData authData) throws DataAccessException{
        auths.put(authData.username(), authData);
        return authData;
    }

    public void deleteAuth(String username)throws DataAccessException{
        auths.remove(username);
    }

    public boolean validateAuth(AuthData authData)throws DataAccessException{
        AuthData userAuth = auths.get(authData.username());
        return !(userAuth == null) && (authData.authToken().equals(userAuth.authToken()));
    }

    public void clear()throws DataAccessException{
        this.auths.clear();
    }
}
