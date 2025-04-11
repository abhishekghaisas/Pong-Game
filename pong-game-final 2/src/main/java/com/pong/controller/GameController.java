package com.pong.controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.Timer;
import com.pong.model.GameModel;
import com.pong.model.Settings;
import com.pong.model.User;
import com.pong.physics.PhysicsEngine;
import com.pong.view.GameView;
import com.pong.view.ScoreStatistics;


/**
 * GameController handles the game loop, user input, and communication between the model and view.
 * It processes key events, updates game states, and manages game flow (start, pause, end).
 */
public class GameController implements ActionListener, KeyListener {
    // MVC Components
    private final GameModel model;
    private final GameView view;
    private final Settings settings;
    private final JFrame frame;
    private User player1;
    private User player2;
    private final PhysicsEngine physicsEngine;

    // Paddle movement flags (user input tracking)
    private boolean paddle1Up = false, paddle1Down = false;
    private boolean paddle2Up = false, paddle2Down = false;

    // Timer controlling the game loop execution
    private final Timer timer; // Triggers actionPerformed at fixed intervals
    private boolean isGamePaused = false;

    public GameController(GameView view, JFrame frame, User player1, User player2, Settings settings) {
        this.view = view;
        this.frame = frame;
        this.player1 = player1;
        this.player2 = player2;
        this.settings = settings;

        // Set dimensions for game elements
        int paddleWidth = 10;
        int paddleHeight = 100;
        int ballSize = 20;
        int maxScore= settings.getScoreLimit();

        // Initialize the game model with dimensions and element sizes
        this.model = new GameModel(
                view.getPreferredSize().width,
                view.getPreferredSize().height,
                paddleWidth,
                paddleHeight,
                ballSize,
                maxScore
        );

        // Physics parameters
        int initialSpeed = 5;
        int maxDeformationFrames = 5;

        // Initialize the physics engine with the model and parameters
        this.physicsEngine = new PhysicsEngine(model, initialSpeed, maxDeformationFrames, settings.isEnableSound());

        // Start the game loop timer (approximately 60 frames per second)
        timer = new Timer(16, this);
        timer.start();
    }

    /**
     * Main game loop executed every timer tick.
     *
     * @param e ActionEvent from the timer
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGamePaused) {
            // Update paddle positions based on current key presses
            updatePaddlePositions();

            // Update ball position using physics calculations
            physicsEngine.updateBallPosition();

            // Handle collisions and check for scoring
            int scorer = physicsEngine.handleCollisions();

            // Update scores and reset the ball if a player scored
            if (scorer == 1) {
                model.player1Score++;
                physicsEngine.resetBall();
            } else if (scorer == 2) {
                model.player2Score++;
                physicsEngine.resetBall();
            }

            // Decrease deformation frames for the ball (visual effect)
            physicsEngine.decreaseDeformationFrames();

            // *** Corrected: Check if a player has reached the maxScore in the model ***
            if (model.player1Score >= model.getMaxScore() || model.player2Score >= model.getMaxScore()) {
                endGame();
            }
        }

        // Update the view to reflect the new game state
        view.updateGameState(model);
    }

    /**
     * Updates the positions of paddles based on current key presses.
     * Uses the coordinate system where (0,0) is at the top-left corner.
     * Increasing y moves down, decreasing y moves up.
     */
    private void updatePaddlePositions() {

        int paddleSpeed = 8;

        model.paddle1Speed = 0;
        model.paddle2Speed = 0;

        // Player 1 paddle movement
        if (paddle1Up) {
            // Move paddle up by decreasing y-coordinate
            model.paddle1Y -= paddleSpeed;
            model.paddle1Speed = -paddleSpeed;
            // Ensure paddle doesn't move off the top of the screen
            if (model.paddle1Y < 0) model.paddle1Y = 0;
        }
        if (paddle1Down) {
            // Move paddle down by increasing y-coordinate
            model.paddle1Y += paddleSpeed;
            model.paddle1Speed = paddleSpeed;
            // Ensure paddle doesn't move off the bottom of the screen
            if (model.paddle1Y + model.paddleHeight > model.height) model.paddle1Y = model.height - model.paddleHeight;
        }

        // Player 2 paddle movement
        if (paddle2Up) {
            model.paddle2Y -= paddleSpeed;
            model.paddle2Speed = -paddleSpeed;
            if (model.paddle2Y < 0) model.paddle2Y = 0;
        }
        if (paddle2Down) {
            model.paddle2Y += paddleSpeed;
            model.paddle2Speed = paddleSpeed;
            if (model.paddle2Y + model.paddleHeight > model.height) model.paddle2Y = model.height - model.paddleHeight;
        }
    }

    /**
     * Ends the game, displays the winner, and returns to the welcome screen.
     */
    private void endGame() {
        timer.stop();

        String winner = (model.player1Score >= model.getMaxScore()) ? this.player1.getUsername() : this.player2
                .getUsername();
        ScoreStatistics statsPanel = new ScoreStatistics(model.player1Score, model.player2Score, player1, player2);
        frame.getContentPane().removeAll();
        frame.add(statsPanel);
        frame.revalidate();
        frame.repaint();


        frame.getContentPane().removeAll();
        frame.add(statsPanel);
        frame.revalidate();
        frame.repaint();
        frame.requestFocus();
    }

    private void restartGame() {
        // Reset all game variables
        model.paddle1Y = model.height / 2 - model.paddleHeight / 2;
        model.paddle2Y = model.height / 2 - model.paddleHeight / 2;
        model.player1Score = 0;
        model.player2Score = 0;
        physicsEngine.resetBall();
    }

    /**
     * Handles key press events to update paddle movement flags.
     * Update movement flags based on key pressed
     * @param e KeyEvent
     */
    @Override
    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_W) {
            paddle1Up = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            paddle1Down = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            paddle2Up = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            paddle2Down = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_R) {
            restartGame();
        }
        if (e.getKeyCode() == KeyEvent.VK_P) {
            isGamePaused = !isGamePaused;
    }
    }
    /**
     * Handles key release events to update paddle movement flags.
     * Update movement flags based on key released
     *
     * @param e KeyEvent
     */
    @Override
    public void keyReleased(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_W) {
            paddle1Up = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            paddle1Down = false;
        }

        if (e.getKeyCode() == KeyEvent.VK_UP) {
            paddle2Up = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            paddle2Down = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used but required by KeyListener interface
    }
}