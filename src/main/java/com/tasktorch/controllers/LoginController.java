package com.tasktorch.controllers;

import com.tasktorch.models.User;
import com.tasktorch.models.UserManager;
import com.tasktorch.utils.NavigationService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controller for the Login view.
 * Handles user authentication and account creation.
 */
public class LoginController {
    @FXML
    private TextField usernameField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private Button loginButton;
    
    @FXML
    private Button signupButton;
    
    @FXML
    private Label errorLabel;
    
    private UserManager userManager;
    
    /**
     * Initialize the login controller.
     */
    @FXML
    public void initialize() {
        userManager = new UserManager();
        
        // Set up button handlers
        loginButton.setOnAction(e -> handleLogin());
        signupButton.setOnAction(e -> handleSignUp());
        
        // Allow Enter key to trigger login
        passwordField.setOnAction(e -> handleLogin());
    }
    
    /**
     * Handle login button click.
     */
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        // Clear previous errors
        errorLabel.setVisible(false);
        
        // Validation
        if (username.isEmpty()) {
            showError("Please enter a username.");
            return;
        }
        
        if (password.isEmpty()) {
            showError("Please enter a password.");
            return;
        }
        
        // Check if any users exist
        if (!userManager.usernameExists(username)) {
            showError("Username not found. Please sign up first or check your username.");
            return;
        }
        
        // Authenticate user
        User user = userManager.authenticate(username, password);
        
        if (user != null) {
            // Login successful - navigate to dashboard
            NavigationService.setCurrentUser(user);
            NavigationService.navigateToDashboard();
        } else {
            showError("Invalid password. Please try again.");
        }
    }
    
    /**
     * Handle sign up button click.
     */
    private void handleSignUp() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        
        // Clear previous errors
        errorLabel.setVisible(false);
        
        // Validation
        if (username.isEmpty()) {
            showError("Please enter a username.");
            return;
        }
        
        if (password.isEmpty()) {
            showError("Please enter a password.");
            return;
        }
        
        if (password.length() < 3) {
            showError("Password must be at least 3 characters long.");
            return;
        }
        
        // Check if username already exists
        if (userManager.usernameExists(username)) {
            showError("Username already exists. Please choose a different one or login.");
            return;
        }
        
        // Create new user
        boolean success = userManager.addUser(username, password);
        
        if (success) {
            // Sign up successful - show message and login
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Account Created");
            alert.setHeaderText(null);
            alert.setContentText("Account created successfully! Logging you in...");
            alert.showAndWait();
            
            // Automatically log in the new user
            User newUser = userManager.authenticate(username, password);
            if (newUser != null) {
                NavigationService.setCurrentUser(newUser);
                NavigationService.navigateToDashboard();
            } else {
                showError("Account created but login failed. Please try logging in manually.");
            }
        } else {
            showError("Failed to create account. Please try again.");
        }
    }
    
    /**
     * Show error message.
     * 
     * @param message Error message to display
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}

