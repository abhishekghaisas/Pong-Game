package com.pong.view;
import com.pong.controller.GameController;
import com.pong.model.Settings;
import com.pong.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SettingsView extends JPanel {

    private final User playerOne;
    private final User playerTwo;

    public SettingsView(User playerOne, User playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        setBackground(new Color(30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Settings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titleLabel, gbc);


        JLabel ballColorLabel = new JLabel("Ball Color:");
        ballColorLabel.setForeground(Color.WHITE);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(ballColorLabel, gbc);

        JComboBox<String> ballColorDropdown = new JComboBox<>(new String[]{"White", "Yellow", "Cyan", "Orange"});
        ballColorDropdown.setSelectedItem("White");
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(ballColorDropdown, gbc);


        JLabel paddle1ColorLabel = new JLabel("Paddle 1 Color:");
        paddle1ColorLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(paddle1ColorLabel, gbc);

        JComboBox<String> paddle1ColorDropdown = new JComboBox<>(new String[]{"White", "Red", "Blue", "Green", "Magenta"});
        paddle1ColorDropdown.setSelectedItem("White");
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(paddle1ColorDropdown, gbc);


        JLabel paddle2ColorLabel = new JLabel("Paddle 2 Color:");
        paddle2ColorLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(paddle2ColorLabel, gbc);

        JComboBox<String> paddle2ColorDropdown = new JComboBox<>(new String[]{"White", "Red", "Blue", "Green", "Magenta"});
        paddle2ColorDropdown.setSelectedItem("White");
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(paddle2ColorDropdown, gbc);


        JLabel scoreLimitLabel = new JLabel("Score Limit:");
        scoreLimitLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(scoreLimitLabel, gbc);

        JComboBox<String> scoreLimitDropdown = new JComboBox<>(new String[]{"5", "11", "21"});
        scoreLimitDropdown.setSelectedItem("11");
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(scoreLimitDropdown, gbc);


        JCheckBox enableSoundCheckbox = new JCheckBox("Enable Sound");
        enableSoundCheckbox.setSelected(true);
        enableSoundCheckbox.setForeground(Color.WHITE);
        enableSoundCheckbox.setBackground(new Color(30, 30, 30));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(enableSoundCheckbox, gbc);


        JButton startGameButton = new JButton("Start Game");
        startGameButton.addActionListener((ActionEvent e) -> startGame(
                createSettings(
                        ballColorDropdown.getSelectedItem().toString(),
                        paddle1ColorDropdown.getSelectedItem().toString(),
                        paddle2ColorDropdown.getSelectedItem().toString(),
                        Integer.parseInt(scoreLimitDropdown.getSelectedItem().toString()),
                        enableSoundCheckbox.isSelected()
                )
        ));
        gbc.gridy = 6;
        add(startGameButton, gbc);
    }

    private void startGame(Settings settings) {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            GameView gameView = new GameView(playerOne, playerTwo, settings);
            GameController controller = new GameController(gameView, frame, playerOne, playerTwo, settings);

            frame.getContentPane().removeAll();
            frame.add(gameView);
            frame.addKeyListener(controller);
            frame.revalidate();
            frame.repaint();
            frame.requestFocus();
        }
    }

    private Settings createSettings(String ballColor, String paddle1Color, String paddle2Color, int scoreLimit, boolean enableSound) {
        Settings settings = new Settings();
        settings.setBallColor(convertColor(ballColor));
        settings.setPaddle1Color(convertColor(paddle1Color));
        settings.setPaddle2Color(convertColor(paddle2Color));
        settings.setScoreLimit(scoreLimit);
        settings.setEnableSound(enableSound);
        return settings;
    }

    private Color convertColor(String color) {
        return switch (color) {
            case "Yellow" -> Color.YELLOW;
            case "Cyan" -> Color.CYAN;
            case "Orange" -> Color.ORANGE;
            case "Red" -> Color.RED;
            case "Blue" -> Color.BLUE;
            case "Green" -> Color.GREEN;
            case "Magenta" -> Color.MAGENTA;
            default -> Color.WHITE;
        };
    }
}