package server;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.JoinGame;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ServerError;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebsocketHandler {

    public WebsocketHandler() {

    }

    @OnWebSocketConnect
    public void onConnect(Session session) throws Exception {
        System.out.println("Connected " + session.getRemoteAddress());
        Server.gameSessions.put(session, 0);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        Server.gameSessions.remove(session);
        System.out.println("Closed " + session.getRemoteAddress() + " Status Code:" + statusCode + " Because:" + reason);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        System.out.println("Message " + message);

        if(message.contains("CONNECT")) {
            JoinGame command = new Gson().fromJson(message, JoinGame.class);
            Server.gameSessions.replace(session, command.getGameID());
            handleJoinGame(session, command);
        }

    }

    // Send the notification to all clients on the current game except the currSession
    public void broadcastMessage(Session currSession, ServerMessage message) throws IOException {
        broadcastMessage(currSession, message, false);
    }

    // Send the notification to all clients on the current game
    public void broadcastMessage(Session currSession, ServerMessage message, boolean toSelf) throws IOException {
        System.out.printf("Broadcasting (toSelf: %s): %s%n", toSelf, new Gson().toJson(message));
        for (Session session : Server.gameSessions.keySet()) {
            boolean inAGame = Server.gameSessions.get(session) != 0;
            boolean sameGame = Server.gameSessions.get(session).equals(Server.gameSessions.get(currSession));
            boolean isSelf = session == currSession;
            if ((toSelf || !isSelf) && inAGame && sameGame) {
                sendMessage(session, message);
            }
        }
    }

    public void sendMessage(Session session, ServerMessage message) throws IOException {
        session.getRemote().sendString(new Gson().toJson(message));
    }
    private void sendError(Session session, ServerError error) {
        try{
            System.out.printf("Error: %s%n", new Gson().toJson(error));
            session.getRemote().sendString(new Gson().toJson(error));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handleJoinGame(Session session, JoinGame command) {
        try{
            //Get game data
            GameData game = Server.gameService.getGame(command.getGameID());
            //get user data
            AuthData authData = Server.userService.getAuthData(command.getAuthToken());

            //validate
            if (authData == null) {
                throw new Exception("Auth token not found");
            }

            if (game == null) {
                throw new Exception("Game not found");
            }

            Notification notification;

            //get color
            ChessGame.TeamColor color = command.getColor();
            if(color != null){
                //validate they are the correct color
                if(Objects.equals(game.whiteUsername(), authData.username())) {

                } else if(Objects.equals(game.blackUsername(), authData.username())) {

                } else{
                    throw new Exception("Invalid color");
                }

                notification = new Notification("%s has joined the game as %s".formatted(authData.username(), command.getColor().toString()));
            }else{
                notification = new Notification("%s has joined the game as an observer".formatted(authData.username()));

            }

            //send notification
            broadcastMessage(session, notification);

            //update session map
            LoadGame load = new LoadGame(game.game());
            sendMessage(session, load);

        } catch (Exception e){
            sendError(session, new ServerError(e.getMessage()));
        }

    }

}
