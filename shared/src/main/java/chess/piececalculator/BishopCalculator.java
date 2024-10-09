package chess.piececalculator;

import chess.*;

import java.util.*;

public class BishopCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();

        HashSet<ChessMove> validMoves = new HashSet<>();


        // Check upper-left diagonal
        CalculatorUtils.northWestMoves(board, position, color, validMoves, 8);

        // Check upper-right diagonal
        CalculatorUtils.northEastMoves(board, position, color, validMoves, 8);

        // Check lower-left diagonal
        CalculatorUtils.southWestMoves(board, position, color, validMoves, 8);

        // Check lower-right diagonal
        CalculatorUtils.southEastMoves(board, position, color, validMoves, 8);

        return validMoves;
    }
}
