package chess.pieceCalculator;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class RookCalculator implements PieceMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();

        HashSet<ChessMove> validMoves = new HashSet<>();
        // Check Up direction
        CalculatorUtils.NorthMoves(board, position, color, validMoves, 8);

        // Check Down direction
        CalculatorUtils.SouthMoves(board, position, color, validMoves, 8);

        // Check left direction
        CalculatorUtils.WestMoves(board, position, color, validMoves, 8);

        // Check right direction
        CalculatorUtils.EastMoves(board, position, color, validMoves, 8);

        return validMoves;
    }
}
