package com.tasktorch.utils;

import com.tasktorch.models.Theme;
import com.tasktorch.models.TaskManager;
import com.tasktorch.models.UserSettings;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;

/**
 * Service for managing theme application across the application.
 */
public class ThemeService {
    private static final String LIGHT_STYLESHEET = "/styles.css";
    private static final String DARK_STYLESHEET = "/dark-mode.css";
    private static Theme currentTheme = Theme.LIGHT;
    
    /**
     * Apply theme to a scene.
     * 
     * @param scene Scene to apply theme to
     */
    public static void applyTheme(Scene scene) {
        if (scene == null) {
            return;
        }
        
        // Remove existing theme stylesheets
        List<String> stylesheets = scene.getStylesheets();
        stylesheets.removeIf(s -> s.contains("styles.css") || s.contains("dark-mode.css"));
        
        // Add the appropriate stylesheet
        try {
            if (currentTheme == Theme.DARK) {
                java.net.URL darkResource = ThemeService.class.getResource(DARK_STYLESHEET);
                if (darkResource != null) {
                    scene.getStylesheets().add(darkResource.toExternalForm());
                } else {
                    System.err.println("Warning: Dark mode stylesheet not found, using light mode");
                    java.net.URL lightResource = ThemeService.class.getResource(LIGHT_STYLESHEET);
                    if (lightResource != null) {
                        scene.getStylesheets().add(lightResource.toExternalForm());
                    }
                }
            } else {
                java.net.URL lightResource = ThemeService.class.getResource(LIGHT_STYLESHEET);
                if (lightResource != null) {
                    scene.getStylesheets().add(lightResource.toExternalForm());
                } else {
                    System.err.println("Warning: Light mode stylesheet not found");
                }
            }
        } catch (Exception e) {
            System.err.println("Error applying theme: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Apply theme to a stage (applies to its scene).
     * 
     * @param stage Stage to apply theme to
     */
    public static void applyTheme(Stage stage) {
        if (stage != null && stage.getScene() != null) {
            applyTheme(stage.getScene());
        }
    }
    
    /**
     * Load theme from user settings.
     */
    public static void loadTheme() {
        try {
            TaskManager taskManager = new TaskManager();
            UserSettings settings = taskManager.loadSettings();
            currentTheme = settings.getTheme();
        } catch (Exception e) {
            System.err.println("Error loading theme: " + e.getMessage());
            currentTheme = Theme.LIGHT; // Default to light theme
        }
    }
    
    /**
     * Set the current theme and apply it to the primary stage.
     * 
     * @param theme Theme to set
     */
    public static void setTheme(Theme theme) {
        currentTheme = theme;
        
        // Apply to primary stage if available
        Stage primaryStage = NavigationService.getPrimaryStage();
        if (primaryStage != null && primaryStage.getScene() != null) {
            applyTheme(primaryStage.getScene());
        }
    }
    
    /**
     * Get the current theme.
     * 
     * @return Current theme
     */
    public static Theme getCurrentTheme() {
        return currentTheme;
    }
}

