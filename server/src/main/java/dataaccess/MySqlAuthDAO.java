package dataaccess;

import model.AuthData;

import java.sql.SQLException;

public class MySqlAuthDAO implements AuthDAO {
    public MySqlAuthDAO() throws DataAccessException {
        configureDatabase();
    }

    public void clear() throws DataAccessException {
        try (var conn=DatabaseManager.getConnection()) {
            try (var preparedStatement=conn.prepareStatement("DELETE FROM Authentication;")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("unable to drop table");
        }
    }

    public AuthData createAuth(AuthData authData) throws DataAccessException {
        try (var conn=DatabaseManager.getConnection()) {
            try (var preparedStatement=conn.prepareStatement("INSERT INTO Authentication (Authtoken, Username) VALUES(?, ?)")) {
                preparedStatement.setString(1, authData.authToken());
                preparedStatement.setString(2, authData.username());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("unable to insert values");
        }
        return authData;
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        if(authToken==null){
            throw new DataAccessException("authToken is null");
        }
        try (var conn=DatabaseManager.getConnection()) {
            try (var preparedStatement=conn.prepareStatement("DELETE FROM Authentication WHERE Authtoken=?")) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to delete data: %s", e.getMessage()));
        }
    }

    public boolean validateAuth(String authToken) throws DataAccessException {
        AuthData userAuth=this.getAuth(authToken);
        return !(userAuth == null) && (authToken.equals(userAuth.authToken()));
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn=DatabaseManager.getConnection()) {
            var statement="SELECT Authtoken, Username FROM Authentication WHERE Authtoken=?";
            try (var ps=conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs=ps.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(rs.getString("Authtoken"), rs.getString("Username"));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    private final String[] createStatements={
            """
            CREATE TABLE IF NOT EXISTS `Authentication` (
                 `Authtoken` varchar(100) NOT NULL,
                 `Username` varchar(100) DEFAULT NULL,
                 PRIMARY KEY (`Authtoken`)
               ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn=DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement=conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException | DataAccessException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
