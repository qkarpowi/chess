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

        if(color == ChessGame.TeamColor.BLACK){
            int limit;
            if(position.getRow() == 7){
                limit = 2;
            } else {
                limit = 1;
            }
            int count = 0;
            for (int i = row - 1; i > 0; i--) {
                if(count == limit) { break; }
                ChessPosition checkPosition = new ChessPosition(i, col);
                if (board.getPiece(checkPosition) == null) {
                    if(i == 1){
                        validMoves.add(new ChessMove(position, checkPosition, ChessPiece.PieceType.QUEEN));
                        validMoves.add(new ChessMove(position, checkPosition, ChessPiece.PieceType.BISHOP));
                        validMoves.add(new ChessMove(position, checkPosition, ChessPiece.PieceType.ROOK));
                        validMoves.add(new ChessMove(position, checkPosition, ChessPiece.PieceType.KNIGHT));
                    } else {
                        validMoves.add(new ChessMove(position, checkPosition, null));
                    }
                } else{
                    break;
                }
                count++;
            }
            //Check left diagonal
            if(col > 1) {
                ChessPosition left = new ChessPosition(row -1, col -1);
                if (board.getPiece(left) != null && board.getPiece(left).getTeamColor() != color) {
                    if(row - 1 == 1){
                        validMoves.add(new ChessMove(position, left, ChessPiece.PieceType.QUEEN));
                        validMoves.add(new ChessMove(position, left, ChessPiece.PieceType.BISHOP));
                        validMoves.add(new ChessMove(position, left, ChessPiece.PieceType.ROOK));
                        validMoves.add(new ChessMove(position, left, ChessPiece.PieceType.KNIGHT));
                    } else {
                        validMoves.add(new ChessMove(position, left, null));
                    }
                }
            }
            //Check right diagonal
            if(col < 8) {
                ChessPosition right = new ChessPosition(row -1, col +1);
                if (board.getPiece(right) != null && board.getPiece(right).getTeamColor() != color) {
                    if(row - 1 == 1){
                        validMoves.add(new ChessMove(position, right, ChessPiece.PieceType.QUEEN));
                        validMoves.add(new ChessMove(position, right, ChessPiece.PieceType.BISHOP));
                        validMoves.add(new ChessMove(position, right, ChessPiece.PieceType.ROOK));
                        validMoves.add(new ChessMove(position, right, ChessPiece.PieceType.KNIGHT));
                    } else {
                        validMoves.add(new ChessMove(position, right, null));
                    }
                }
            }
        }
        if(color == ChessGame.TeamColor.WHITE){
            int limit;
            if(position.getRow() == 2){
                limit = 2;
            } else {
                limit = 1;
            }
            int count = 0;
            for (int i = row + 1; i <= 8; i++) {
                if(count == limit) { break; }
                ChessPosition checkPosition = new ChessPosition(i, col);
                if (board.getPiece(checkPosition) == null) {
                    if(i == 8){
                        validMoves.add(new ChessMove(position, checkPosition, ChessPiece.PieceType.QUEEN));
                        validMoves.add(new ChessMove(position, checkPosition, ChessPiece.PieceType.BISHOP));
                        validMoves.add(new ChessMove(position, checkPosition, ChessPiece.PieceType.ROOK));
                        validMoves.add(new ChessMove(position, checkPosition, ChessPiece.PieceType.KNIGHT));
                    } else {
                        validMoves.add(new ChessMove(position, checkPosition, null));
                    }
                }else{
                    break;
                }
                count++;
            }
            //Check left diagonal
            if(col > 1) {
                ChessPosition left = new ChessPosition(row +1, col -1);
                if (board.getPiece(left) != null && board.getPiece(left).getTeamColor() != color) {
                    if(row + 1 == 8){
                        validMoves.add(new ChessMove(position, left, ChessPiece.PieceType.QUEEN));
                        validMoves.add(new ChessMove(position, left, ChessPiece.PieceType.BISHOP));
                        validMoves.add(new ChessMove(position, left, ChessPiece.PieceType.ROOK));
                        validMoves.add(new ChessMove(position, left, ChessPiece.PieceType.KNIGHT));
                    } else {
                        validMoves.add(new ChessMove(position, left, null));
                    }
                }
            }
            //Check right diagonal
            if(col < 8) {
                ChessPosition right = new ChessPosition(row +1, col +1);
                if (board.getPiece(right) != null && board.getPiece(right).getTeamColor() != color) {
                    if(row + 1 == 8){
                        validMoves.add(new ChessMove(position, right, ChessPiece.PieceType.QUEEN));
                        validMoves.add(new ChessMove(position, right, ChessPiece.PieceType.BISHOP));
                        validMoves.add(new ChessMove(position, right, ChessPiece.PieceType.ROOK));
                        validMoves.add(new ChessMove(position, right, ChessPiece.PieceType.KNIGHT));
                    } else {
                        validMoves.add(new ChessMove(position, right, null));
                    }
                }
            }
        }
        return validMoves;
    }
}
