# Chess Game - Java Edition

A fully functional chess game built with Java and Swing GUI. Play against another player locally with complete chess rules implementation.

## Features

### Core Gameplay
- ✅ Full chess rules implementation
- ✅ All piece movements (Pawn, Rook, Knight, Bishop, Queen, King)
- ✅ Special moves:
  - Castling (King-side and Queen-side)
  - En Passant capture
  - Pawn promotion (auto-promotes to Queen)
- ✅ Check and Checkmate detection
- ✅ Stalemate detection
- ✅ Legal move validation (prevents moves that would leave king in check)

### User Interface
- 🎨 Beautiful Swing GUI with custom colors
- 👁️ Visual move hints (toggle-able via menu)
- 🎯 Highlighted valid moves and captures
- ⚠️ King in check indicator
- 📊 Move history tracker
- 🏆 Captured pieces display
- 🖱️ Click-to-select and move interface

### Game Controls
- **New Game**: Start a fresh game (File → New Game)
- **Undo Move**: Take back the last move (File → Undo Move)
- **Toggle Hints**: Show/hide valid move indicators (File → Show Hints)

## Requirements

- Java Development Kit (JDK) 8 or higher
- No external dependencies required (uses only Java standard library)

## Installation & Running

### Option 1: Using Command Line

#### Compile the game:
```bash
# Navigate to the project directory
cd chess-java

# Compile all Java files
javac -d bin src/chess/*.java

# Run the game
java -cp bin chess.ChessGUI
```

### Option 2: Using the provided scripts

#### On Linux/Mac:
```bash
chmod +x compile.sh run.sh
./compile.sh
./run.sh
```

#### On Windows:
```batch
compile.bat
run.bat
```

### Option 3: Using an IDE

1. Open the project in your favorite IDE (IntelliJ IDEA, Eclipse, NetBeans, VS Code)
2. Set the source folder to `src`
3. Run the `ChessGUI` class

## How to Play

1. **Select a piece**: Click on any piece of the current player's color
2. **Move the piece**: Click on a highlighted square to move
3. **Valid moves**: Green highlights show possible moves, red shows captures
4. **Special moves**:
   - **Castling**: Click king, then click two squares toward rook
   - **En Passant**: Available immediately after opponent's pawn moves two squares
   - **Promotion**: Pawns automatically promote to Queens at the end

## File Structure

```
chess-java/
│
├── src/
│   └── chess/
│       ├── ChessBoard.java      # Main game logic
│       ├── ChessGUI.java        # Swing GUI interface
│       ├── ChessPiece.java      # Piece representation
│       ├── Position.java        # Board position
│       ├── Move.java            # Move representation
│       ├── Piece.java           # Piece type enum
│       └── Color.java           # Color enum
│
├── compile.sh                   # Linux/Mac compile script
├── run.sh                       # Linux/Mac run script
├── compile.bat                  # Windows compile script
├── run.bat                      # Windows run script
└── README.md                    # This file
```

## Class Overview

### ChessBoard
Main game logic class that handles:
- Board state management
- Move validation
- Check/Checkmate detection
- Game rules enforcement

### ChessGUI
Swing-based graphical user interface that provides:
- Interactive chess board
- Move visualization
- Game status display
- User controls

### ChessPiece
Represents a chess piece with:
- Piece type (King, Queen, etc.)
- Color (White/Black)
- Movement tracking

### Position
Represents board coordinates with:
- Row and column indices
- Algebraic notation conversion
- Validation methods

### Move
Represents a chess move with:
- Source and destination positions
- Captured piece information
- Special move flags (castling, en passant)

## Technical Details

### Technologies Used
- **Java 8+**: Core language
- **Swing**: GUI framework
- **AWT**: Graphics and event handling

### Design Patterns
- **MVC Pattern**: Separation of game logic (Model) and GUI (View/Controller)
- **Builder Pattern**: Used in Move class construction
- **Enum Pattern**: Type-safe piece and color representation

### Game Rules Implemented
1. **All Standard Moves**: Complete piece movement rules
2. **Castling**: King-side and queen-side with proper conditions
3. **En Passant**: Special pawn capture
4. **Pawn Promotion**: Automatic promotion to queen
5. **Check Detection**: Prevents illegal moves
6. **Checkmate/Stalemate**: Proper game ending conditions

## Future Enhancements

Potential features for future versions:
- [ ] AI opponent (Minimax algorithm)
- [ ] Timed games (chess clock)
- [ ] Save/Load game functionality
- [ ] Multiple board themes
- [ ] Sound effects
- [ ] Algebraic notation display
- [ ] Move suggestions and hints
- [ ] Game analysis
- [ ] Online multiplayer
- [ ] Pawn promotion piece selection

## Contributing

Feel free to fork this repository and submit pull requests with improvements!

## License

This project is open source and available under the MIT License.

## Author

Created by Krishna M Prasad

## Acknowledgments

- Unicode chess symbols for piece rendering
- Java Swing for cross-platform GUI
- Standard chess rules from FIDE

---

Enjoy playing chess! ♟️
