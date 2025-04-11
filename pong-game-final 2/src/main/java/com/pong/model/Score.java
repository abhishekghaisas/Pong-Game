package com.pong.model;
import java.sql.Timestamp;

public class Score {
    private int scoreId;
    private int userId;
    private int score;
    private int scorePoints;
    private Timestamp datePlayed;
    private int opponentScore;


    public Score() {}

    public Score(int scoreId, int userId, int score, int scorePoints, Timestamp datePlayed, int opponentScore) {
        this.scoreId = scoreId;
        this.userId = userId;
        this.score = score;
        this.scorePoints = scorePoints;
        this.datePlayed = datePlayed;
        this.opponentScore = opponentScore;
    }


    public int getScoreId() {
        return scoreId;
    }

    public void setScoreId(int scoreId) {
        this.scoreId = scoreId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScorePoints() {
        return scorePoints;
    }

    public void setScorePoints(int scorePoints) {
        this.scorePoints = scorePoints;
    }

    public Timestamp getDatePlayed() {
        return datePlayed;
    }

    public void setDatePlayed(Timestamp datePlayed) {
        this.datePlayed = datePlayed;
    }

    public int getOpponentScore() {
        return opponentScore;
    }

    public void setOpponentScore(int opponentScore) {
        this.opponentScore = opponentScore;
    }

    @Override
    public String toString() {
        return "Score{" +
                "scoreId=" + scoreId +
                ", userId=" + userId +
                ", score=" + score +
                ", scorePoints=" + scorePoints +
                ", datePlayed=" + datePlayed +
                ", opponentScore=" + opponentScore +
                '}';
    }

    public static class Builder {
        private int scoreId;
        private int userId;
        private int score;
        private int scorePoints;
        private Timestamp datePlayed;
        private int opponentScore;

        public Builder setScoreId(int scoreId) {
            this.scoreId = scoreId;
            return this;
        }

        public Builder setUserId(int userId) {
            this.userId = userId;
            return this;
        }

        public Builder setScore(int score) {
            this.score = score;
            return this;
        }

        public Builder setScorePoints(int scorePoints) {
            this.scorePoints = scorePoints;
            return this;
        }

        public Builder setDatePlayed(Timestamp datePlayed) {
            this.datePlayed = datePlayed;
            return this;
        }

        public Builder setOpponentScore(int opponentScore) {
            this.opponentScore = opponentScore;
            return this;
        }

        public Score build() {
            return new Score(scoreId, userId, score, scorePoints, datePlayed, opponentScore);
        }
    }
}