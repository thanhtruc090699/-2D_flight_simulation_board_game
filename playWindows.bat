@echo off
:: Check if Gradle is installed
where gradle >nul 2>nul
if errorlevel 1 (
    echo Gradle is not installed. Please install Gradle to run the game.
    pause
    exit /b
)

:: Run the game
gradle lwjgl3:run
pause
