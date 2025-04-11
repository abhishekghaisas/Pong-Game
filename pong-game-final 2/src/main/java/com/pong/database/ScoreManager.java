package com.pong.database;
import com.pong.model.Score;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class ScoreManager {

    public void addScore(Score score) {
        String sql = "INSERT INTO scores (user_id, score, score_points, date_played, opponent_score) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);

            stmt.setInt(1, score.getUserId());
            stmt.setInt(2, score.getScore());
            stmt.setInt(3, score.getScorePoints());
            stmt.setTimestamp(4, score.getDatePlayed());
            stmt.setInt(5, score.getOpponentScore());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {

                conn.commit();
                System.out.println("Score added successfully.");
            } else {

                conn.rollback();
                System.out.println("Failed to add score.");
            }

        } catch (SQLException e) {
            System.err.println("Error adding score: " + e.getMessage());
            throw new RuntimeException("Database error occurred during score insertion", e);
        }
    }

    public List<Score> getUserScores(int userId) {
        List<Score> scores = new ArrayList<>();
        String sql = "SELECT score_id, user_id, score, score_points, date_played, opponent_score FROM scores WHERE user_id = ?";

        try (Connection conn = DatabaseUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Score score = new Score.Builder()
                            .setScoreId(rs.getInt("score_id"))
                            .setUserId(rs.getInt("user_id"))
                            .setScore(rs.getInt("score"))
                            .setScorePoints(rs.getInt("score_points"))
                            .setDatePlayed(rs.getTimestamp("date_played"))
                            .setOpponentScore(rs.getInt("opponent_score"))
                            .build();
                    scores.add(score);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving scores for user ID " + userId + ": " + e.getMessage());
            throw new RuntimeException("Database error occurred while fetching scores", e);
        }

        return scores;
    }
}