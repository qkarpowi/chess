package model;

import chess.ChessGame;

public record GamaData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
}
