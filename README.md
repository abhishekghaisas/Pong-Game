# Pong-Game
![Java Version](https://img.shields.io/badge/Java-17-brightgreen.svg)
![Status](https://img.shields.io/badge/Status-Completed-success.svg)
![Database](https://img.shields.io/badge/Database-MySQL-blue.svg)
![Framework](https://img.shields.io/badge/UI-Swing-orange.svg)

A Java-based two-player Pong game that recreates the classic table tennis experience with modern features, including customizable settings, user profiles, and score tracking.

<div align="center">
  <img src="docs/images/screenshots/welcome_screen.png" alt="Welcome Screen" width="400">
  <img src="docs/images/screenshots/gameplay.png" alt="Gameplay" width="400">
</div>

## Table of Contents

1. [Introduction](#introduction)
2. [Features](#features)
3. [Technologies Used](#technologies-used)
4. [Architecture](#architecture)
5. [Game States](#game-states)
6. [Setup Instructions](#setup-instructions)
7. [Usage](#usage)
8. [Database Schema](#database-schema)
9. [Challenges Overcome](#challenges-overcome)
10. [Future Enhancements](#future-enhancements)

## Introduction
The Pong Game is a simple yet engaging two-player game where players control paddles to hit a bouncing ball. This project highlights foundational game development concepts and serves as a stepping stone towards exploring advanced mechanics.

## Features:
- **Local Multiplayer:** Play against a friend using keyboard controls.
- **Customizable Settings:** User can select paddle and ball colors, set score limit, and enable sound.
- **User Authentication:** User can create an account and will be authenticated before the game starts.
- **Score Statistics:** User can view their score history.
- **Physics Engine:** Ball movement and paddle collision physics, including a deformation effect on the ball to enhance realism.
- **Sound Effects:** Optional audio for paddle hit.

## Technologies Used

<table>
  <tr>
    <th>Technology</th>
    <th>Purpose</th>
    <th>Implementation Details</th>
  </tr>
  <tr>
    <td>Java</td>
    <td>Core Programming Language</td>
    <td>JDK 17 features, OOP concepts, Builder pattern</td>
  </tr>
  <tr>
    <td>Swing</td>
    <td>GUI Development</td>
    <td>Custom components, event handling, animations</td>
  </tr>
  <tr>
    <td>MySQL</td>
    <td>Database</td>
    <td>User profiles, score tracking, statistics</td>
  </tr>
  <tr>
    <td>JDBC</td>
    <td>Database Connectivity</td>
    <td>Connection pooling, prepared statements</td>
  </tr>
  <tr>
    <td>Gradle</td>
    <td>Build Automation</td>
    <td>Dependency management, build configuration</td>
  </tr>
  <tr>
    <td>JUnit</td>
    <td>Testing</td>
    <td>Unit tests for game physics and logic</td>
  </tr>
</table>

## Architecture

This project follows the Model-View-Controller (MVC) architectural pattern, which separates the application into three interconnected components:

<div align="center">
  <img src="docs/images/mvc_architecture.png" alt="MVC Architecture" width="700">
</div>

### Key Components:
- **Model**: Represents the game state and business logic
- **View**: Handles UI rendering and user input
- **Controller**: Acts as an intermediary between Model and View
- **Database Layer**: Manages persistent data storage
- **Physics Engine**: Implements game physics for realistic ball movement

## Game States

The game transitions through various game states to manage the flow of gameplay and user interaction:

| Game State | Main Function |
| --- | --- |
| START | Displays the game intro screen. |
| Sign Up | Players create accounts by providing username, email, and password. |
| Login | Players log in using their username and password. |
| SETTINGS | Allows customization of ball colors, paddle colors, score limits, and sound settings. |
| GAMEPLAY | Main game state where players control paddles, the ball moves with physics, and scores are tracked. |
| PAUSE | Provides pause functionality with options to resume gameplay or restart the game. |
| GAME_OVER | Displays final scores, announces the winner, and shows player history. |

### State Transition Flow
1. **START → Login/Sign Up**: Players authenticate before playing
2. **Login/Sign Up → SETTINGS**: Players customize game settings
3. **SETTINGS → GAMEPLAY**: Game begins with chosen settings
4. **GAMEPLAY ↔ PAUSE**: Game can be paused and resumed
5. **GAMEPLAY → GAME_OVER**: Game ends when score limit is reached
6. **GAME_OVER → START**: Players can start a new game

## Game Screens

### Login & Sign Up
<div align="center">
  <img src="docs/images/screenshots/login_and_signup.png" alt="Login Screen" width="600">
</div>


### Settings
The settings screen allows players to customize their game experience:
<div align="center">
  <img src="docs/images/screenshots/settings_screen.png" alt="Settings Screen" width="600">
</div>

### Gameplay
<div align="center">
  <img src="docs/images/screenshots/gameplay.png" alt="Gameplay" width="600">
</div>

## Setup Instructions

### Prerequisites:
- Java Development Kit (JDK) 17
- MySQL database

### Steps:

1. Clone the repository:
```sh
git clone https://github.com/anusha24n/PongGame.git
cd pong-game
```

2. Set up the database:
   - Go to the configuration folder and run the `run_sql_script.sh` script.
   - Enter your MySQL root password when prompted.
   - The script executes the `mysql-setup.sql` file to set up the pongdb database.

3. Build the project:
```sh
./gradlew build
```

4. Run the application:
```sh
./gradlew run
```

### Debugging Database Setup

If you encounter errors while running the SQL script:
1. Access the MySQL console:
   ```sh
   mysql -u root -p
   ```

2. Adjust password validation settings:
   ```sql
   SHOW VARIABLES LIKE 'validate_password%';
   SET GLOBAL validate_password.policy = LOW;
   ```

3. Exit MySQL and retry the script:
   ```sh
   sh run_sql_script.sh
   ```

## Usage

1. **Launch the Game**: Run the application using Gradle or your IDE.
2. **Login/Signup:**
   - When you start the game, you'll be taken to the Login Screen.
   - Enter credentials if you're an existing user or sign up to create a new account.
   - Demo credentials: username: `emily_freeman`, password: `password123`

3. **Settings Screen:**
    - Customize the ball and paddle colors and set the score limit.
    - Enable/disable sound effects.
    - Click "Start Game" to begin playing.

4. **Gameplay:**
   - Player 1 Controls: W (up) and S (down)
   - Player 2 Controls: ↑ (up) and ↓ (down)
   - The game ends when a player reaches the score limit.

5. **Score History:**
   - After the game, view score history for each player.

## Database Schema

The game uses a MySQL database to store user profiles and game scores:

<div align="center">
  <img src="docs/images/database_schema.png" alt="Database Schema" width="700">
</div>

### Key Tables:
- **users**: Stores user accounts and authentication information
- **scores**: Records game outcomes, opponents, and timestamps

## Challenges Overcome

### Physics Engine Implementation
- Developed a realistic collision detection system that accurately calculates ball trajectories
- Implemented ball deformation effects to enhance visual feedback
- Optimized physics calculations to maintain consistent 60 FPS

### Database Integration
- Created a flexible data model to support user profiles and statistics
- Implemented efficient JDBC connection pooling to minimize connection overhead
- Developed a robust error handling system for database operations

### UI Design
- Designed an intuitive interface with clear user flow between game states
- Implemented dynamic rendering for various screen resolutions
- Created responsive controls with accurate input handling

## Future Enhancements

- **Online Multiplayer**: Add networked gameplay support
- **AI Opponent**: Implement computer player with adjustable difficulty
- **Advanced Customization**: Additional paddle types and power-ups
- **Leaderboards**: Global rankings and achievements
- **Mobile Version**: Port to Android and iOS

## Course Information
This project was developed as part of the Application Engineering and Development course in the MS IS program. It demonstrates skills in:

- Object-Oriented Programming
- UI/UX Design
- Database Design and Management
- Software Architecture
- Game Development Principles
