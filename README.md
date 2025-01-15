# SkyTeam - libGDX University Project

A 2D flight simulation game developed using [libGDX](https://libgdx.com/).

## Table of Contents

1. Introduction
2. Installation
3. System Requirements
4. Compiling and Running
5. Running the Game
6. Directory Structure
7. Troubleshooting
8. Contributors
9. Additional Documentation

## Introduction

SkyTeam is a LibGDX-based game project developed for our university course in System Development. The game involves a flight simulation with various mechanics, such as managing the plane’s systems, landing, and more. The project is built to run on multiple platforms, including desktop systems.

## Installation

1. **Clone the Repository:**

`git clone https://gitlab.hof-university.de/mariii/skyteam.git`
`cd path/to/skyteam`

2. **Install Dependencies**:

- **Java Development Kit (JDK) version 17 or higher** is required to compile and run this project.
- **Gradle** is used for building and running the project. The project includes a Gradle wrapper, so you do not need to install Gradle separately.

3. **Building the Project**:

- For Windows or MacOS, run the following command from the root directory:

`./gradlew build`

This will build the core project and all platform-specific code (e.g., `lwjgl3` for the desktop platform).

## System Requirements

- **Operating System**: Windows 7 or newer, MacOS (10.12 or later), or Linux.
- **Java**: JDK version 17 or higher (OpenJDK or Oracle JDK).
- **Gradle**: Any recent version of Gradle (the wrapper included in the project ensures compatibility).
- **RAM**: 4GB or more (for smooth performance).
- **Graphics**: Any recent graphics card should be sufficient.


## Compiling and Running

### Gradle Commands

You can use the Gradle wrapper to run various tasks. Some useful tasks include:

- **Build the Project**:

`./gradlew build`

- **Run the Game (Desktop version)**:

`./gradlew lwjgl3:run`

- **Clean the Project (removes build artifacts)**:

`./gradlew clean`

- **Generate Project Files for IDEs**:

  - For IntelliJ IDEA:

    `./gradlew idea`

  - For Eclipse:

    `./gradlew eclipse`

## Running the Game

- **Windows**:
  You can run the game directly on Windows by executing the `playWindows.bat` file found in the root directory.

`playWindows.bat`

- **MacOS**:
  On MacOS, you can use the `playMacOS.sh` shell script to run the game.

`playMacOS.sh`

These scripts set up the environment and run the game instantly.

## Directory Structure

- **core**: Contains the main game logic, shared across all platforms.
- **lwjgl3**: Desktop platform implementation using LWJGL3.
- **assets**: All game assets like images, sounds, and fonts.
- **tests**: Contains unit tests for Java classes, using JUnit for test execution.
- **build.gradle**: Gradle build script.
- **gradlew**: Gradle wrapper (run using `./gradlew`).
- **gradlew.bat**: Gradle wrapper for Windows (run using `gradlew.bat`).
- **playWindows.bat**: Script for running the game on Windows.
- **playMacOS.sh**: Script for running the game on MacOS.

## Troubleshooting
If you encounter issues while running or compiling the project, try the following:

1. **Ensure JDK Version:** Make sure you have JDK version 17 or higher installed. You can check the installed version by running:

`java -version`

2. **Gradle Version Issues:** If you experience compatibility issues with Gradle, try running:

`./gradlew --refresh-dependencies`

3. **Memory Issues:** If the game crashes or runs slowly, ensure that your system meets the minimum requirements, especially RAM.



## Contributors

- **Marija Voloder** – Developer/Student
- **Thi Thanh Truc Trinh** – Developer/Student
- **Rathin** – Developer/Student
- **Thomas Buchmann** – Academic Guidance

## Additional Documentation