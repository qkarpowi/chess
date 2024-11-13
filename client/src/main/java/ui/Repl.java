package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    private final LoginClient client;

    public Repl(String serverUrl) {
        client = new LoginClient(serverUrl);
    }

    public void run() {
        System.out.println("♕ Welcome to Chess. Sign in to start. ♕");
        System.out.print(client.help());

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
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + "Chess>>> ");
    }

}