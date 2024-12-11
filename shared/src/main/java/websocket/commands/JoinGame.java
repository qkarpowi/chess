package websocket.commands;

import chess.ChessGame;

public class JoinGame extends UserGameCommand {

    ChessGame.TeamColor playerColor;

    public JoinGame(String authToken, int gameID, ChessGame.TeamColor playerColor) {
        super(CommandType.CONNECT, authToken, gameID);
        this.playerColor = playerColor;
    }

    public ChessGame.TeamColor getColor() {
        return playerColor;
    }
}