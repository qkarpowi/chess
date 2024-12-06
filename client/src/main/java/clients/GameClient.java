package clients;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthData;
import server.ServerFacade;
import ui.PrintBoard;

import java.util.Arrays;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class GameClient implements ConsoleClient{
    private final ServerFacade server;
    private int gameID;
    private final AuthData authData;
    private final ChessGame chessGame;

    public GameClient(ServerFacade facade, AuthData authData, int gameID) {
        server = facade;
        this.authData = authData;
        this.gameID = gameID;
        chessGame = initGame();
    }

    @Override
    public AuthData getAuthData() {
        return authData;
    }

    private ChessGame initGame(){
        try{
            var games = server.listGames(authData).games();
            for(var game: games){
                if(game.gameID() == gameID){
                    return game.game();
                }
            }
        } catch (ResponseException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException();
    }

    @Override
    public int getGameID() {
        return gameID;
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "help" -> help();
                case "exit" -> exit();
                case "quit" -> "quit";
                default -> PrintBoard.printBlackPerspective(chessGame.getBoard(), null) +
                        PrintBoard.printWhitePerspective(chessGame.getBoard(), null);
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String printGame(){
        return PrintBoard.printBlackPerspective(chessGame.getBoard(), null) +
                PrintBoard.printWhitePerspective(chessGame.getBoard(), null);
    }

    private String exit(){
        gameID = 0;
        return "Exiting Game";
    }

    @Override
    public String help() {
        return SET_TEXT_COLOR_BLUE + """
                    exit - the game
                    help - with possible commands""";
    }
}
