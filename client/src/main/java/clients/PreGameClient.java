package clients;

import exception.ResponseException;
import model.AuthData;
import server.ServerFacade;

import java.util.Arrays;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class PreGameClient implements ConsoleClient{
    private final ServerFacade server;
    private final String serverUrl;
    private int gameID;
    private AuthData authData;

    public PreGameClient(String serverUrl, AuthData authData) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
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
        return "";
    }
    private String listGames() throws ResponseException {
        return "";
    }
    private String joinGame(String... params) throws ResponseException {
        return "";
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
                    help - with possible commands
                    """;
    }
}
