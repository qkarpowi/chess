package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO {
    final private HashMap<String, AuthData> auths = new HashMap<>();

    public AuthData createAuth(AuthData authData) throws DataAccessException{
        auths.put(authData.authToken(), authData);
        return authData;
    }

    public void deleteAuth(String authToken)throws DataAccessException{
        auths.remove(authToken);
    }

    public boolean validateAuth(String authToken)throws DataAccessException{
        AuthData userAuth = auths.get(authToken);
        return !(userAuth == null) && (authToken.equals(userAuth.authToken()));
    }

    public AuthData getAuth(String authToken)throws DataAccessException{
        return auths.get(authToken);
    }

    public void clear()throws DataAccessException{
        this.auths.clear();
    }
}
