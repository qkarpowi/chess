package model;

import chess.ChessGame;

public record JoinGame(ChessGame.TeamColor playerColor, int gameID) {
}
