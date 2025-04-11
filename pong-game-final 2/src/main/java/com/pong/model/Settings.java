package com.pong.model;
import java.awt.Color;

public class Settings{
    private Color ballColor = Color.WHITE;
    private Color paddle1Color = Color.WHITE;
    private Color paddle2Color = Color.WHITE;
    private int scoreLimit = 11;
    private boolean enableSound = true;


    public Color getBallColor() {
        return ballColor;
    }

    public void setBallColor(Color ballColor) {
        this.ballColor = ballColor;
    }

    public Color getPaddle1Color() {
        return paddle1Color;
    }

    public void setPaddle1Color(Color paddle1Color) {
        this.paddle1Color = paddle1Color;
    }

    public Color getPaddle2Color() {
        return paddle2Color;
    }

    public void setPaddle2Color(Color paddle2Color) {
        this.paddle2Color = paddle2Color;
    }

    public int getScoreLimit() {
        return scoreLimit;
    }

    public void setScoreLimit(int scoreLimit) {
        this.scoreLimit = scoreLimit;
    }

    public boolean isEnableSound() {
        return enableSound;
    }

    public void setEnableSound(boolean enableSound) {
        this.enableSound = enableSound;
    }
}