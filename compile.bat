@echo off
REM Compile script for Chess Game

echo Compiling Chess Game...

REM Create bin directory if it doesn't exist
if not exist bin mkdir bin

REM Compile all Java files
javac -d bin src\chess\*.java

if %ERRORLEVEL% EQU 0 (
    echo Compilation successful!
    echo Run the game using: run.bat
) else (
    echo Compilation failed!
    exit /b 1
)
