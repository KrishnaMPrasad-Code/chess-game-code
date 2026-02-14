package chess;

/**
 * Enum representing different chess piece types
 */
public enum Piece {
    KING('K'),
    QUEEN('Q'),
    ROOK('R'),
    BISHOP('B'),
    KNIGHT('N'),
    PAWN('P');
    
    private final char symbol;
    
    Piece(char symbol) {
        this.symbol = symbol;
    }
    
    public char getSymbol() {
        return symbol;
    }
    
    /**
     * Get Unicode symbol for display
     */
    public String getUnicodeSymbol(Color color) {
        if (color == Color.WHITE) {
            switch (this) {
                case KING: return "♔";
                case QUEEN: return "♕";
                case ROOK: return "♖";
                case BISHOP: return "♗";
                case KNIGHT: return "♘";
                case PAWN: return "♙";
            }
        } else {
            switch (this) {
                case KING: return "♚";
                case QUEEN: return "♛";
                case ROOK: return "♜";
                case BISHOP: return "♝";
                case KNIGHT: return "♞";
                case PAWN: return "♟";
            }
        }
        return "";
    }
}
