package com.pong.view;
import com.pong.database.UserManager;
import com.pong.model.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SignUpScreen allows users to create a new account by providing their details.
 */
public class SignUpScreen extends JPanel {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField emailField;
    private UserManager userManager;


    public SignUpScreen(JFrame frame) {
        this.frame = frame;
        this.userManager = new UserManager();

        setPreferredSize(new Dimension(800, 600));
        setLayout(new GridBagLayout());
        setBackground(new Color(30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);


        JLabel title = new JLabel("SIGN UP");
        title.setFont(new Font("Verdana", Font.BOLD, 40));
        title.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(title, gbc);


        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        usernameLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        add(usernameLabel, gbc);


        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Verdana", Font.PLAIN, 20));
        gbc.gridy = 2;
        add(usernameField, gbc);


        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        passwordLabel.setForeground(Color.WHITE);
        gbc.gridy = 3;
        add(passwordLabel, gbc);


        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Verdana", Font.PLAIN, 20));
        gbc.gridy = 4;
        add(passwordField, gbc);


        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        emailLabel.setForeground(Color.WHITE);
        gbc.gridy = 5;
        add(emailLabel, gbc);


        emailField = new JTextField(15);
        emailField.setFont(new Font("Verdana", Font.PLAIN, 20));
        gbc.gridy = 6;
        add(emailField, gbc);


        JButton signUpButton = new JButton("SIGN UP");
        signUpButton.setFont(new Font("Verdana", Font.PLAIN, 24));
        signUpButton.addActionListener(this::signUpAction);
        gbc.gridy = 7;
        add(signUpButton, gbc);


        JButton backButton = new JButton("Back to Login");
        backButton.setFont(new Font("Verdana", Font.PLAIN, 20));
        backButton.addActionListener(this::backToLoginScreen);
        gbc.gridy = 8;
        add(backButton, gbc);
    }

    /**
     * Handles the sign-up action when the user submits the form.
     */
    private void signUpAction(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String email = emailField.getText();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "All fields must be filled in.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }


        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(frame, "Please enter a valid email address.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user = new User.Builder()
                .setUsername(username)
                .setPassword(password)
                .setEmail(email)
                .build();

        try {
            userManager.createUser(user);
            JOptionPane.showMessageDialog(frame, "Account created successfully! You can now log in.");


            frame.getContentPane().removeAll();
            LoginScreen loginScreen = new LoginScreen(frame);
            frame.add(loginScreen);
            frame.revalidate();
            frame.repaint();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Navigates back to the login screen.
     */
    private void backToLoginScreen(ActionEvent e) {
        frame.getContentPane().removeAll();
        LoginScreen loginScreen = new LoginScreen(frame);
        frame.add(loginScreen);
        frame.revalidate();
        frame.repaint();
    }

    /**
     * Helper method to validate email format.
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}