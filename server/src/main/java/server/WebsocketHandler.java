package server;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class WebsocketHandler {

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


    }


}
