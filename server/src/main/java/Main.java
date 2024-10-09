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

        var server = new Server();
        server.run(8080);
    }
}