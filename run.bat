@echo off
REM Run script for Chess Game

echo Starting Chess Game...

REM Check if compiled
if not exist bin (
    echo Game not compiled. Running compile script...
    call compile.bat
)

REM Run the game
java -cp bin chess.ChessGUI
