package clients;

import model.AuthData;

public interface ConsoleClient {
    AuthData getAuthData();

    int getGameID();

    String eval(String input);

    String help();
}
