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

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class WebsocketCommunicator extends Endpoint {

    Session session;
    private ChessGame.TeamColor teamColor;
    private ChessGame game;

    public WebsocketCommunicator(String serverDomain, ChessGame.TeamColor teamColor, ChessGame game) {
        this.teamColor = teamColor;
        this.game = game;
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
            game = loadGame.getGame();
        }
    }

    private void printNotification(String message) {
        System.out.printf("\n" + SET_TEXT_COLOR_MAGENTA + message + "\n");
        System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\n" + "Chess>>> " + SET_TEXT_COLOR_GREEN);
    }

    private void printLoadedGame(ChessGame game) {
        if(this.teamColor == ChessGame.TeamColor.BLACK) {
            System.out.println("\n" + PrintBoard.printBlackPerspective(game.getBoard(), null));
        } else {
            System.out.println("\n" + PrintBoard.printWhitePerspective(game.getBoard(), null));
        }
        System.out.print(SET_TEXT_COLOR_LIGHT_GREY + "\n" + "Chess>>> " + SET_TEXT_COLOR_GREEN);
    }

    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }
}