package chess;

import java.util.Objects;

/**
 * Represents a position on the chess board
 */
public class Position {
    private final int row;
    private final int col;
    
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }
    
    /**
     * Check if position is within board bounds
     */
    public boolean isValid() {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
    
    /**
     * Get position offset by given delta
     */
    public Position offset(int deltaRow, int deltaCol) {
        return new Position(row + deltaRow, col + deltaCol);
    }
    
    /**
     * Convert to algebraic notation (e.g., "e4")
     */
    public String toAlgebraic() {
        return String.valueOf((char)('a' + col)) + (8 - row);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && col == position.col;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
    
    @Override
    public String toString() {
        return toAlgebraic();
    }
}
