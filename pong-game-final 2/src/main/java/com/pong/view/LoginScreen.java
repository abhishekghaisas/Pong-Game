package com.pong.view;
import com.pong.database.UserManager;
import com.pong.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * This class handles user authentication.
 * */

public class LoginScreen extends JPanel {
    private JFrame frame;
    private JTextField usernameFieldPlayer1;
    private JPasswordField passwordFieldPlayer1;
    private JTextField usernameFieldPlayer2;
    private JPasswordField passwordFieldPlayer2;
    private UserManager userManager;
    private boolean isPlayer1LoggedIn = false;
    private boolean isPlayer2LoggedIn = false;
    private User playerOne;
    private User playerTwo;

    public LoginScreen(JFrame frame) {
        this.frame = frame;
        this.userManager = new UserManager();


        setPreferredSize(new Dimension(800, 600));
        setBackground(new Color(30, 30, 30));


        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(10, 10, 10, 10);


        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 40));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBackground(new Color(30, 30, 30));
        centerPanel.setPreferredSize(new Dimension(650, 500));


        JPanel player1Panel = createLoginPanel("Player 1", true);
        JPanel player2Panel = createLoginPanel("Player 2", false);


        GridBagConstraints innerGbc = new GridBagConstraints();
        innerGbc.insets = new Insets(10, 20, 10, 20);
        innerGbc.anchor = GridBagConstraints.CENTER;

        // Add Player 1 panel
        innerGbc.gridx = 0;
        innerGbc.gridy = 0;
        centerPanel.add(player1Panel, innerGbc);

        // Add Player 2 panel
        innerGbc.gridx = 1;
        innerGbc.gridy = 0;
        centerPanel.add(player2Panel, innerGbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(centerPanel, gbc);


        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setFont(new Font("Verdana", Font.PLAIN, 24));
        signUpButton.addActionListener((ActionEvent e) -> navigateToSignUpScreen());

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        add(signUpButton, gbc);
    }

    /**
     * Creates a login panel for each player (Player 1 or Player 2)
     *
     * @param playerTitle Title for the player panel ("Player 1" or "Player 2")
     * @param isPlayer1  Boolean to distinguish between Player 1 and Player 2
     * @return JPanel with login components for the player
     */
    private JPanel createLoginPanel(String playerTitle, boolean isPlayer1) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(30, 30, 30));
        panel.setPreferredSize(new Dimension(320, 400));


        JLabel title = new JLabel(playerTitle);
        title.setFont(new Font("Verdana", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        panel.add(title);


        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        usernameLabel.setForeground(Color.WHITE);
        panel.add(usernameLabel);


        JTextField usernameField = new JTextField(20);
        usernameField.setFont(new Font("Verdana", Font.PLAIN, 20));
        usernameField.setPreferredSize(new Dimension(280, 40));
        usernameField.setMaximumSize(new Dimension(280, 40));
        usernameField.setBackground(Color.WHITE);
        usernameField.setForeground(Color.BLACK);
        if (isPlayer1) {
            usernameFieldPlayer1 = usernameField;
        } else {
            usernameFieldPlayer2 = usernameField;
        }
        panel.add(usernameField);


        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        passwordLabel.setForeground(Color.WHITE);
        panel.add(passwordLabel);


        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Verdana", Font.PLAIN, 20));
        passwordField.setPreferredSize(new Dimension(280, 40));
        passwordField.setMaximumSize(new Dimension(280, 40));
        passwordField.setBackground(Color.WHITE);
        passwordField.setForeground(Color.BLACK);
        if (isPlayer1) {
            passwordFieldPlayer1 = passwordField;
        } else {
            passwordFieldPlayer2 = passwordField;
        }
        panel.add(passwordField);


        panel.add(Box.createVerticalStrut(20));


        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Verdana", Font.PLAIN, 20));
        loginButton.addActionListener((ActionEvent e) -> loginAction(isPlayer1));
        panel.add(loginButton);

        return panel;
    }

    /**
     * Handles the login action for both players.
     * It checks the credentials for Player 1 and Player 2.
     *
     * @param isPlayer1 Boolean to distinguish between Player 1 and Player 2
     */
    private void loginAction(boolean isPlayer1) {
        String username;
        String password;

        if (isPlayer1) {
            username = usernameFieldPlayer1.getText();
            password = new String(passwordFieldPlayer1.getPassword());
        } else {
            username = usernameFieldPlayer2.getText();
            password = new String(passwordFieldPlayer2.getPassword());
        }

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please fill in both fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Prevent the same user from logging in as both players
        if (isPlayer1 && playerTwo != null && username.equals(playerTwo.getUsername())) {
            JOptionPane.showMessageDialog(frame, "This user is already logged in as Player 2.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isPlayer1 && playerOne != null && username.equals(playerOne.getUsername())) {
            JOptionPane.showMessageDialog(frame, "This user is already logged in as Player 1.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            User user = userManager.authenticateUser(username, password);
            JOptionPane.showMessageDialog(frame, username + " logged in successfully!");

            if (isPlayer1) {
                isPlayer1LoggedIn = true;
                playerOne = user;
            } else {
                isPlayer2LoggedIn = true;
                playerTwo = user;
            }

            // Check if both players are logged in and transition to GameView
            if (isPlayer1LoggedIn && isPlayer2LoggedIn) {
                frame.getContentPane().removeAll();
                SettingsView settingsView=new SettingsView(playerOne, playerTwo);
                frame.add(settingsView);
                frame.revalidate();
                frame.repaint();
            }

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * This method is used to navigate to the Sign Up Screen when the player clicks on "Sign Up"
     */
    private void navigateToSignUpScreen() {

        frame.getContentPane().removeAll();

        SignUpScreen signUpScreen = new SignUpScreen(frame);
        frame.add(signUpScreen);
        frame.revalidate();
        frame.repaint();
    }
}