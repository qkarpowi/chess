package chess.pieceCalculator;

import chess.*;
import chess.pieceCalculator.PieceMovesCalculator;

import java.util.*;

public class BishopCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();

        HashSet<ChessMove> validMoves = new HashSet<>();


        // Check upper-left diagonal
        CalculatorUtils.NorthWestMoves(board, position, color, validMoves, 8);

        // Check upper-right diagonal
        CalculatorUtils.NorthEastMoves(board, position, color, validMoves, 8);

        // Check lower-left diagonal
        CalculatorUtils.SouthWestMoves(board, position, color, validMoves, 8);

        // Check lower-right diagonal
        CalculatorUtils.SouthEastMoves(board, position, color, validMoves, 8);

        return validMoves;
    }
}
