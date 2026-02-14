package chess;

/**
 * Represents a chess piece with its type and color
 */
public class ChessPiece {
    private final Piece type;
    private final Color color;
    private boolean hasMoved;
    
    public ChessPiece(Piece type, Color color) {
        this.type = type;
        this.color = color;
        this.hasMoved = false;
    }
    
    public ChessPiece(Piece type, Color color, boolean hasMoved) {
        this.type = type;
        this.color = color;
        this.hasMoved = hasMoved;
    }
    
    public Piece getType() {
        return type;
    }
    
    public Color getColor() {
        return color;
    }
    
    public boolean hasMoved() {
        return hasMoved;
    }
    
    public void setMoved() {
        this.hasMoved = true;
    }
    
    public String getUnicodeSymbol() {
        return type.getUnicodeSymbol(color);
    }
    
    /**
     * Create a copy of this piece
     */
    public ChessPiece copy() {
        return new ChessPiece(type, color, hasMoved);
    }
    
    @Override
    public String toString() {
        return color.toString() + " " + type.toString();
    }
}
