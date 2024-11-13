package ui;

import clients.ConsoleClient;
import clients.LoginClient;
import model.AuthData;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    private final ConsoleClient client;
    private ClientState clientState;
    private AuthData authData;

    public Repl(String serverUrl) {
        client = new LoginClient(serverUrl);
        clientState = ClientState.LoggedOut;
    }

    public void run() {
        System.out.println("♕ Welcome to Chess. Type help to get started. ♕");

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
            if(clientState == ClientState.LoggedOut && client.getAuthData() != null) {
                clientState = ClientState.LoggedIn;
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