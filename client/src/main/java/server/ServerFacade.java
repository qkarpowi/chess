package server;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;

import model.*;
import websocket.commands.Leave;
import websocket.commands.MakeMove;
import websocket.commands.Resign;
import websocket.commands.UserGameCommand;

import java.io.*;
import java.net.*;

public class ServerFacade {
    private final String serverUrl;
    private final String httpUrl;
    private WebsocketCommunicator ws;

    public ServerFacade(String url) {
        serverUrl = url;
        httpUrl = "http://" + url;
    }

    public AuthData register(UserData user) throws ResponseException {
        var path = "/user";
        return this.makeRequest("POST", path, user, AuthData.class, null);
    }

    public AuthData login(LoginData login) throws ResponseException{
        var path = "/session";
        return this.makeRequest("POST", path, login, AuthData.class, null);
    }

    public void logout(AuthData authData) throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null, authData.authToken());
    }

    public GameList listGames(AuthData authData) throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, null, GameList.class, authData.authToken());
    }

    public GameData createGame(GameCreate game, AuthData authData) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, game, GameData.class, authData.authToken());
    }

    public void joinGame(JoinGame game, AuthData authData) throws ResponseException {
        var path = "/game";
        this.makeRequest("PUT", path, game, null, authData.authToken());
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String authtoken) throws ResponseException {
        try {
            URL url = (new URI(httpUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if(authtoken != null) {
                http.setRequestProperty("Authorization", authtoken);
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

    public void connectWS(ChessGame.TeamColor color) {
        try {
            ws = new WebsocketCommunicator(serverUrl, color);
        }
        catch (Exception e) {
            System.out.println("Failed to make connection with server");
        }
    }

    public void joinGameWS(String authToken, int gameID, ChessGame.TeamColor color) {
        connectWS(color);
        sendCommand(new websocket.commands.JoinGame(authToken, gameID, color));
    }

    public void makeMove(int gameID, ChessMove move, String authToken) {
        sendCommand(new MakeMove(authToken, gameID, move));
    }

    public void leave(String authToken, int gameID) {
        sendCommand(new Leave(authToken, gameID));
    }

    public void resign(String authToken, int gameID) {
        sendCommand(new Resign(authToken, gameID));
    }

    public void sendCommand(UserGameCommand command) {
        String message = new Gson().toJson(command);
        ws.sendMessage(message);
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
