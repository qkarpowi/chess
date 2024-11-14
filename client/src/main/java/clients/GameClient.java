package clients;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthData;
import server.ServerFacade;
import ui.PrintBoard;

public class GameClient implements ConsoleClient{
    private ServerFacade server;
    private int gameID;
    private AuthData authData;
    private ChessGame chessGame;

    public GameClient(ServerFacade facade, AuthData authData, int gameID) {
        server = facade;
        this.authData = authData;
        this.gameID = gameID;
        chessGame = initGame();
    }

    private ChessGame initGame(){
        try{
            var games = server.listGames().games();
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
    public AuthData getAuthData() {
        return authData;
    }

    @Override
    public int getGameID() {
        return gameID;
    }

    @Override
    public String eval(String input) {
        return PrintBoard.PrintBlackPerspective(chessGame.getBoard()) +
                PrintBoard.PrintWhitePerspective(chessGame.getBoard());
    }

    @Override
    public String help() {
        return "";
    }
}
