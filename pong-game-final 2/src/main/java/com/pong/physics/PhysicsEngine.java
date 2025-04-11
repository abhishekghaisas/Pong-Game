package com.pong.physics;
import java.awt.Rectangle;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import com.pong.model.GameModel;

/**
 * PhysicsEngine handles all physics calculations for the game,
 * including movement, collision detection, and collision response.
 * It updates the GameModel based on physics rules.
 */
public class PhysicsEngine {

    private final int initialSpeed;
    private final int maxDeformationFrames;
    private final double paddleInfluence;
    private final boolean isPaddleHitClipEnabled;
    private GameModel model;
    private Clip paddleHitClip;

    /**
     * Constructor initializes the physics engine with necessary parameters.
     * @param model                Reference to the GameModel
     * @param initialSpeed         Initial speed of the ball
     * @param maxDeformationFrames Number of frames to show ball deformation after collision
     */
    public PhysicsEngine(GameModel model, int initialSpeed, int maxDeformationFrames, boolean isPaddleHitClipEnabled) {
        this.model = model;
        this.initialSpeed = initialSpeed;
        this.maxDeformationFrames = maxDeformationFrames;
        this.paddleInfluence = 0.35;
        this.isPaddleHitClipEnabled = isPaddleHitClipEnabled;
        resetBall();
        if (isPaddleHitClipEnabled) {
            loadsfx("/sounds/Table-tennis-paddle-ball-hit-901.wav");
        }
    }

    /**
     * Updates the ball's position based on its current speed.
     */
    public void updateBallPosition() {
        model.ballX = model.ballX + (int) model.ballXSpeed;
        model.ballY = model.ballY + (int) model.ballYSpeed;
    }

    /**
     * Handles collision detection and response with walls and paddles.
     *
     * @return Integer indicating which player scored
     * (0 if no score, 1 if Player 1 scored, 2 if Player 2 scored)
     */
    public int handleCollisions() {
        // Handle collision with top and bottom walls
        handleWallCollision();

        // Create Rectangle objects for collision detection between ball and paddles
        // This simplifies collision detection using the 'intersects()' method
        Rectangle ballRect = new Rectangle(model.ballX, model.ballY, model.ballSize, model.ballSize);
        Rectangle paddle1Rect = new Rectangle(model.paddle1X, model.paddle1Y, model.paddleWidth, model.paddleHeight);
        Rectangle paddle2Rect = new Rectangle(model.paddle2X, model.paddle2Y, model.paddleWidth, model.paddleHeight);

        // Handle collision with paddles
        handlePaddleCollision(ballRect, paddle1Rect, model.paddle1Speed, true);
        handlePaddleCollision(ballRect, paddle2Rect, model.paddle2Speed, false);

        // Check if the ball has gone past a paddle (scoring)
        if (model.ballX < 0) {
            // Ball has gone past the left edge; Player 2 scores
            return 2;
        } else if (model.ballX > model.width) {
            // Ball has gone past the right edge; Player 1 scores
            return 1;
        }

        return 0; // No score
    }

    /**
     * Handles collision with top and bottom walls and updates ball's vertical speed accordingly.
     * Reverses the ball's vertical direction when it hits the top or bottom of the game area.
     */
    private void handleWallCollision() {
        // Check collision with the top wall
        if (model.ballY <= 0 && model.ballYSpeed < 0) {
            // Adjust ball position to be at the top edge
            model.ballY = 0;
            // Reverse vertical speed to bounce downwards
            model.ballYSpeed = reverse(model.ballYSpeed);
        }
        // Check collision with the bottom wall
        else if (model.ballY + model.ballSize >= model.height && model.ballYSpeed > 0) {
            // Adjust ball position to be at the bottom edge
            model.ballY = model.height - model.ballSize;
            // Reverse vertical speed to bounce upwards
            model.ballYSpeed = reverse(model.ballYSpeed);
        }
    }

    /**
     * Handles collision detection and response between the ball and a paddle.
     *
     * @param ballRect     The Rectangle representing the ball's current position and size
     * @param paddleRect   The Rectangle representing the paddle's current position and size
     * @param paddleSpeed  The vertical speed of the paddle (used to influence ball's speed)
     * @param isLeftPaddle Indicates whether the paddle is the left paddle (Player 1) or not
     */
    private void handlePaddleCollision(Rectangle ballRect, Rectangle paddleRect, int paddleSpeed, boolean isLeftPaddle) {
        if (ballRect.intersects(paddleRect)) {
            playPaddleHitSound();

            // Adjust ball position to prevent it from getting stuck inside the paddle
            if (isLeftPaddle) {
                // For left paddle, position the ball to the right edge of the paddle
                model.ballX = paddleRect.x + model.paddleWidth;
            } else {
                // For right paddle, position the ball to the left edge of the paddle
                model.ballX = paddleRect.x - model.ballSize;
            }

            // Calculate new ball speeds based on collision point and paddle movement
            calculateBounce(paddleRect.y, paddleSpeed, isLeftPaddle);

            // Start ball deformation effect
            model.deformationFrames = maxDeformationFrames;
        }
    }

    private void loadsfx(String fileName) {
        try {
            InputStream audioSrc = getClass().getResourceAsStream(fileName);
            if (audioSrc == null) {
                throw new IOException("Audio file not found: " + fileName);
            }
            // Buffer the input stream for efficiency
            InputStream bufferedIn = new BufferedInputStream(audioSrc);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(bufferedIn);
            paddleHitClip = AudioSystem.getClip();
            paddleHitClip.open(audioIn);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void playPaddleHitSound() {
        if (paddleHitClip != null) {
            paddleHitClip.setFramePosition(0);
            paddleHitClip.start();
        }
    }

    /**
     * Calculates the new ball speeds after collision with a paddle.
     * Adjusts the ball's direction and speed to simulate realistic bouncing.
     *
     * @param paddleY      Y position of the paddle
     * @param paddleSpeed  Vertical speed of the paddle
     * @param isLeftPaddle True if it's the left paddle (Player 1), false if it's the right paddle (Player 2)
     */
    private void calculateBounce(int paddleY, int paddleSpeed, boolean isLeftPaddle) {
        // Calculate the point of impact relative to the paddle's center
        // This will determine the bounce angle
        double paddleMidPoint = paddleY + (model.paddleHeight / 2.0);
        double ballMidPoint = model.ballY + (model.ballSize / 2.0);
        double relativeIntersectY = paddleMidPoint - ballMidPoint;

        // Normalize the relative intersection to a value between -1 and 1
        double normalizedRelativeIntersectionY = relativeIntersectY / (model.paddleHeight / 2.0);

        // Maximum bounce angle (in radians) is 45 degrees (π/4)
        double maxBounceAngle = Math.PI / 4; // 45 degrees

        // Calculate the bounce angle based on where the ball hits the paddle
        double bounceAngle = normalizedRelativeIntersectionY * maxBounceAngle;

        // Calculate the speed (magnitude) of the ball
        double speed = Math.sqrt(model.ballXSpeed * model.ballXSpeed + model.ballYSpeed * model.ballYSpeed);

        // Update ball speeds based on the bounce angle
        model.ballXSpeed = speed * Math.cos(bounceAngle);
        model.ballYSpeed = speed * -Math.sin(bounceAngle);

        // Ensure the ball moves away from the paddle horizontally
        if (isLeftPaddle && model.ballXSpeed < 0) {
            // Ball should move right after hitting the left paddle
            model.ballXSpeed = reverse(model.ballXSpeed);
        }
        if (!isLeftPaddle && model.ballXSpeed > 0) {
            // Ball should move left after hitting the right paddle
            model.ballXSpeed = reverse(model.ballXSpeed);
        }

        // Add influence from the paddle's movement to the ball's vertical speed
        model.ballYSpeed = model.ballYSpeed + (paddleSpeed * paddleInfluence);
    }

    /**
     * Resets the ball to the center of the game area and randomizes its initial direction.
     * Called at the start of the game and after a player scores.
     */
    public void resetBall() {
        // Center the ball horizontally and vertically
        model.ballX = model.width / 2 - model.ballSize / 2;
        model.ballY = model.height / 2 - model.ballSize / 2;

        model.ballXSpeed = initialSpeed * (Math.random() > 0.5 ? 1 : -1);
        model.ballYSpeed = initialSpeed * (Math.random() > 0.5 ? 1 : -1);

        model.deformationFrames = 0;
    }

    /**
     * Reverses the speed of the ball (used for bouncing).
     *
     * @param speed Current speed
     * @return Reversed speed
     */
    private double reverse(double speed) {
        return -speed;
    }

    /**
     * Decreases the deformation frames counter.
     * Used to manage the duration of the ball deformation effect after a collision.
     */
    public void decreaseDeformationFrames() {
        if (model.deformationFrames > 0) {
            model.deformationFrames--;
        }
    }
}