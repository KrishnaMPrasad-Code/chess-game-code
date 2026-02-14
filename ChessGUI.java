package chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * GUI for the chess game using Swing
 */
public class ChessGUI extends JFrame {
    private static final int BOARD_SIZE = 600;
    private static final int SQUARE_SIZE = BOARD_SIZE / 8;
    private static final Color LIGHT_SQUARE = new Color(240, 217, 181);
    private static final Color DARK_SQUARE = new Color(181, 136, 99);
    private static final Color SELECTED_COLOR = new Color(186, 202, 68);
    private static final Color VALID_MOVE_COLOR = new Color(0, 255, 0, 100);
    private static final Color VALID_CAPTURE_COLOR = new Color(255, 0, 0, 100);
    private static final Color CHECK_COLOR = new Color(255, 107, 107);
    
    private ChessBoard chessBoard;
    private JPanel boardPanel;
    private JButton[][] squares;
    private Position selectedSquare;
    private List<Position> validMoves;
    private JLabel statusLabel;
    private JLabel turnLabel;
    private JPanel capturedWhitePanel;
    private JPanel capturedBlackPanel;
    private JTextArea moveHistoryArea;
    private boolean showHints = true;
    
    public ChessGUI() {
        chessBoard = new ChessBoard();
        selectedSquare = null;
        validMoves = null;
        
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        // Create menu bar
        createMenuBar();
        
        // Create top panel with game info
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);
        
        // Create board panel
        boardPanel = createBoardPanel();
        add(boardPanel, BorderLayout.CENTER);
        
        // Create right panel with move history and captured pieces
        JPanel rightPanel = createRightPanel();
        add(rightPanel, BorderLayout.EAST);
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        
        updateBoard();
    }
    
    /**
     * Create menu bar
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu gameMenu = new JMenu("Game");
        
        JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(e -> newGame());
        gameMenu.add(newGameItem);
        
        JMenuItem undoItem = new JMenuItem("Undo Move");
        undoItem.addActionListener(e -> undoMove());
        gameMenu.add(undoItem);
        
        gameMenu.addSeparator();
        
        JCheckBoxMenuItem hintsItem = new JCheckBoxMenuItem("Show Hints", true);
        hintsItem.addActionListener(e -> {
            showHints = hintsItem.isSelected();
            updateBoard();
        });
        gameMenu.add(hintsItem);
        
        gameMenu.addSeparator();
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        gameMenu.add(exitItem);
        
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
    }
    
    /**
     * Create top panel with game status
     */
    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        
        turnLabel = new JLabel("White's Turn", SwingConstants.CENTER);
        turnLabel.setFont(new Font("Arial", Font.BOLD, 18));
        turnLabel.setForeground(new Color(102, 126, 234));
        panel.add(turnLabel);
        
        statusLabel = new JLabel("Game in Progress", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel.setForeground(new Color(118, 75, 162));
        panel.add(statusLabel);
        
        return panel;
    }
    
    /**
     * Create chess board panel
     */
    private JPanel createBoardPanel() {
        JPanel panel = new JPanel(new GridLayout(8, 8));
        panel.setPreferredSize(new Dimension(BOARD_SIZE, BOARD_SIZE));
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        
        squares = new JButton[8][8];
        
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton square = new JButton();
                square.setFont(new Font("Arial Unicode MS", Font.PLAIN, 48));
                square.setFocusPainted(false);
                square.setBorderPainted(false);
                
                Color squareColor = (row + col) % 2 == 0 ? LIGHT_SQUARE : DARK_SQUARE;
                square.setBackground(squareColor);
                
                final int r = row;
                final int c = col;
                square.addActionListener(e -> handleSquareClick(new Position(r, c)));
                
                squares[row][col] = square;
                panel.add(square);
            }
        }
        
        return panel;
    }
    
    /**
     * Create right panel with move history and captured pieces
     */
    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 10));
        panel.setPreferredSize(new Dimension(250, BOARD_SIZE));
        
        // Captured pieces panels
        JPanel capturedPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        
        capturedWhitePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        capturedWhitePanel.setBorder(BorderFactory.createTitledBorder("Captured White"));
        capturedWhitePanel.setBackground(Color.WHITE);
        capturedPanel.add(capturedWhitePanel);
        
        capturedBlackPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        capturedBlackPanel.setBorder(BorderFactory.createTitledBorder("Captured Black"));
        capturedBlackPanel.setBackground(Color.WHITE);
        capturedPanel.add(capturedBlackPanel);
        
        panel.add(capturedPanel, BorderLayout.NORTH);
        
        // Move history
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBorder(BorderFactory.createTitledBorder("Move History"));
        
        moveHistoryArea = new JTextArea();
        moveHistoryArea.setEditable(false);
        moveHistoryArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(moveHistoryArea);
        historyPanel.add(scrollPane, BorderLayout.CENTER);
        
        panel.add(historyPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Handle square click
     */
    private void handleSquareClick(Position pos) {
        ChessPiece clickedPiece = chessBoard.getPieceAt(pos);
        
        // If a square is already selected
        if (selectedSquare != null) {
            // Try to make a move
            if (validMoves != null && validMoves.contains(pos)) {
                if (chessBoard.makeMove(selectedSquare, pos)) {
                    selectedSquare = null;
                    validMoves = null;
                    updateBoard();
                    updateGameStatus();
                    return;
                }
            }
            
            // Deselect if clicking same square
            if (selectedSquare.equals(pos)) {
                selectedSquare = null;
                validMoves = null;
                updateBoard();
                return;
            }
        }
        
        // Select a piece if it's current player's piece
        if (clickedPiece != null && clickedPiece.getColor() == chessBoard.getCurrentPlayer()) {
            selectedSquare = pos;
            validMoves = chessBoard.getValidMoves(pos);
            updateBoard();
        }
    }
    
    /**
     * Update the board display
     */
    private void updateBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Position pos = new Position(row, col);
                JButton square = squares[row][col];
                
                // Reset background
                Color squareColor = (row + col) % 2 == 0 ? LIGHT_SQUARE : DARK_SQUARE;
                square.setBackground(squareColor);
                
                // Set piece
                ChessPiece piece = chessBoard.getPieceAt(pos);
                if (piece != null) {
                    square.setText(piece.getUnicodeSymbol());
                    
                    // Highlight king in check
                    if (piece.getType() == Piece.KING && chessBoard.isKingInCheck(piece.getColor())) {
                        square.setBackground(CHECK_COLOR);
                    }
                } else {
                    square.setText("");
                }
                
                // Highlight selected square
                if (selectedSquare != null && selectedSquare.equals(pos)) {
                    square.setBackground(SELECTED_COLOR);
                }
                
                // Highlight valid moves
                if (showHints && validMoves != null && validMoves.contains(pos)) {
                    if (piece != null) {
                        square.setBackground(VALID_CAPTURE_COLOR);
                    } else {
                        square.setBackground(VALID_MOVE_COLOR);
                    }
                }
            }
        }
        
        updateCapturedPieces();
        updateMoveHistory();
    }
    
    /**
     * Update game status display
     */
    private void updateGameStatus() {
        Color currentPlayer = chessBoard.getCurrentPlayer();
        turnLabel.setText(currentPlayer + "'s Turn");
        
        ChessBoard.GameState state = chessBoard.getGameState();
        switch (state) {
            case IN_PROGRESS:
                statusLabel.setText("Game in Progress");
                statusLabel.setForeground(new Color(118, 75, 162));
                break;
            case CHECK:
                statusLabel.setText(currentPlayer + " is in Check!");
                statusLabel.setForeground(Color.RED);
                break;
            case CHECKMATE:
                Color winner = currentPlayer.opposite();
                statusLabel.setText("Checkmate! " + winner + " wins!");
                statusLabel.setForeground(new Color(0, 128, 0));
                break;
            case STALEMATE:
                statusLabel.setText("Stalemate! Draw!");
                statusLabel.setForeground(Color.BLUE);
                break;
        }
    }
    
    /**
     * Update captured pieces display
     */
    private void updateCapturedPieces() {
        capturedWhitePanel.removeAll();
        for (ChessPiece piece : chessBoard.getCapturedWhite()) {
            JLabel label = new JLabel(piece.getUnicodeSymbol());
            label.setFont(new Font("Arial Unicode MS", Font.PLAIN, 24));
            capturedWhitePanel.add(label);
        }
        capturedWhitePanel.revalidate();
        capturedWhitePanel.repaint();
        
        capturedBlackPanel.removeAll();
        for (ChessPiece piece : chessBoard.getCapturedBlack()) {
            JLabel label = new JLabel(piece.getUnicodeSymbol());
            label.setFont(new Font("Arial Unicode MS", Font.PLAIN, 24));
            capturedBlackPanel.add(label);
        }
        capturedBlackPanel.revalidate();
        capturedBlackPanel.repaint();
    }
    
    /**
     * Update move history display
     */
    private void updateMoveHistory() {
        StringBuilder sb = new StringBuilder();
        List<Move> moves = chessBoard.getMoveHistory();
        
        for (int i = 0; i < moves.size(); i++) {
            if (i % 2 == 0) {
                sb.append(String.format("%d. ", (i / 2) + 1));
            }
            sb.append(moves.get(i).toString());
            if (i % 2 == 0) {
                sb.append(" ");
            } else {
                sb.append("\n");
            }
        }
        
        moveHistoryArea.setText(sb.toString());
        moveHistoryArea.setCaretPosition(moveHistoryArea.getDocument().getLength());
    }
    
    /**
     * Start a new game
     */
    private void newGame() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to start a new game?",
            "New Game",
            JOptionPane.YES_NO_OPTION
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            chessBoard.reset();
            selectedSquare = null;
            validMoves = null;
            updateBoard();
            updateGameStatus();
        }
    }
    
    /**
     * Undo last move
     */
    private void undoMove() {
        if (chessBoard.undoMove()) {
            selectedSquare = null;
            validMoves = null;
            updateBoard();
            updateGameStatus();
        } else {
            JOptionPane.showMessageDialog(
                this,
                "No moves to undo!",
                "Undo Move",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
    
    /**
     * Main method to start the game
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChessGUI());
    }
}
