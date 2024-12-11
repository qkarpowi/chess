package websocket.messages;

public class ServerError extends ServerMessage {

    String errorMessage;
    public ServerError(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }

    public String getMessage() {
        return errorMessage;
    }
}