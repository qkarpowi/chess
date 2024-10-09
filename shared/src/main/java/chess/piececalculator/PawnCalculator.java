package chess.piececalculator;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class PawnCalculator implements PieceMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();
        int row = position.getRow();
        int col = position.getColumn();
        HashSet<ChessMove> validMoves = new HashSet<>();

        int forwardDirection = (color == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int startRow = (color == ChessGame.TeamColor.WHITE) ? 2 : 7;
        int promotionRow = (color == ChessGame.TeamColor.WHITE) ? 8 : 1;

        int moveLimit = (row == startRow) ? 2 : 1;
        addForwardMoves(validMoves, board, position, row, col, forwardDirection, moveLimit, promotionRow);

        // Check diagonals
        addDiagonalMove(validMoves, board, position, row + forwardDirection, col - 1, color, promotionRow);
        addDiagonalMove(validMoves, board, position, row + forwardDirection, col + 1, color, promotionRow);

        return validMoves;
    }

    private void addForwardMoves(HashSet<ChessMove> validMoves, ChessBoard board, ChessPosition position,
                                 int row, int col, int forwardDirection, int moveLimit, int promotionRow) {
        int count = 0;
        for (int i = row + forwardDirection; i > 0 && i <= 8 && count < moveLimit; i += forwardDirection) {
            ChessPosition checkPosition = new ChessPosition(i, col);
            if (board.getPiece(checkPosition) == null) {
                if (i == promotionRow) {
                    addPromotionMoves(validMoves, position, checkPosition);
                } else {
                    validMoves.add(new ChessMove(position, checkPosition, null));
                }
            } else {
                break;
            }
            count++;
        }
    }

    private void addDiagonalMove(HashSet<ChessMove> validMoves, ChessBoard board, ChessPosition position,
                                 int targetRow, int targetCol, ChessGame.TeamColor color, int promotionRow) {
        if (targetCol >= 1 && targetCol <= 8) {
            ChessPosition targetPosition = new ChessPosition(targetRow, targetCol);
            ChessPiece targetPiece = board.getPiece(targetPosition);
            if (targetPiece != null && targetPiece.getTeamColor() != color) {
                if (targetRow == promotionRow) {
                    addPromotionMoves(validMoves, position, targetPosition);
                } else {
                    validMoves.add(new ChessMove(position, targetPosition, null));
                }
            }
        }
    }

    private void addPromotionMoves(HashSet<ChessMove> validMoves, ChessPosition startPosition, ChessPosition endPosition) {
        validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
        validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
        validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
        validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
    }

}
