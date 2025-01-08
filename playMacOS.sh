#!/bin/bash
# Check if Gradle is installed
if ! command -v gradle &> /dev/null; then
    echo "Gradle is not installed. Please install Gradle to run the game."
    exit 1
fi

# Run the game
gradle lwjgl3:run
