package clients;

import model.AuthData;

public interface ConsoleClient {
    AuthData getAuthData();

    String eval(String input);

    String help();
}
