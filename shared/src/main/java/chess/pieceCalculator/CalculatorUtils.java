package chess.pieceCalculator;

import chess.*;

import java.util.HashSet;

public class CalculatorUtils {
    private static int boardHeight = 8;
    private static int boardWidth = 8;
    public static void NorthWestMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color, HashSet<ChessMove> moves, int limit){
        // Check upper-left diagonal
        int row = position.getRow();
        int col = position.getColumn();

        int count = 0;
        for (int i = row - 1, j = col - 1; i >= 1 && j >= 1; i--, j--) {
            if(count == limit) break;
            ChessPosition checkPosition = new ChessPosition(i, j);
            if (board.getPiece(checkPosition) == null) {
                moves.add(new ChessMove(position, checkPosition, null));
            } else if(board.getPiece(checkPosition).getTeamColor() != color){
                moves.add(new ChessMove(position, checkPosition, null));
                break; // Stop when encountering a piece
            }else{
                break;
            }
            count++;
        }
    }
    public static void NorthEastMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color, HashSet<ChessMove> moves, int limit){
        // Check upper-left diagonal
        int row = position.getRow();
        int col = position.getColumn();

        int count = 0;
        for (int i = row - 1, j = col + 1; i >= 1 && j <= boardHeight; i--, j++)  {
            if(count == limit) break;
            ChessPosition checkPosition = new ChessPosition(i, j);
            if (board.getPiece(checkPosition) == null) {
                moves.add(new ChessMove(position, checkPosition, null));
            } else if(board.getPiece(checkPosition).getTeamColor() != color){
                moves.add(new ChessMove(position, checkPosition, null));
                break; // Stop when encountering a piece
            }else{
                break;
            }
            count++;
        }
    }

    public static void SouthWestMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color, HashSet<ChessMove> moves, int limit){
        // Check upper-left diagonal
        int row = position.getRow();
        int col = position.getColumn();

        int count = 0;
        for (int i = row + 1, j = col - 1; i <= boardWidth && j >= 1; i++, j--)   {
            if(count == limit) break;
            ChessPosition checkPosition = new ChessPosition(i, j);
            if (board.getPiece(checkPosition) == null) {
                moves.add(new ChessMove(position, checkPosition, null));
            } else if(board.getPiece(checkPosition).getTeamColor() != color){
                moves.add(new ChessMove(position, checkPosition, null));
                break; // Stop when encountering a piece
            }else{
                break;
            }
            count++;
        }
    }
    public static void SouthEastMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color, HashSet<ChessMove> moves, int limit){
        // Check upper-left diagonal
        int row = position.getRow();
        int col = position.getColumn();

        int count = 0;
        for (int i = row + 1, j = col + 1; i <= boardWidth && j <= boardHeight; i++, j++) {
            if(count == limit) break;
            ChessPosition checkPosition = new ChessPosition(i, j);
            if (board.getPiece(checkPosition) == null) {
                moves.add(new ChessMove(position, checkPosition, null));
            } else if(board.getPiece(checkPosition).getTeamColor() != color){
                moves.add(new ChessMove(position, checkPosition, null));
                break; // Stop when encountering a piece
            }else{
                break;
            }
            count++;
        }
    }

    public static void WestMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color, HashSet<ChessMove> moves, int limit){
        // Check upper-left diagonal
        int row = position.getRow();
        int col = position.getColumn();

        int count = 0;
        for (int j = col - 1; j > 0; j--) {
            if(count == limit) break;
            ChessPosition checkPosition = new ChessPosition(row, j);
            if (board.getPiece(checkPosition) == null) {
                moves.add(new ChessMove(position, checkPosition, null));
            } else if(board.getPiece(checkPosition).getTeamColor() != color){
                moves.add(new ChessMove(position, checkPosition, null));
                break; // Stop when encountering a piece
            }else{
                break;
            }
            count++;
        }
    }
    public static void SouthMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color, HashSet<ChessMove> moves, int limit){
        // Check upper-left diagonal
        int row = position.getRow();
        int col = position.getColumn();

        int count = 0;
        for (int i = row + 1; i <= boardWidth; i++) {
            if(count == limit) break;
            ChessPosition checkPosition = new ChessPosition(i, col);
            if (board.getPiece(checkPosition) == null) {
                moves.add(new ChessMove(position, checkPosition, null));
            } else if(board.getPiece(checkPosition).getTeamColor() != color){
                moves.add(new ChessMove(position, checkPosition, null));
                break; // Stop when encountering a piece
            }else{
                break;
            }
            count++;
        }
    }
    public static void NorthMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color, HashSet<ChessMove> moves, int limit){
        int row = position.getRow();
        int col = position.getColumn();

        int count = 0;
        for (int i = row - 1; i > 0; i--) {
            if(count == limit) break;
            ChessPosition checkPosition = new ChessPosition(i, col);
            if (board.getPiece(checkPosition) == null) {
                moves.add(new ChessMove(position, checkPosition, null));
            } else if(board.getPiece(checkPosition).getTeamColor() != color){
                moves.add(new ChessMove(position, checkPosition, null));
                break; // Stop when encountering a piece
            }else{
                break;
            }
            count++;
        }
    }
    public static void EastMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color, HashSet<ChessMove> moves, int limit){
        // Check upper-left diagonal
        int row = position.getRow();
        int col = position.getColumn();

        int count = 0;
        for (int j = col + 1; j <= boardHeight; j++) {
            if(count == limit) break;
            ChessPosition checkPosition = new ChessPosition(row, j);
            if (board.getPiece(checkPosition) == null) {
                moves.add(new ChessMove(position, checkPosition, null));
            } else if(board.getPiece(checkPosition).getTeamColor() != color){
                moves.add(new ChessMove(position, checkPosition, null));
                break; // Stop when encountering a piece
            }else{
                break;
            }
            count++;
        }
    }
}