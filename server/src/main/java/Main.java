import chess.*;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import server.*;
import service.DatabaseService;
import service.GameService;
import service.UserService;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess server.Server: " + piece);

        var gameDAO = new MemoryGameDAO();
        var authDAO = new MemoryAuthDAO();
        var userDAO = new MemoryUserDAO();

        var userService = new UserService(userDAO, authDAO);
        var gameService = new GameService(authDAO, gameDAO);
        var databaseService = new DatabaseService(authDAO, userDAO, gameDAO);

        var server = new Server(userService, databaseService, gameService);
        server.run(8080);
    }
}