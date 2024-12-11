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
import websocket.commands.Leave;
import websocket.commands.MakeMove;
import websocket.commands.Resign;
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
        }else if(message.contains("MAKE_MOVE")) {
            MakeMove command = new Gson().fromJson(message, MakeMove.class);
            handleMakeMove(session, command);
        }else if(message.contains("LEAVE")) {
            Leave command = new Gson().fromJson(message, Leave.class);
            handleLeave(session, command);
        }else if(message.contains("RESIGN")) {
            Resign command = new Gson().fromJson(message, Resign.class);
            handleResign(session, command);
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

    private void handleLeave(Session session, Leave command) {
        try {
            AuthData auth = Server.userService.getAuthData(command.getAuthToken());

            Notification notification = new Notification("%s has left the game".formatted(auth.username()));
            broadcastMessage(session, notification);

            session.close();
        } catch (Exception e) {
            sendError(session, new ServerError("Not authorized"));
        }
    }

    private void handleResign(Session session, Resign command) {
        try {
            AuthData auth = Server.userService.getAuthData(command.getAuthToken());
            GameData game = Server.gameService.getGame(command.getGameID());
            ChessGame.TeamColor userColor = getTeamColor(auth.username(), game);

            String opponentUsername = userColor == ChessGame.TeamColor.WHITE ? game.blackUsername() : game.whiteUsername();

            if (userColor == null) {
                sendError(session, new ServerError("You are observing this game"));
                return;
            }

            if (game.game().isGameOver()) {
                sendError(session, new ServerError("Game is already over!"));
                return;
            }

            game.game().setGameOver(true);
            Server.gameService.updateGame(auth.authToken(), game);
            Notification notification = new Notification("%s has forfeited, %s wins!".formatted(auth.username(), opponentUsername));
            broadcastMessage(session, notification, true);

        } catch (Exception e) {
            sendError(session, new ServerError(e.getMessage()));
        }
    }

    private ChessGame.TeamColor getTeamColor(String username, GameData game) {
        if (username.equals(game.whiteUsername())) {
            return ChessGame.TeamColor.WHITE;
        }
        else if (username.equals(game.blackUsername())) {
            return ChessGame.TeamColor.BLACK;
        }
        else return null;
    }


    private void handleMakeMove(Session session, MakeMove command) {
        try {
            AuthData auth = Server.userService.getAuthData(command.getAuthToken());
            GameData game = Server.gameService.getGame(command.getGameID());
            ChessGame.TeamColor userColor = getTeamColor(auth.username(), game);
            if (userColor == null) {
                sendError(session, new ServerError("You are observing this game"));
                return;
            }

            if (game.game().isGameOver()) {
                sendError(session, new ServerError("Game is over"));
                return;
            }

            if (game.game().getTeamTurn().equals(userColor)) {
                game.game().makeMove(command.getMove());

                Notification notification;
                ChessGame.TeamColor opponentColor = userColor == ChessGame.TeamColor.WHITE ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;

                if (game.game().isInCheckmate(opponentColor)) {
                    notification = new Notification("Checkmate! %s wins!".formatted(auth.username()));
                    game.game().setGameOver(true);
                }
                else if (game.game().isInStalemate(opponentColor)) {
                    notification = new Notification("Stalemate caused by %s's move! It's a tie!".formatted(auth.username()));
                    game.game().setGameOver(true);
                }
                else if (game.game().isInCheck(opponentColor)) {
                    notification = new Notification("A move has been made by %s, %s is now in check!".formatted(auth.username(), opponentColor.toString()));
                }
                else {
                    notification = new Notification("A move has been made by %s".formatted(auth.username()));
                }
                broadcastMessage(session, notification);

                Server.gameService.updateGame(auth.authToken(), game);

                LoadGame load = new LoadGame(game.game());
                broadcastMessage(session, load, true);
            }
            else {
                sendError(session, new ServerError("Is not your turn"));
            }
        }
        catch (Exception e) {
            sendError(session, new ServerError(e.getMessage()));
        }
    }

}
