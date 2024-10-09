package chess.piececalculator;

import chess.*;

import java.util.HashSet;

public class CalculatorUtils {
    private static int boardHeight = 8;
    private static int boardWidth = 8;
    public static void northWestMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color, HashSet<ChessMove> moves, int limit){
        // Check upper-right diagonal
        int row = position.getRow();
        int col = position.getColumn();

        int count = 0;
        for (int i = row - 1, j = col - 1; i >= 1 && j >= 1; i--, j--) {
            if(count == limit) { break; }
            if(!checkSquare(board, position, color, i, j, moves)){
                break;
            }
            count++;
        }
    }
    public static void northEastMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color, HashSet<ChessMove> moves, int limit){
        // Check upper-left diagonal
        int row = position.getRow();
        int col = position.getColumn();

        int count = 0;
        for (int i = row - 1, j = col + 1; i >= 1 && j <= boardHeight; i--, j++)  {
            if(count == limit) { break; }
            if(!checkSquare(board, position, color, i, j, moves)){
                break;
            }
            count++;
        }
    }

    public static void southWestMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color, HashSet<ChessMove> moves, int limit){
        // Check lower-left diagonal
        int row = position.getRow();
        int col = position.getColumn();

        int count = 0;
        for (int i = row + 1, j = col - 1; i <= boardWidth && j >= 1; i++, j--)   {
            if(count == limit) { break; }
            if(!checkSquare(board, position, color, i, j, moves)){
                break;
            }
            count++;
        }
    }
    public static void southEastMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color, HashSet<ChessMove> moves, int limit){
        // Check lower-left diagonal
        int row = position.getRow();
        int col = position.getColumn();

        int count = 0;
        for (int i = row + 1, j = col + 1; i <= boardWidth && j <= boardHeight; i++, j++) {
            if(count == limit) { break; }
            if(!checkSquare(board, position, color, i, j, moves)){
                break;
            }
            count++;
        }
    }

    public static void westMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color, HashSet<ChessMove> moves, int limit){
        // Check right
        int row = position.getRow();
        int col = position.getColumn();

        int count = 0;
        for (int j = col - 1; j > 0; j--) {
            if(count == limit) { break; }
            if(!checkSquare(board, position, color, row, j, moves)){
                break;
            }
            count++;
        }
    }
    public static void southMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color, HashSet<ChessMove> moves, int limit){
        // Check downward
        int row = position.getRow();
        int col = position.getColumn();

        int count = 0;
        for (int i = row + 1; i <= boardWidth; i++) {
            if(count == limit) { break; }
            if(!checkSquare(board, position, color, i, col, moves)){
                break;
            }
            count++;
        }
    }
    public static void northMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color, HashSet<ChessMove> moves, int limit){
        //check north
        int row = position.getRow();
        int col = position.getColumn();

        int count = 0;
        for (int i = row - 1; i > 0; i--) {
            if(count == limit) { break; }
            if(!checkSquare(board, position, color, i, col, moves)){
                break;
            }
            count++;
        }
    }
    public static void eastMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor color, HashSet<ChessMove> moves, int limit){
        // Check right
        int row = position.getRow();
        int col = position.getColumn();
        int count = 0;
        for (int j = col + 1; j <= boardHeight; j++) {
            if(count == limit) { break; }
            if(!checkSquare(board, position, color, row, j, moves)){
                break;
            }
            count++;
        }
    }

    private static boolean checkSquare(ChessBoard board, ChessPosition position, ChessGame.TeamColor color, int row, int col, HashSet<ChessMove> moves){
        ChessPosition checkPosition = new ChessPosition(row, col);
        if (board.getPiece(checkPosition) == null) {
            moves.add(new ChessMove(position, checkPosition, null));
        } else if(board.getPiece(checkPosition).getTeamColor() != color){
            moves.add(new ChessMove(position, checkPosition, null));
            return false; // Stop when encountering a piece
        }else{
            return false;
        }
        return true;
    }
}