package chess;

/**
 * Represents a chess move
 */
public class Move {
    private final Position from;
    private final Position to;
    private final ChessPiece piece;
    private final ChessPiece capturedPiece;
    private final boolean isEnPassant;
    private final boolean isCastling;
    private final Position enPassantCapture;
    private final Position rookFrom;
    private final Position rookTo;
    
    private Move(Builder builder) {
        this.from = builder.from;
        this.to = builder.to;
        this.piece = builder.piece;
        this.capturedPiece = builder.capturedPiece;
        this.isEnPassant = builder.isEnPassant;
        this.isCastling = builder.isCastling;
        this.enPassantCapture = builder.enPassantCapture;
        this.rookFrom = builder.rookFrom;
        this.rookTo = builder.rookTo;
    }
    
    public Position getFrom() {
        return from;
    }
    
    public Position getTo() {
        return to;
    }
    
    public ChessPiece getPiece() {
        return piece;
    }
    
    public ChessPiece getCapturedPiece() {
        return capturedPiece;
    }
    
    public boolean isEnPassant() {
        return isEnPassant;
    }
    
    public boolean isCastling() {
        return isCastling;
    }
    
    public Position getEnPassantCapture() {
        return enPassantCapture;
    }
    
    public Position getRookFrom() {
        return rookFrom;
    }
    
    public Position getRookTo() {
        return rookTo;
    }
    
    @Override
    public String toString() {
        return from.toAlgebraic() + "-" + to.toAlgebraic();
    }
    
    /**
     * Builder for Move class
     */
    public static class Builder {
        private Position from;
        private Position to;
        private ChessPiece piece;
        private ChessPiece capturedPiece;
        private boolean isEnPassant = false;
        private boolean isCastling = false;
        private Position enPassantCapture;
        private Position rookFrom;
        private Position rookTo;
        
        public Builder from(Position from) {
            this.from = from;
            return this;
        }
        
        public Builder to(Position to) {
            this.to = to;
            return this;
        }
        
        public Builder piece(ChessPiece piece) {
            this.piece = piece;
            return this;
        }
        
        public Builder capturedPiece(ChessPiece capturedPiece) {
            this.capturedPiece = capturedPiece;
            return this;
        }
        
        public Builder enPassant(Position enPassantCapture) {
            this.isEnPassant = true;
            this.enPassantCapture = enPassantCapture;
            return this;
        }
        
        public Builder castling(Position rookFrom, Position rookTo) {
            this.isCastling = true;
            this.rookFrom = rookFrom;
            this.rookTo = rookTo;
            return this;
        }
        
        public Move build() {
            return new Move(this);
        }
    }
}
