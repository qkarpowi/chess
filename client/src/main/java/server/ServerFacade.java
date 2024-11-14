package server;

import com.google.gson.Gson;
import exception.ResponseException;

import model.*;

import java.io.*;
import java.net.*;

public class ServerFacade {
    private final String serverUrl;

    private String authToken = null;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData Register(UserData user) throws ResponseException {
        var path = "/user";
        var result = this.makeRequest("POST", path, user, AuthData.class);
        this.authToken = result.authToken();
        return result;
    }

    public AuthData login(LoginData login) throws ResponseException{
        var path = "/session";
        var result = this.makeRequest("POST", path, login, AuthData.class);
        this.authToken = result.authToken();
        return result;
    }

    public void logout() throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null);
        authToken = null;
    }

    public GameList listGames() throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, null, GameList.class);
    }

    public void createGame(GameCreate game) throws ResponseException {
        var path = "/game";
        this.makeRequest("POST", path, game, null);
    }

    public void joinGame(JoinGame game) throws ResponseException {
        var path = "/game";
        this.makeRequest("POST", path, game, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if(this.authToken != null) {
                http.setRequestProperty("Authorization", this.authToken);
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status + "\nMessage: " + http.getResponseMessage());
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
