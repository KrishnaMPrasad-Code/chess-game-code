package chess;

/**
 * Enum representing piece colors
 */
public enum Color {
    WHITE,
    BLACK;
    
    /**
     * Get the opposite color
     */
    public Color opposite() {
        return this == WHITE ? BLACK : WHITE;
    }
    
    @Override
    public String toString() {
        return this == WHITE ? "White" : "Black";
    }
}
