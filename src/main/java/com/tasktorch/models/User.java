package com.tasktorch.models;

/**
 * Represents a user account with username and password.
 */
public class User {
    private String username;
    private String password; // In a real app, this should be hashed
    
    /**
     * Default constructor.
     */
    public User() {
        this.username = "";
        this.password = "";
    }
    
    /**
     * Constructor for User.
     * 
     * @param username Username
     * @param password Password (should be hashed in production)
     */
    public User(String username, String password) {
        this.username = username != null ? username : "";
        this.password = password != null ? password : "";
    }
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username != null ? username : "";
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password != null ? password : "";
    }
    
    /**
     * Check if password matches.
     * 
     * @param inputPassword Password to check
     * @return True if passwords match
     */
    public boolean checkPassword(String inputPassword) {
        if (inputPassword == null) {
            return false;
        }
        // Compare passwords exactly (no trimming to preserve spaces if needed)
        return this.password.equals(inputPassword);
    }
}

