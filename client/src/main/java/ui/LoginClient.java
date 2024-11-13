package ui;

import server.ServerFacade;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class LoginClient {
    private final ServerFacade server;
    private final String serverUrl;

    public LoginClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

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

    private String login(String... params){
        return "";
    }

    private String register(String... params) {
        return "";
    }

    public String help() {
            return SET_TEXT_COLOR_BLUE + """
                    login <username> <password> - to play chess
                    register <username> <password> <email> - to create an account
                    quit - playing chess
                    """;

    }
}
