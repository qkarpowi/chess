package clients;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthData;
import model.GameCreate;
import model.GameData;
import model.JoinGame;
import server.ServerFacade;

import java.util.Arrays;
import java.util.Objects;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class PreGameClient implements ConsoleClient{
    private final ServerFacade server;
    private int gameID;
    private AuthData authData;

    public PreGameClient(ServerFacade facade, AuthData authData) {
        server = facade;
        this.authData = authData;
        gameID = 0;
    }

    @Override
    public int getGameID(){
        return gameID;
    }

    @Override
    public AuthData getAuthData() {
        return authData;
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "logout" -> logout();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }
    private String createGame(String... params) throws ResponseException {
        if(params.length == 1){
            server.createGame(new GameCreate(params[0]));
            return "Game " + params[0] + " created successfully";
        }
        throw new ResponseException(400, "Expected: <name>");
    }
    private String listGames() throws ResponseException {
        var gameData = server.listGames().games();
        StringBuilder output = new StringBuilder();
        if(gameData.isEmpty()) {
            output.append("No games currently");
        } else {
            GameData[] gameArray = gameData.toArray(new GameData[0]); // Specify the type to get a Game[] array
            for (int i = 0; i < gameArray.length; i++) {
                GameData data = gameArray[i]; // Store the current game item in 'data' for easy access
                output.append(i + 1).append(" -")
                        .append(" Name: ").append(data.gameName())
                        .append(" White Username: ").append(data.whiteUsername())
                        .append(" Black Username: ").append(data.blackUsername())
                        .append("\n");
            }
        }
        return output.toString();
    }
    private String joinGame(String... params) throws ResponseException {
        if(params.length == 2){
            int gameIdToJoin = Integer.parseInt(params[0]) - 1;
            var gameData = server.listGames().games().toArray(new GameData[0]);
            gameIdToJoin = gameData[gameIdToJoin].gameID();
            if(Objects.equals(params[1].toLowerCase(), "white")){
                server.joinGame(new JoinGame(ChessGame.TeamColor.WHITE, gameIdToJoin));
                gameID = gameIdToJoin;
                return "Game " + gameIdToJoin + " joined successfully";
            } else if (Objects.equals(params[1].toLowerCase(), "black")){
                server.joinGame(new JoinGame(ChessGame.TeamColor.BLACK, gameIdToJoin));
                gameID = gameIdToJoin;
                return "Game " + gameIdToJoin + " joined successfully";
            }
        }
        throw new ResponseException(400, "Expected: <ID> [WHITE|BLACK]");
    }
    private String observeGame(String... params) throws ResponseException {
        return "";
    }
    private String logout() throws ResponseException {
        server.logout();
        authData = null;
        return "You have logged out successfully";
    }

    @Override
    public String help() {
        return SET_TEXT_COLOR_BLUE + """
                    create <name> - a game
                    list - games
                    join <ID> [WHITE|BLACK] - a game
                    observe <ID> - a game
                    logout - when you are done
                    quit - playing chess
                    help - with possible commands""";
    }
}
