package model;

import chess.ChessGame;

import java.util.Collection;

public record GameList(Collection<GameData> games) {
}
