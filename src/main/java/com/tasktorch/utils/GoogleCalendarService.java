package com.tasktorch.utils;

import com.tasktorch.models.Task;

import java.io.File;

/**
 * Service for integrating with Google Calendar API.
 * Handles OAuth2 authentication and syncing tasks to Google Calendar.
 * 
 * Note: This service requires Google API credentials to be set up.
 * See GOOGLE_CALENDAR_SETUP.md for instructions.
 * 
 * Currently stubbed out - full implementation requires Google API dependencies.
 */
public class GoogleCalendarService {
    private static final String TOKENS_DIRECTORY_PATH = "data/tokens";
    private static final String CREDENTIALS_FILE_PATH = "data/credentials.json";
    
    /**
     * Check if Google Calendar is connected.
     * 
     * @return True if connected, false otherwise
     */
    public static boolean isConnected() {
        // Check if credentials file exists and tokens directory exists
        File credFile = new File(CREDENTIALS_FILE_PATH);
        File tokenDir = new File(TOKENS_DIRECTORY_PATH);
        return credFile.exists() && tokenDir.exists() && tokenDir.listFiles() != null && tokenDir.listFiles().length > 0;
    }
    
    /**
     * Initialize and authenticate with Google Calendar.
     * 
     * @return True if authentication successful, false otherwise
     */
    public static boolean authenticate() {
        // Stub implementation - requires Google API setup
        System.out.println("Google Calendar integration requires credentials.json setup.");
        System.out.println("See GOOGLE_CALENDAR_SETUP.md for instructions.");
        return false;
    }
    
    /**
     * Create a calendar event from a Task.
     * 
     * @param task Task to create event for
     * @return Event ID if successful, null otherwise
     */
    public static String createEvent(Task task) {
        if (!isConnected()) {
            return null;
        }
        // Stub - requires full Google API implementation
        System.out.println("Google Calendar sync requires API setup.");
        return null;
    }
    
    /**
     * Update a calendar event.
     * 
     * @param eventId Event ID to update
     * @param task Updated task
     * @return True if successful, false otherwise
     */
    public static boolean updateEvent(String eventId, Task task) {
        if (!isConnected()) {
            return false;
        }
        // Stub - requires full Google API implementation
        return false;
    }
    
    /**
     * Delete a calendar event.
     * 
     * @param eventId Event ID to delete
     * @return True if successful, false otherwise
     */
    public static boolean deleteEvent(String eventId) {
        if (!isConnected()) {
            return false;
        }
        // Stub - requires full Google API implementation
        return false;
    }
    
    /**
     * Disconnect from Google Calendar.
     */
    public static void disconnect() {
        try {
            // Delete token directory
            File tokenDir = new File(TOKENS_DIRECTORY_PATH);
            if (tokenDir.exists()) {
                deleteDirectory(tokenDir);
            }
        } catch (Exception e) {
            System.err.println("Error disconnecting from Google Calendar: " + e.getMessage());
        }
    }
    
    /**
     * Helper method to delete directory recursively.
     */
    private static void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }
}

