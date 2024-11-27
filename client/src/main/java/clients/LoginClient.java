package clients;

import exception.ResponseException;
import model.AuthData;
import model.LoginData;
import model.UserData;
import server.ServerFacade;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class LoginClient implements ConsoleClient {
    private final ServerFacade server;
    private AuthData authdata= null;
    private final int gameId;

    public LoginClient(ServerFacade facade) {
        server = facade;
        gameId= 0;
    }

    @Override
    public AuthData getAuthData() {
        return authdata;
    }

    @Override
    public int getGameID() {
        return gameId;
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String login(String... params) throws ResponseException {
        if (params.length == 2) {
            var loginData = new LoginData(params[0], params[1]);
            try{
            authdata = server.login(loginData);
            } catch (ResponseException e) {
                if(e.getMessage().contains("Unauthorized")) {
                    throw new ResponseException(400, "Credentials Failed");
                }
                throw e;
            }

            return String.format("You logged in as %s.", loginData.username());
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }

    private String register(String... params) throws ResponseException {
        if (params.length == 3) {
            var userData = new UserData(params[0], params[1], params[2]);
            try {
                authdata=server.register(userData);
            } catch (ResponseException e) {
                if(e.getMessage().contains("Forbidden")) {
                    throw new ResponseException(400, "Username Taken");
                }
                throw e;
            }
            return String.format("You logged in as %s.", userData.username());
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }

    @Override
    public String help() {
            return SET_TEXT_COLOR_BLUE + """
                    login <username> <password> - to play chess
                    register <username> <password> <email> - to create an account
                    quit - playing chess
                    help - with possible commands""";
    }

    @Override
    public String printGame() {
        return "";
    }
}
