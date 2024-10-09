package chess.piececalculator;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class KnightCalculator implements PieceMovesCalculator{
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessGame.TeamColor color = board.getPiece(position).getTeamColor();

        HashSet<ChessMove> validMoves = new HashSet<>();


        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition checkPosition;
        ChessPiece piece;

        //Check top direction
        if (row <= 6) {
            if (col != 8) {
                checkPosition = new ChessPosition(row + 2, col + 1);
                piece = board.getPiece(checkPosition);
                if(piece == null || piece.getTeamColor() != color){
                    validMoves.add(new ChessMove(position, checkPosition, null));
                }
            }

            if (col > 1) {
                checkPosition = new ChessPosition(row + 2, col - 1);
                piece = board.getPiece(checkPosition);
                if (piece == null || piece.getTeamColor() != color) {
                    validMoves.add(new ChessMove(position, checkPosition, null));
                }
            }
        }
        //Check bottom direction
        if (row >= 3) {
            if (col != 8) {
                checkPosition = new ChessPosition(row - 2, col + 1);
                piece = board.getPiece(checkPosition);
                if(piece == null || piece.getTeamColor() != color){
                    validMoves.add(new ChessMove(position, checkPosition, null));
                }
            }
            if (col > 1){
                checkPosition = new ChessPosition(row - 2, col - 1);
                piece = board.getPiece(checkPosition);
                if (piece == null || piece.getTeamColor() != color) {
                    validMoves.add(new ChessMove(position, checkPosition, null));
                }
            }
        }

        //Check right direction
        if (col >= 3) {
            if (row != 8) {
                checkPosition = new ChessPosition(row + 1, col - 2);
                piece = board.getPiece(checkPosition);
                if(piece == null || piece.getTeamColor() != color){
                    validMoves.add(new ChessMove(position, checkPosition, null));
                }
            }
            if (row > 1) {
                checkPosition = new ChessPosition(row - 1, col - 2);
                piece = board.getPiece(checkPosition);
                if (piece == null || piece.getTeamColor() != color) {
                    validMoves.add(new ChessMove(position, checkPosition, null));
                }
            }
        }

        //Check left direction
        if (col <= 6) {
            if (row != 8) {
                checkPosition = new ChessPosition(row + 1, col + 2);
                piece = board.getPiece(checkPosition);
                if(piece == null || piece.getTeamColor() != color){
                    validMoves.add(new ChessMove(position, checkPosition, null));
                }
            }
            if (row > 1) {
                checkPosition = new ChessPosition(row - 1, col + 2);
                piece = board.getPiece(checkPosition);
                if (piece == null || piece.getTeamColor() != color) {
                    validMoves.add(new ChessMove(position, checkPosition, null));
                }
            }
        }

        return validMoves;
    }
}
