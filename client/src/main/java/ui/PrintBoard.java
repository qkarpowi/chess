package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import static ui.EscapeSequences.*;

public class PrintBoard {
    public static String printBlackPerspective(ChessBoard board){
        StringBuilder output = new StringBuilder();
        output.append(SET_BG_COLOR_DARK_GREY);
        output.append(SET_TEXT_COLOR_BLACK);
        output.append("    h  g  f  e  d  c  b  a \n");
        output.append(RESET_BG_COLOR);

        //Printing board
        printBoard(board, output, 0, 0, 1, 1);

        output.append(SET_BG_COLOR_DARK_GREY);
        output.append("    h  g  f  e  d  c  b  a \n");
        output.append(RESET_BG_COLOR);
        return output.toString();
    }
    public static String printWhitePerspective(ChessBoard board){
        StringBuilder output = new StringBuilder();
        output.append(SET_BG_COLOR_DARK_GREY);
        output.append(SET_TEXT_COLOR_BLACK);
        output.append("    a  b  c  d  e  f  g  h \n");
        output.append(RESET_BG_COLOR);

        //Printing board
        printBoard(board, output, 7, 7, -1, -1);

        output.append(SET_BG_COLOR_DARK_GREY);
        output.append("    a  b  c  d  e  f  g  h \n");
        output.append(RESET_BG_COLOR);
        return output.toString();
    }

    private static void printBoard(ChessBoard board, StringBuilder output,
                                   int row, int col, int rowDelta, int colDelta) {
        int count = 1;
        for (; row < 8 && row >= 0; row += rowDelta) {
            for (; col < 8 && col >= 0; col += colDelta) {
                if ((col == 0 && colDelta == 1)||(col == 7 && colDelta == -1)) {
                    // Print intro number with dark grey background
                    output.append(SET_BG_COLOR_DARK_GREY)
                            .append(SET_TEXT_COLOR_BLACK)
                            .append(" ").append(row + 1).append(" ");
                }

                // Set background color based on count
                output.append(count % 2 == 1 ? SET_BG_COLOR_WHITE : SET_BG_COLOR_LIGHT_GREY);
                count++;

                // Piece logic: Get piece and append the correct symbol or empty space
                ChessPiece piece = board.getPiece(new ChessPosition(row + 1, col + 1));
                if (piece == null) {
                    output.append(EMPTY);
                } else {
                    switch (piece.getPieceType()) {
                        case PAWN -> output.append(piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_PAWN : BLACK_PAWN);
                        case ROOK -> output.append(piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_ROOK : BLACK_ROOK);
                        case KNIGHT -> output.append(piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KNIGHT : BLACK_KNIGHT);
                        case BISHOP -> output.append(piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_BISHOP : BLACK_BISHOP);
                        case KING -> output.append(piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_KING : BLACK_KING);
                        case QUEEN -> output.append(piece.getTeamColor() == ChessGame.TeamColor.WHITE ? WHITE_QUEEN : BLACK_QUEEN);
                    }
                }

                if ((col == 7 && colDelta == 1) || (col == 0 && colDelta == -1)) {
                    // Print outro number and reset background color
                    output.append(SET_BG_COLOR_DARK_GREY)
                            .append(SET_TEXT_COLOR_BLACK)
                            .append(" ").append(row + 1).append(" \n")
                            .append(RESET_BG_COLOR);
                    count++;
                }
            }
            col -= colDelta * 8;
        }
    }


}
