package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Represents the chess board and manages game state
 */
public class ChessBoard {
    private ChessPiece[][] board;
    private Color currentPlayer;
    private Stack<Move> moveHistory;
    private Position enPassantTarget;
    private boolean whiteKingSideCastle;
    private boolean whiteQueenSideCastle;
    private boolean blackKingSideCastle;
    private boolean blackQueenSideCastle;
    private List<ChessPiece> capturedWhite;
    private List<ChessPiece> capturedBlack;
    private GameState gameState;
    
    public enum GameState {
        IN_PROGRESS,
        CHECK,
        CHECKMATE,
        STALEMATE
    }
    
    public ChessBoard() {
        board = new ChessPiece[8][8];
        currentPlayer = Color.WHITE;
        moveHistory = new Stack<>();
        capturedWhite = new ArrayList<>();
        capturedBlack = new ArrayList<>();
        whiteKingSideCastle = true;
        whiteQueenSideCastle = true;
        blackKingSideCastle = true;
        blackQueenSideCastle = true;
        gameState = GameState.IN_PROGRESS;
        initializeBoard();
    }
    
    /**
     * Initialize the board with starting positions
     */
    private void initializeBoard() {
        // Place pawns
        for (int col = 0; col < 8; col++) {
            board[1][col] = new ChessPiece(Piece.PAWN, Color.BLACK);
            board[6][col] = new ChessPiece(Piece.PAWN, Color.WHITE);
        }
        
        // Place other pieces
        Piece[] backRow = {Piece.ROOK, Piece.KNIGHT, Piece.BISHOP, Piece.QUEEN, 
                          Piece.KING, Piece.BISHOP, Piece.KNIGHT, Piece.ROOK};
        
        for (int col = 0; col < 8; col++) {
            board[0][col] = new ChessPiece(backRow[col], Color.BLACK);
            board[7][col] = new ChessPiece(backRow[col], Color.WHITE);
        }
    }
    
    /**
     * Get piece at position
     */
    public ChessPiece getPieceAt(Position pos) {
        if (!pos.isValid()) return null;
        return board[pos.getRow()][pos.getCol()];
    }
    
    /**
     * Set piece at position
     */
    private void setPieceAt(Position pos, ChessPiece piece) {
        if (pos.isValid()) {
            board[pos.getRow()][pos.getCol()] = piece;
        }
    }
    
    /**
     * Get current player
     */
    public Color getCurrentPlayer() {
        return currentPlayer;
    }
    
    /**
     * Get game state
     */
    public GameState getGameState() {
        return gameState;
    }
    
    /**
     * Get captured white pieces
     */
    public List<ChessPiece> getCapturedWhite() {
        return new ArrayList<>(capturedWhite);
    }
    
    /**
     * Get captured black pieces
     */
    public List<ChessPiece> getCapturedBlack() {
        return new ArrayList<>(capturedBlack);
    }
    
    /**
     * Get move history
     */
    public Stack<Move> getMoveHistory() {
        return new Stack<Move>() {{
            addAll(moveHistory);
        }};
    }
    
    /**
     * Check if a move is valid
     */
    public boolean isValidMove(Position from, Position to) {
        List<Position> validMoves = getValidMoves(from);
        return validMoves.contains(to);
    }
    
    /**
     * Get all valid moves for a piece at given position
     */
    public List<Position> getValidMoves(Position from) {
        ChessPiece piece = getPieceAt(from);
        if (piece == null || piece.getColor() != currentPlayer) {
            return new ArrayList<>();
        }
        
        List<Position> moves = getPossibleMoves(from);
        
        // Filter out moves that would leave king in check
        moves.removeIf(to -> wouldBeInCheck(from, to, currentPlayer));
        
        return moves;
    }
    
    /**
     * Get all possible moves (without check validation)
     */
    private List<Position> getPossibleMoves(Position from) {
        ChessPiece piece = getPieceAt(from);
        if (piece == null) return new ArrayList<>();
        
        switch (piece.getType()) {
            case PAWN:
                return getPawnMoves(from, piece.getColor());
            case ROOK:
                return getRookMoves(from, piece.getColor());
            case KNIGHT:
                return getKnightMoves(from, piece.getColor());
            case BISHOP:
                return getBishopMoves(from, piece.getColor());
            case QUEEN:
                return getQueenMoves(from, piece.getColor());
            case KING:
                return getKingMoves(from, piece.getColor());
            default:
                return new ArrayList<>();
        }
    }
    
    /**
     * Get pawn moves
     */
    private List<Position> getPawnMoves(Position from, Color color) {
        List<Position> moves = new ArrayList<>();
        int direction = (color == Color.WHITE) ? -1 : 1;
        int startRow = (color == Color.WHITE) ? 6 : 1;
        
        // Move forward one square
        Position forward = from.offset(direction, 0);
        if (forward.isValid() && getPieceAt(forward) == null) {
            moves.add(forward);
            
            // Move forward two squares from starting position
            if (from.getRow() == startRow) {
                Position forward2 = from.offset(2 * direction, 0);
                if (getPieceAt(forward2) == null) {
                    moves.add(forward2);
                }
            }
        }
        
        // Capture diagonally
        for (int colOffset : new int[]{-1, 1}) {
            Position diagonal = from.offset(direction, colOffset);
            if (diagonal.isValid()) {
                ChessPiece target = getPieceAt(diagonal);
                if (target != null && target.getColor() != color) {
                    moves.add(diagonal);
                }
                
                // En passant
                if (enPassantTarget != null && diagonal.equals(enPassantTarget)) {
                    moves.add(diagonal);
                }
            }
        }
        
        return moves;
    }
    
    /**
     * Get rook moves
     */
    private List<Position> getRookMoves(Position from, Color color) {
        return getSlidingMoves(from, color, new int[][]{{0,1}, {0,-1}, {1,0}, {-1,0}});
    }
    
    /**
     * Get bishop moves
     */
    private List<Position> getBishopMoves(Position from, Color color) {
        return getSlidingMoves(from, color, new int[][]{{1,1}, {1,-1}, {-1,1}, {-1,-1}});
    }
    
    /**
     * Get queen moves
     */
    private List<Position> getQueenMoves(Position from, Color color) {
        List<Position> moves = new ArrayList<>();
        moves.addAll(getRookMoves(from, color));
        moves.addAll(getBishopMoves(from, color));
        return moves;
    }
    
    /**
     * Get sliding moves (for rook, bishop, queen)
     */
    private List<Position> getSlidingMoves(Position from, Color color, int[][] directions) {
        List<Position> moves = new ArrayList<>();
        
        for (int[] dir : directions) {
            for (int i = 1; i < 8; i++) {
                Position pos = from.offset(dir[0] * i, dir[1] * i);
                if (!pos.isValid()) break;
                
                ChessPiece target = getPieceAt(pos);
                if (target == null) {
                    moves.add(pos);
                } else {
                    if (target.getColor() != color) {
                        moves.add(pos);
                    }
                    break;
                }
            }
        }
        
        return moves;
    }
    
    /**
     * Get knight moves
     */
    private List<Position> getKnightMoves(Position from, Color color) {
        List<Position> moves = new ArrayList<>();
        int[][] knightMoves = {{-2,-1}, {-2,1}, {-1,-2}, {-1,2}, 
                               {1,-2}, {1,2}, {2,-1}, {2,1}};
        
        for (int[] move : knightMoves) {
            Position pos = from.offset(move[0], move[1]);
            if (pos.isValid()) {
                ChessPiece target = getPieceAt(pos);
                if (target == null || target.getColor() != color) {
                    moves.add(pos);
                }
            }
        }
        
        return moves;
    }
    
    /**
     * Get king moves
     */
    private List<Position> getKingMoves(Position from, Color color) {
        List<Position> moves = new ArrayList<>();
        int[][] directions = {{-1,-1}, {-1,0}, {-1,1}, {0,-1}, 
                             {0,1}, {1,-1}, {1,0}, {1,1}};
        
        for (int[] dir : directions) {
            Position pos = from.offset(dir[0], dir[1]);
            if (pos.isValid()) {
                ChessPiece target = getPieceAt(pos);
                if (target == null || target.getColor() != color) {
                    moves.add(pos);
                }
            }
        }
        
        // Castling
        if (!isKingInCheck(color)) {
            // King-side castling
            boolean canCastleKingSide = (color == Color.WHITE) ? 
                whiteKingSideCastle : blackKingSideCastle;
            
            if (canCastleKingSide) {
                Position k1 = from.offset(0, 1);
                Position k2 = from.offset(0, 2);
                if (getPieceAt(k1) == null && getPieceAt(k2) == null &&
                    !isSquareUnderAttack(k1, color) && !isSquareUnderAttack(k2, color)) {
                    moves.add(k2);
                }
            }
            
            // Queen-side castling
            boolean canCastleQueenSide = (color == Color.WHITE) ? 
                whiteQueenSideCastle : blackQueenSideCastle;
            
            if (canCastleQueenSide) {
                Position q1 = from.offset(0, -1);
                Position q2 = from.offset(0, -2);
                Position q3 = from.offset(0, -3);
                if (getPieceAt(q1) == null && getPieceAt(q2) == null && 
                    getPieceAt(q3) == null &&
                    !isSquareUnderAttack(q1, color) && !isSquareUnderAttack(q2, color)) {
                    moves.add(q2);
                }
            }
        }
        
        return moves;
    }
    
    /**
     * Make a move on the board
     */
    public boolean makeMove(Position from, Position to) {
        if (!isValidMove(from, to)) {
            return false;
        }
        
        ChessPiece piece = getPieceAt(from);
        ChessPiece captured = getPieceAt(to);
        
        Move.Builder moveBuilder = new Move.Builder()
            .from(from)
            .to(to)
            .piece(piece.copy());
        
        if (captured != null) {
            moveBuilder.capturedPiece(captured.copy());
            if (captured.getColor() == Color.WHITE) {
                capturedWhite.add(captured);
            } else {
                capturedBlack.add(captured);
            }
        }
        
        // Handle en passant
        if (piece.getType() == Piece.PAWN && enPassantTarget != null && to.equals(enPassantTarget)) {
            int captureRow = (piece.getColor() == Color.WHITE) ? to.getRow() + 1 : to.getRow() - 1;
            Position capturePos = new Position(captureRow, to.getCol());
            ChessPiece enPassantCaptured = getPieceAt(capturePos);
            
            if (enPassantCaptured != null) {
                moveBuilder.enPassant(capturePos);
                if (enPassantCaptured.getColor() == Color.WHITE) {
                    capturedWhite.add(enPassantCaptured);
                } else {
                    capturedBlack.add(enPassantCaptured);
                }
                setPieceAt(capturePos, null);
            }
        }
        
        // Handle castling
        if (piece.getType() == Piece.KING && Math.abs(to.getCol() - from.getCol()) == 2) {
            int rookFromCol = (to.getCol() > from.getCol()) ? 7 : 0;
            int rookToCol = (to.getCol() > from.getCol()) ? to.getCol() - 1 : to.getCol() + 1;
            
            Position rookFrom = new Position(from.getRow(), rookFromCol);
            Position rookTo = new Position(from.getRow(), rookToCol);
            
            moveBuilder.castling(rookFrom, rookTo);
            
            ChessPiece rook = getPieceAt(rookFrom);
            setPieceAt(rookTo, rook);
            setPieceAt(rookFrom, null);
            rook.setMoved();
        }
        
        // Update en passant target
        enPassantTarget = null;
        if (piece.getType() == Piece.PAWN && Math.abs(to.getRow() - from.getRow()) == 2) {
            enPassantTarget = new Position((from.getRow() + to.getRow()) / 2, from.getCol());
        }
        
        // Update castling rights
        if (piece.getType() == Piece.KING) {
            if (piece.getColor() == Color.WHITE) {
                whiteKingSideCastle = false;
                whiteQueenSideCastle = false;
            } else {
                blackKingSideCastle = false;
                blackQueenSideCastle = false;
            }
        }
        
        if (piece.getType() == Piece.ROOK) {
            if (piece.getColor() == Color.WHITE) {
                if (from.getCol() == 0) whiteQueenSideCastle = false;
                if (from.getCol() == 7) whiteKingSideCastle = false;
            } else {
                if (from.getCol() == 0) blackQueenSideCastle = false;
                if (from.getCol() == 7) blackKingSideCastle = false;
            }
        }
        
        // Make the move
        setPieceAt(to, piece);
        setPieceAt(from, null);
        piece.setMoved();
        
        // Handle pawn promotion
        if (piece.getType() == Piece.PAWN && (to.getRow() == 0 || to.getRow() == 7)) {
            setPieceAt(to, new ChessPiece(Piece.QUEEN, piece.getColor()));
        }
        
        moveHistory.push(moveBuilder.build());
        
        // Switch player
        currentPlayer = currentPlayer.opposite();
        
        // Update game state
        updateGameState();
        
        return true;
    }
    
    /**
     * Undo the last move
     */
    public boolean undoMove() {
        if (moveHistory.isEmpty()) {
            return false;
        }
        
        Move move = moveHistory.pop();
        Position from = move.getFrom();
        Position to = move.getTo();
        
        // Restore piece
        setPieceAt(from, move.getPiece());
        setPieceAt(to, move.getCapturedPiece());
        
        // Restore en passant capture
        if (move.isEnPassant()) {
            Position capturePos = move.getEnPassantCapture();
            ChessPiece captured = move.getCapturedPiece();
            setPieceAt(capturePos, captured);
            
            if (captured.getColor() == Color.WHITE) {
                capturedWhite.remove(capturedWhite.size() - 1);
            } else {
                capturedBlack.remove(capturedBlack.size() - 1);
            }
        }
        
        // Restore castling
        if (move.isCastling()) {
            Position rookFrom = move.getRookFrom();
            Position rookTo = move.getRookTo();
            ChessPiece rook = getPieceAt(rookTo);
            setPieceAt(rookFrom, rook);
            setPieceAt(rookTo, null);
        }
        
        // Restore captured piece
        if (move.getCapturedPiece() != null && !move.isEnPassant()) {
            ChessPiece captured = move.getCapturedPiece();
            if (captured.getColor() == Color.WHITE) {
                capturedWhite.remove(capturedWhite.size() - 1);
            } else {
                capturedBlack.remove(capturedBlack.size() - 1);
            }
        }
        
        // Switch player back
        currentPlayer = currentPlayer.opposite();
        gameState = GameState.IN_PROGRESS;
        
        return true;
    }
    
    /**
     * Find king position for given color
     */
    private Position findKing(Color color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position pos = new Position(row, col);
                ChessPiece piece = getPieceAt(pos);
                if (piece != null && piece.getType() == Piece.KING && piece.getColor() == color) {
                    return pos;
                }
            }
        }
        return null;
    }
    
    /**
     * Check if king is in check
     */
    public boolean isKingInCheck(Color color) {
        Position kingPos = findKing(color);
        if (kingPos == null) return false;
        return isSquareUnderAttack(kingPos, color);
    }
    
    /**
     * Check if a square is under attack
     */
    private boolean isSquareUnderAttack(Position pos, Color defenderColor) {
        Color attackerColor = defenderColor.opposite();
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position attackerPos = new Position(row, col);
                ChessPiece attacker = getPieceAt(attackerPos);
                
                if (attacker != null && attacker.getColor() == attackerColor) {
                    List<Position> attacks = getAttackSquares(attackerPos, attacker);
                    if (attacks.contains(pos)) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * Get squares that a piece attacks
     */
    private List<Position> getAttackSquares(Position from, ChessPiece piece) {
        if (piece.getType() == Piece.PAWN) {
            return getPawnAttacks(from, piece.getColor());
        } else if (piece.getType() == Piece.KING) {
            return getKingAttacks(from);
        } else {
            return getPossibleMoves(from);
        }
    }
    
    /**
     * Get pawn attack squares
     */
    private List<Position> getPawnAttacks(Position from, Color color) {
        List<Position> attacks = new ArrayList<>();
        int direction = (color == Color.WHITE) ? -1 : 1;
        
        for (int colOffset : new int[]{-1, 1}) {
            Position pos = from.offset(direction, colOffset);
            if (pos.isValid()) {
                attacks.add(pos);
            }
        }
        
        return attacks;
    }
    
    /**
     * Get king attack squares
     */
    private List<Position> getKingAttacks(Position from) {
        List<Position> attacks = new ArrayList<>();
        int[][] directions = {{-1,-1}, {-1,0}, {-1,1}, {0,-1}, 
                             {0,1}, {1,-1}, {1,0}, {1,1}};
        
        for (int[] dir : directions) {
            Position pos = from.offset(dir[0], dir[1]);
            if (pos.isValid()) {
                attacks.add(pos);
            }
        }
        
        return attacks;
    }
    
    /**
     * Check if a move would leave king in check
     */
    private boolean wouldBeInCheck(Position from, Position to, Color color) {
        // Simulate the move
        ChessPiece piece = getPieceAt(from);
        ChessPiece captured = getPieceAt(to);
        
        setPieceAt(to, piece);
        setPieceAt(from, null);
        
        boolean inCheck = isKingInCheck(color);
        
        // Undo simulation
        setPieceAt(from, piece);
        setPieceAt(to, captured);
        
        return inCheck;
    }
    
    /**
     * Check if player has any valid moves
     */
    private boolean hasAnyValidMoves(Color color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position pos = new Position(row, col);
                ChessPiece piece = getPieceAt(pos);
                
                if (piece != null && piece.getColor() == color) {
                    List<Position> moves = getValidMoves(pos);
                    if (!moves.isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * Update game state
     */
    private void updateGameState() {
        boolean hasValidMoves = hasAnyValidMoves(currentPlayer);
        boolean inCheck = isKingInCheck(currentPlayer);
        
        if (!hasValidMoves) {
            if (inCheck) {
                gameState = GameState.CHECKMATE;
            } else {
                gameState = GameState.STALEMATE;
            }
        } else if (inCheck) {
            gameState = GameState.CHECK;
        } else {
            gameState = GameState.IN_PROGRESS;
        }
    }
    
    /**
     * Reset the board to initial state
     */
    public void reset() {
        board = new ChessPiece[8][8];
        currentPlayer = Color.WHITE;
        moveHistory.clear();
        capturedWhite.clear();
        capturedBlack.clear();
        enPassantTarget = null;
        whiteKingSideCastle = true;
        whiteQueenSideCastle = true;
        blackKingSideCastle = true;
        blackQueenSideCastle = true;
        gameState = GameState.IN_PROGRESS;
        initializeBoard();
    }
}
