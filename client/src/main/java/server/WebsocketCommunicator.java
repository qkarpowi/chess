package server;

import chess.ChessGame;
import com.google.gson.Gson;
import ui.PrintBoard;
import websocket.messages.LoadGame;
import websocket.messages.Notification;
import websocket.messages.ServerError;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static ui.EscapeSequences.ERASE_LINE;
import static ui.EscapeSequences.SET_TEXT_COLOR_MAGENTA;

public class WebsocketCommunicator extends Endpoint {

    Session session;
    private ChessGame.TeamColor teamColor;

    public WebsocketCommunicator(String serverDomain, ChessGame.TeamColor teamColor) {
        this.teamColor = teamColor;
        try {
            URI uri = new URI("ws://" + serverDomain + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    handleMessage(message);
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
    }

    private void handleMessage(String message) {
        if (message.contains("\"serverMessageType\":\"NOTIFICATION\"")) {
            Notification notif = new Gson().fromJson(message, Notification.class);
            printNotification(notif.getMessage());
        }
        else if (message.contains("\"serverMessageType\":\"ERROR\"")) {
            ServerError error = new Gson().fromJson(message, ServerError.class);
            printNotification(error.getMessage());
        }
        else if (message.contains("\"serverMessageType\":\"LOAD_GAME\"")) {
            LoadGame loadGame = new Gson().fromJson(message, LoadGame.class);
            printLoadedGame(loadGame.getGame());
        }
    }

    private void printNotification(String message) {
        System.out.printf("\n" + SET_TEXT_COLOR_MAGENTA + message + "\n");
    }

    private void printLoadedGame(ChessGame game) {
        if(this.teamColor == ChessGame.TeamColor.BLACK) {
            System.out.println(PrintBoard.printBlackPerspective(game.getBoard(), null));
        } else {
            System.out.println(PrintBoard.printWhitePerspective(game.getBoard(), null));
        }
    }

    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }
}