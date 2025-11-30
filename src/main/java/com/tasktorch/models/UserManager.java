package com.tasktorch.models;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading users from CSV file.
 */
public class UserManager {
    private static final String USERS_FILE = "data/users.csv";
    
    /**
     * Load users from CSV file.
     * 
     * @return List of User objects
     */
    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        
        try {
            // Create data directory if it doesn't exist
            Files.createDirectories(Paths.get("data"));
            
            File file = new File(USERS_FILE);
            if (!file.exists()) {
                return users; // Return empty list if file doesn't exist
            }

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String header = br.readLine(); // Skip header
                if (header == null) {
                    return users;
                }

                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) {
                        continue; // Skip empty lines
                    }
                    String[] values = parseCSVLine(line);
                    if (values.length >= 2) {
                        String username = values[0].trim();
                        String password = values[1].trim();
                        if (!username.isEmpty() && !password.isEmpty()) {
                            User user = new User(username, password);
                            users.add(user);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
        
        return users;
    }
    
    /**
     * Save users to CSV file.
     * 
     * @param users List of User objects to save
     */
    public void saveUsers(List<User> users) {
        try {
            // Create data directory if it doesn't exist
            Files.createDirectories(Paths.get("data"));

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(USERS_FILE))) {
                // Write header
                bw.write("username,password\n");

                // Write users
                for (User user : users) {
                    bw.write(escapeCSV(user.getUsername()) + ",");
                    bw.write(escapeCSV(user.getPassword()) + "\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }
    
    /**
     * Add a new user.
     * 
     * @param username Username
     * @param password Password
     * @return True if user was added, false if username already exists
     */
    public boolean addUser(String username, String password) {
        List<User> users = loadUsers();
        
        // Check if username already exists
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return false; // Username already exists
            }
        }
        
        // Add new user
        users.add(new User(username, password));
        saveUsers(users);
        return true;
    }
    
    /**
     * Authenticate a user.
     * 
     * @param username Username
     * @param password Password
     * @return User object if authentication succeeds, null otherwise
     */
    public User authenticate(String username, String password) {
        List<User> users = loadUsers();
        
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username) && 
                user.checkPassword(password)) {
                return user;
            }
        }
        
        return null; // Authentication failed
    }
    
    /**
     * Check if a username exists.
     * 
     * @param username Username to check
     * @return True if username exists
     */
    public boolean usernameExists(String username) {
        List<User> users = loadUsers();
        
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        
        return false;
    }
    
    // Helper methods for CSV parsing
    private String[] parseCSVLine(String line) {
        List<String> values = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        values.add(current.toString());
        
        return values.toArray(new String[0]);
    }

    private String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}

