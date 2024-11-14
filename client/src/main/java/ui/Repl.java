package ui;

import clients.ConsoleClient;
import clients.GameClient;
import clients.LoginClient;
import clients.PreGameClient;
import model.AuthData;
import server.ServerFacade;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    private final String serverUrl;
    private ConsoleClient client;
    private ClientState clientState;
    private final ServerFacade facade;

    public Repl(String serverUrl) {
        this.serverUrl = serverUrl;
        facade = new ServerFacade(serverUrl);
        client = new LoginClient(facade);
        clientState = ClientState.LoggedOut;
    }

    public void run() {
        System.out.print("♕ Welcome to Chess. Type help to get started. ♕");
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
            if(client.getAuthData() != null && client.getGameID() == 0) {
                clientState = ClientState.LoggedIn;
                client = new PreGameClient(facade, client.getAuthData());
            }
            if(clientState == ClientState.LoggedIn && client.getAuthData() == null) {
                clientState = ClientState.LoggedOut;
                client = new LoginClient(facade);
            }
            if(clientState == ClientState.LoggedIn && client.getGameID() != 0) {
                clientState = ClientState.InGame;
                client = new GameClient(facade, client.getAuthData(), client.getGameID());
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        switch (clientState) {
            case LoggedOut:
                System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\n" + "[LOGGED_OUT]>>> " + SET_TEXT_COLOR_GREEN);
                break;
            case LoggedIn:
                System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\n" + client.getAuthData().username() + ">>> " + SET_TEXT_COLOR_GREEN);
                break;
            case InGame:
                System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\n" + client.getAuthData().username() + " Chess>>> " + SET_TEXT_COLOR_GREEN);
        }

    }

}