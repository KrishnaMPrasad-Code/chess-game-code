#!/bin/bash
# Run script for Chess Game

echo "Starting Chess Game..."

# Check if compiled
if [ ! -d "bin" ]; then
    echo "Game not compiled. Running compile script..."
    ./compile.sh
fi

# Run the game
java -cp bin chess.ChessGUI
