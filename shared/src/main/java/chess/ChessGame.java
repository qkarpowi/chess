package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard board;
    private boolean isGameOver = false;
    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.teamTurn= TeamColor.WHITE;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver=gameOver;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn= team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //test if you are still in check after move
        Collection<ChessMove> possibleMoves = this.board.getPiece(startPosition).pieceMoves(this.board, startPosition);
        Collection<ChessMove> validMoves = new HashSet<ChessMove>();
        ChessPiece pieceToMove = this.board.getPiece(startPosition);
        if(pieceToMove == null) {
            return validMoves;
        }
        for (ChessMove move : possibleMoves) {

            try {
                ChessBoard boardCopy = (ChessBoard) this.board.clone();
                if (move.getPromotionPiece() == null) {
                    boardCopy.addPiece(move.getEndPosition(), pieceToMove);
                } else {
                    boardCopy.addPiece(move.getEndPosition(), new ChessPiece(this.getTeamTurn(), move.getPromotionPiece()));
                }
                boardCopy.removePiece(move.getStartPosition(), pieceToMove);
                if (!internalIsInCheck(boardCopy, pieceToMove.getTeamColor())) {
                    validMoves.add(move);
                }
            } catch (CloneNotSupportedException | InvalidMoveException e) {
                throw new RuntimeException(e);
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece pieceToMove = board.getPiece(move.getStartPosition());
        if (pieceToMove == null) {
            throw new InvalidMoveException();
        }
        if (pieceToMove.getTeamColor() != this.getTeamTurn()){
            throw new InvalidMoveException("It's not this teams turn");
        }
        Collection<ChessMove> validMoves = this.validMoves(move.getStartPosition());
        if (!validMoves.contains(move)){
            throw new InvalidMoveException("Move not valid for this piece");
        }

        //test if you are still in check after move
        if(this.isInCheck(this.getTeamTurn())){
            try{
                ChessBoard boardCopy = (ChessBoard) this.board.clone();
                if(move.getPromotionPiece() == null) {
                    boardCopy.addPiece(move.getEndPosition(), pieceToMove);
                } else {
                    boardCopy.addPiece(move.getEndPosition(), new ChessPiece(this.getTeamTurn(), move.getPromotionPiece()));
                }
                boardCopy.removePiece(move.getStartPosition(), pieceToMove);
                if (internalIsInCheck(boardCopy, this.getTeamTurn())) {
                    throw new InvalidMoveException("After making move you are still in check");
                }
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        if(move.getPromotionPiece() == null) {
            this.board.addPiece(move.getEndPosition(), pieceToMove);
        } else {
            this.board.addPiece(move.getEndPosition(), new ChessPiece(this.getTeamTurn(), move.getPromotionPiece()));
        }
        this.board.removePiece(move.getStartPosition(), pieceToMove);

        //Switch Team turns.
        if(this.getTeamTurn() == TeamColor.WHITE){
            this.setTeamTurn(TeamColor.BLACK);
        } else {
            this.setTeamTurn(TeamColor.WHITE);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return this.internalIsInCheck(this.board, teamColor);
    }

    private boolean internalIsInCheck(ChessBoard board, TeamColor teamColor) {
        ChessPosition kingPosition = this.kingPosition(board, teamColor);
        if (kingPosition == null) {
            return false;
        }

        return isKingInCheck(board, kingPosition, teamColor);
    }

    private boolean isKingInCheck(ChessBoard board, ChessPosition kingPosition, TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if (isOpponentPiece(piece, teamColor) && canPieceThreatenKing(piece, board, new ChessPosition(row, col), kingPosition)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isOpponentPiece(ChessPiece piece, TeamColor teamColor) {
        return piece != null && piece.getTeamColor() != teamColor;
    }

    private boolean canPieceThreatenKing(ChessPiece piece, ChessBoard board, ChessPosition piecePosition, ChessPosition kingPosition) {
        Collection<ChessMove> possibleMoves = piece.pieceMoves(board, piecePosition);
        for (ChessMove possibleMove : possibleMoves) {
            if (isMoveThreateningKing(possibleMove, kingPosition)) {
                return true;
            }
        }
        return false;
    }

    private boolean isMoveThreateningKing(ChessMove move, ChessPosition kingPosition) {
        ChessPosition moveEnd = move.getEndPosition();
        return moveEnd.getRow() == kingPosition.getRow() && moveEnd.getColumn() == kingPosition.getColumn();
    }


    private ChessPosition kingPosition(ChessBoard board, TeamColor color) {
        for (int row = 1; row <= 8; row++){
            for (int col = 1; col <= 8; col ++){
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.equals(new ChessPiece(color, ChessPiece.PieceType.KING))) {
                    return position;
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!this.isInCheck(teamColor)) {
            return false;
        }

        return !canTeamEscapeCheck(teamColor);
    }

    private boolean canTeamEscapeCheck(TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = this.board.getPiece(position);

                if (isFriendlyPiece(piece, teamColor)) {
                    Collection<ChessMove> possibleMoves = piece.pieceMoves(this.board, position);
                    if (canMoveEscapeCheck(piece, possibleMoves, teamColor)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isFriendlyPiece(ChessPiece piece, TeamColor teamColor) {
        return piece != null && piece.getTeamColor() == teamColor;
    }

    private boolean canMoveEscapeCheck(ChessPiece piece, Collection<ChessMove> possibleMoves, TeamColor teamColor) {
        for (ChessMove possibleMove : possibleMoves) {
            if (isSafeMove(possibleMove, piece, teamColor)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSafeMove(ChessMove possibleMove, ChessPiece piece, TeamColor teamColor) {
        try {
            ChessBoard boardCopy = (ChessBoard) this.board.clone();
            applyMove(boardCopy, possibleMove, piece, teamColor);
            return !internalIsInCheck(boardCopy, teamColor);
        } catch (CloneNotSupportedException | InvalidMoveException e) {
            throw new RuntimeException(e);
        }
    }

    private void applyMove(ChessBoard board, ChessMove move, ChessPiece piece, TeamColor teamColor) throws InvalidMoveException {
        if (move.getPromotionPiece() == null) {
            board.addPiece(move.getEndPosition(), piece);
        } else {
            board.addPiece(move.getEndPosition(), new ChessPiece(teamColor, move.getPromotionPiece()));
        }
        board.removePiece(move.getStartPosition(), piece);
    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (this.isInCheckmate(teamColor)){
            return false;
        }
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPiece piece = this.board.getPiece(new ChessPosition(row, col));
                if(piece != null && piece.getTeamColor() == teamColor){
                    if (!this.validMoves(new ChessPosition(row, col)).isEmpty()){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
