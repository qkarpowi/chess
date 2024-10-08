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

        var userService = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
        var gameService = new GameService(new MemoryAuthDAO(), new MemoryGameDAO());
        var databaseService = new DatabaseService(new MemoryAuthDAO(), new MemoryUserDAO(), new MemoryGameDAO());

        var server = new Server(userService, databaseService, gameService);
        server.run(8080);
    }
}