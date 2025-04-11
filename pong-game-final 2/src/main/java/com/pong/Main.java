package com.pong;
import com.pong.view.WelcomeScreen;
import javax.swing.*;

/**
 * This class allows users to launch the application.
 */
public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame("Pong Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new WelcomeScreen(frame));
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}