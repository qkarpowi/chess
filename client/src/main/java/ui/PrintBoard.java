package ui;

import chess.ChessBoard;

import static ui.EscapeSequences.*;

public class PrintBoard {
    public static String PrintBlackPerspective(ChessBoard board){
        StringBuilder output = new StringBuilder();
        output.append(SET_BG_COLOR_DARK_GREY);
        output.append(SET_TEXT_COLOR_BLACK);
        output.append("    a  b  c  d  e  f  g  h \n");
        output.append(RESET_BG_COLOR);

        //Printing board
        int count = 1;
        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++){
                if(col == 0){
                    //print intro number
                    output.append(SET_BG_COLOR_DARK_GREY);
                    output.append(SET_TEXT_COLOR_BLACK);
                    output.append(" ").append(row + 1).append(" ");
                }

                //printing background
                if(count % 2 == 1){
                    output.append(SET_BG_COLOR_LIGHT_GREY);
                } else {
                    output.append(SET_BG_COLOR_BLACK);
                }
                count++;

                //piece logic
                output.append("   ");

                if(col == 7){
                    //print outro number
                    output.append(SET_BG_COLOR_DARK_GREY);
                    output.append(SET_TEXT_COLOR_BLACK);
                    output.append(" ").append(row + 1).append(" \n");
                    output.append(RESET_BG_COLOR);
                    count++;
                }
            }
        }

        output.append(SET_BG_COLOR_DARK_GREY);
        output.append("    a  b  c  d  e  f  g  h \n");
        output.append(RESET_BG_COLOR);
        return output.toString();
    }
    public static String PrintWhitePerspective(ChessBoard board){
        StringBuilder output = new StringBuilder();
        output.append(SET_BG_COLOR_DARK_GREY);
        output.append(SET_TEXT_COLOR_BLACK);
        output.append("    h  g  f  e  d  c  b  a \n");
        output.append(RESET_BG_COLOR);

        output.append(SET_BG_COLOR_DARK_GREY);
        output.append("    h  g  f  e  d  c  b  a \n");
        output.append(RESET_BG_COLOR);
        return output.toString();
    }

}
