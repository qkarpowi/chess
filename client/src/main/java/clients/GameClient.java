package clients;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import exception.ResponseException;
import jdk.jshell.spi.ExecutionControl;
import model.AuthData;
import model.GameData;
import server.ServerFacade;
import ui.PrintBoard;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class GameClient implements ConsoleClient{
    private final ServerFacade server;
    private int gameID;
    private final AuthData authData;
    private boolean isPlayer = false;
    private ChessGame.TeamColor teamColor = null;
    private GameData chessGame;

    public GameClient(ServerFacade facade, AuthData authData, int gameID) {
        server = facade;
        this.authData = authData;
        this.gameID = gameID;
        chessGame = initGame();
        if(Objects.equals(authData.username(), chessGame.whiteUsername()) ){
            teamColor = ChessGame.TeamColor.WHITE;
            isPlayer = true;
        } else if (Objects.equals(authData.username(), chessGame.blackUsername())){
            teamColor = ChessGame.TeamColor.BLACK;
            isPlayer = true;
        }
    }

    @Override
    public AuthData getAuthData() {
        return authData;
    }

    private GameData initGame(){
        try{
            var games = server.listGames(authData).games();
            for(var game: games){
                if(game.gameID() == gameID){
                    return game;
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
            if(isPlayer){
                return switch (cmd) {
                    case "help" -> help();
                    case "leave" -> leave();
                    case "move" -> makemove(params);
                    case "resign" -> resign();
                    case "highlight" -> higlight(params);
                    case "redraw" -> printGame();
                    default -> help();
                };
            } else{
                return switch (cmd) {
                    case "help" -> help();
                    case "leave" -> leave();
                    case "redraw" -> printGame();
                    default -> help();
                };
            }

        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String resign() {
        return "butt";
    }

    private String makemove(String[] params) {
        return "butt";
    }

    public String printGame(){
        if(teamColor == ChessGame.TeamColor.BLACK){
            return PrintBoard.printBlackPerspective(chessGame.game().getBoard(), null);
        } else{
            return PrintBoard.printWhitePerspective(chessGame.game().getBoard(), null);
        }
    }

    private String higlight(String[] params){
        if (params.length == 1 && params[0].matches("[a-h][1-8]")) {
            ChessPosition position = new ChessPosition(params[0].charAt(1) - '0', params[0].charAt(0) - ('a'-1));
            if(teamColor == ChessGame.TeamColor.BLACK){
                Collection<ChessPosition> positions = chessGame.game().validMoves(position).stream().map(ChessMove::getEndPosition).collect(Collectors.toSet());
                return PrintBoard.printBlackPerspective(chessGame.game().getBoard(), positions);
            } else {
                Collection<ChessPosition> positions = chessGame.game().validMoves(position).stream().map(ChessMove::getEndPosition).collect(Collectors.toSet());
                return PrintBoard.printWhitePerspective(chessGame.game().getBoard(), positions);
            }
        }
        else {
            return "Please provide a coordinate (ex: 'a1')";
        }
    }

    private String leave(){
        gameID = 0;
        return "leaving Game";
    }

    @Override
    public String help() {
        return SET_TEXT_COLOR_BLUE + """
                    help - with possible commands
                    redraw - the chess board
                    leave - the chess game
                    move <start> <end> - a chess piece
                    resign - give up the game
                    highlight <position> - legal moves for a piece""";
    }
}
