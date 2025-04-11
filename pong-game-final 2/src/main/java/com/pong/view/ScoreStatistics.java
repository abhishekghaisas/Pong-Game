package com.pong.view;
import com.pong.database.ScoreManager;
import com.pong.model.Score;
import com.pong.model.User;
import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public class ScoreStatistics extends JPanel {
    private final int player1Score;
    private final int player2Score;
    private final User playerOne;
    private final User playerTwo;
    private final ScoreManager scoreManager;

    public ScoreStatistics(int player1Score, int player2Score, User playerOne, User playerTwo) {
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        scoreManager = new ScoreManager();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(30, 30, 30));

        JLabel currentScoreLabel = new JLabel(
                "Final Score: " + playerOne.getUsername() + ": " + player1Score + " - " + playerTwo.getUsername() + ": " + player2Score,
                SwingConstants.CENTER
        );
        currentScoreLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        currentScoreLabel.setForeground(Color.WHITE);
        add(currentScoreLabel, BorderLayout.NORTH);

        JPanel historyPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        historyPanel.setBackground(new Color(30, 30, 30));

        JTextArea player1HistoryArea = createHistoryTextArea(String.format("%s History", playerOne.getUsername()), playerOne.getId());
        JTextArea player2HistoryArea = createHistoryTextArea(String.format("%s History", playerTwo.getUsername()), playerTwo.getId());

        historyPanel.add(new JScrollPane(player1HistoryArea));
        historyPanel.add(new JScrollPane(player2HistoryArea));

        add(historyPanel, BorderLayout.CENTER);

        scoreManager.addScore(createScore(player1Score, player2Score, playerOne.getId()));
        scoreManager.addScore(createScore(player2Score, player1Score, playerTwo.getId()));


        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(30, 30, 30));

        JButton backButton = new JButton("Back to Main Menu");
        backButton.setFont(new Font("Verdana", Font.PLAIN, 18));
        backButton.addActionListener(e -> navigateToWelcomeScreen());
        buttonPanel.add(backButton);

        JButton exitButton = new JButton("Exit Game");
        exitButton.setFont(new Font("Verdana", Font.PLAIN, 18));
        exitButton.addActionListener(e -> System.exit(0));
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }


    private Score createScore(int player1Score, int player2Score, int userId) {
        System.out.println("UserId is" + userId);
        return  new Score.Builder()
                .setScore(player1Score)
                .setOpponentScore(player2Score)
                .setDatePlayed(Timestamp.from(Instant.now()))
                .setUserId(userId)
                .build();
    }
    private JTextArea createHistoryTextArea(String title, int userId) {
        JTextArea historyArea = new JTextArea();
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        historyArea.setBackground(Color.BLACK);
        historyArea.setForeground(Color.WHITE);

        StringBuilder historyText = new StringBuilder(title + ":\n");
        List<Score> scores = scoreManager.getUserScores(userId);

        for (Score score : scores) {
            historyText.append(String.format(
                    "Date: %s | Score: %d | Opponent Score: %d\n",
                    score.getDatePlayed(), score.getScore(), score.getOpponentScore()
            ));
        }

        if (scores.isEmpty()) {
            historyText.append("No history available.\n");
        }

        historyArea.setText(historyText.toString());
        return historyArea;
    }

    private void navigateToWelcomeScreen() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            frame.getContentPane().removeAll();
            frame.add(new WelcomeScreen(frame));
            frame.revalidate();
            frame.repaint();
        }
    }

}