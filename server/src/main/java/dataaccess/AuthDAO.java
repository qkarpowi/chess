package dataaccess;

import model.AuthData;

public interface AuthDAO {
    AuthData createAuth(AuthData authData)throws DataAccessException;

    void deleteAuth(String username)throws DataAccessException;

    boolean validateAuth(AuthData authData)throws DataAccessException;

    void clear()throws DataAccessException;
}
