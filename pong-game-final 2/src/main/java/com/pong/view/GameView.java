package com.pong.view;
import com.pong.model.GameModel;
import com.pong.model.Settings;
import com.pong.model.User;
import javax.swing.*;
import java.awt.*;

/**
 * GameView is responsible for rendering the game elements: paddles, ball, and scores.
 * It uses a coordinate system where (0,0) is at the top-left corner.
 */
public class GameView extends JPanel {

    private Color backgroundColor = new Color(30, 30, 30);
    private User playerOne;
    private User playerTwo;

    private GameModel model;
    private Settings settings;

    public GameView(User playerOne, User playerTwo, Settings settings) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.settings = settings;

        setPreferredSize(new Dimension(800, 600));
        setBackground(backgroundColor);
    }

    /**
     * Updates the game model reference and triggers a repaint to reflect the new game state.
     *
     * @param model The GameModel containing the current game state
     */
    public void updateGameState(GameModel model) {
        this.model = model;
        repaint();


        if (model.isGameOver()) {
            showWinnerMessageAndStatistics();
        }
    }

    /**
     * *** Added: Displays a winner/loser message and then transitions to the ScoreStatistics panel. ***
     */
    private void showWinnerMessageAndStatistics() {
        int player1Score = model.player1Score;
        int player2Score = model.player2Score;


        String winner;
        String loser;
        int winnerScore, loserScore;

        if (player1Score > player2Score) {
            winner = playerOne.getUsername();
            loser = playerTwo.getUsername();
            winnerScore = player1Score;
            loserScore = player2Score;
        } else {
            winner = playerTwo.getUsername();
            loser = playerOne.getUsername();
            winnerScore = player2Score;
            loserScore = player1Score;
        }


        String message = String.format(
                "Congratulations, %s! You are the Winner with a score of %d.\n" +
                        "Better luck next time, %s. Your score was %d.",
                winner, winnerScore, loser, loserScore
        );

        JOptionPane.showMessageDialog(
                this,
                message,
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE
        );

        showScoreStatistics(player1Score, player2Score, playerOne, playerTwo);
    }


    /**
     * *** Added: Displays the ScoreStatistics panel. ***
     *
     * @param playerScore    The player's score
     * @param opponentScore  The opponent's score
     */
    private void showScoreStatistics(int playerScore, int opponentScore, User playerOne, User playerTwo) {
        removeAll();
        ScoreStatistics statsPanel = new ScoreStatistics(playerScore, opponentScore, playerOne, playerTwo);
        add(statsPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (model == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setComposite(AlphaComposite.SrcOver);


        g2d.setColor(Color.WHITE);

        g2d.setColor(settings.getPaddle1Color());
        g2d.fillRoundRect(
                model.paddle1X, model.paddle1Y,
                model.paddleWidth, model.paddleHeight,
                10, 10
        );

        g2d.setColor(settings.getPaddle2Color());
        g2d.fillRoundRect(
                model.paddle2X, model.paddle2Y,
                model.paddleWidth, model.paddleHeight,
                10, 10
        );
        g2d.setColor(settings.getBallColor());

        int ballWidth = model.ballSize;
        int ballHeight = model.ballSize;

        if (model.deformationFrames > 0) {

            double deformationFactor = 0.3;
            double speedMagnitude = Math.sqrt(model.ballXSpeed * model.ballXSpeed + model.ballYSpeed * model.ballYSpeed);
            double speedRatio = speedMagnitude / 10.0;

            if (Math.abs(model.ballXSpeed) > Math.abs(model.ballYSpeed)) {

                ballWidth = (int) (model.ballSize * (1 + speedRatio * deformationFactor));
                ballHeight = (int) (model.ballSize * (1 - speedRatio * deformationFactor));
            } else {

                ballWidth = (int) (model.ballSize * (1 - speedRatio * deformationFactor));
                ballHeight = (int) (model.ballSize * (1 + speedRatio * deformationFactor));
            }


            ballWidth = Math.max(ballWidth, model.ballSize);
            ballHeight = Math.max(ballHeight, model.ballSize);
        }


        int drawBallX = model.ballX - (ballWidth - model.ballSize) / 2;
        int drawBallY = model.ballY - (ballHeight - model.ballSize) / 2;


        g2d.fillOval(drawBallX, drawBallY, ballWidth, ballHeight);
        g2d.setColor(Color.WHITE);
        Font scoreFont = new Font("Monospaced", Font.BOLD, 48);
        g2d.setFont(scoreFont);


        String scoreText = model.player1Score + "       " + model.player2Score;
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(scoreText);
        int x = (model.width - textWidth) / 2;
        int y = fm.getAscent() + 30;
        g2d.drawString(scoreText, x, y);


        g2d.setColor(new Color(255, 255, 255, 100));

        float[] dashPattern = {10, 10};
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10, dashPattern, 0));
        g2d.drawLine(model.width / 2, 0, model.width / 2, model.height);
    }
}