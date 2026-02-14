#!/bin/bash
# Compile script for Chess Game

echo "Compiling Chess Game..."

# Create bin directory if it doesn't exist
mkdir -p bin

# Compile all Java files
javac -d bin src/chess/*.java

if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo "Run the game using: ./run.sh"
else
    echo "Compilation failed!"
    exit 1
fi
