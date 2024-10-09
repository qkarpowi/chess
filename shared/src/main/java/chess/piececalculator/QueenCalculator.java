package chess.piececalculator;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class QueenCalculator implements PieceMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();

        HashSet<ChessMove> validMoves = new HashSet<>();
        // Check Up direction
        CalculatorUtils.northMoves(board, position, color, validMoves, 8);

        // Check Down direction
        CalculatorUtils.southMoves(board, position, color, validMoves, 8);

        // Check left direction
        CalculatorUtils.westMoves(board, position, color, validMoves, 8);

        // Check right direction
        CalculatorUtils.eastMoves(board, position, color, validMoves, 8);

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
