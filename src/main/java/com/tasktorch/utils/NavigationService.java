package com.tasktorch.utils;

import com.tasktorch.models.Task;
import com.tasktorch.models.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Service for managing navigation between different views in the application.
 */
public class NavigationService {
    private static Stage primaryStage;
    private static User currentUser;
    
    /**
     * Set the primary stage for the application.
     * 
     * @param stage Primary stage
     */
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }
    
    /**
     * Get the primary stage.
     * 
     * @return Primary stage
     */
    public static Stage getPrimaryStage() {
        return primaryStage;
    }
    
    /**
     * Set the current logged-in user.
     * 
     * @param user Current user
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
    }
    
    /**
     * Get the current logged-in user.
     * 
     * @return Current user
     */
    public static User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * Navigate to the Login view.
     */
    public static void navigateToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationService.class.getResource("/fxml/Login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 500, 400);
            ThemeService.applyTheme(scene);
            primaryStage.setScene(scene);
            primaryStage.setTitle("TaskTorch - Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Navigate to the Dashboard view.
     */
    public static void navigateToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationService.class.getResource("/fxml/Dashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 900, 700);
            ThemeService.applyTheme(scene);
            primaryStage.setScene(scene);
            primaryStage.setTitle("TaskTorch - Dashboard");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Navigate to the Calendar view.
     */
    public static void navigateToCalendar() {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationService.class.getResource("/fxml/CalendarView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 900, 700);
            ThemeService.applyTheme(scene);
            primaryStage.setScene(scene);
            primaryStage.setTitle("TaskTorch - Calendar");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Open the Add Task view in a new window.
     */
    public static void openAddTask() {
        openTaskWindow(null, false);
    }
    
    /**
     * Open the Edit Task view in a new window.
     * 
     * @param task Task to edit
     */
    public static void openEditTask(Task task) {
        openTaskWindow(task, true);
    }
    
    /**
     * Open task window (Add or Edit).
     * 
     * @param task Task to edit (null for new task)
     * @param isEditMode True if editing, false if adding
     */
    private static void openTaskWindow(Task task, boolean isEditMode) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationService.class.getResource("/fxml/AddTask.fxml"));
            Parent root = loader.load();
            
            com.tasktorch.controllers.TaskController controller = loader.getController();
            if (isEditMode && task != null) {
                controller.setTaskForEditing(task);
            } else {
                controller.setNewTaskMode();
            }
            
            Stage stage = new Stage();
            stage.setTitle(isEditMode ? "Edit Task" : "Add Task");
            Scene scene = new Scene(root, 600, 500);
            ThemeService.applyTheme(scene);
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            stage.showAndWait();
            
            // Refresh the current view after closing the task window
            refreshCurrentViewAfterModalClose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Refresh the current view after a modal window closes.
     */
    private static void refreshCurrentViewAfterModalClose() {
        if (primaryStage != null && primaryStage.getScene() != null) {
            String title = primaryStage.getTitle();
            if (title != null) {
                if (title.contains("Dashboard")) {
                    navigateToDashboard();
                } else if (title.contains("Calendar")) {
                    navigateToCalendar();
                }
            }
        }
    }
    
    /**
     * Open the Settings view in a new window.
     */
    public static void openSettings() {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationService.class.getResource("/fxml/Settings.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("Settings");
            Scene scene = new Scene(root, 500, 400);
            ThemeService.applyTheme(scene);
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Open the About view in a new window.
     */
    public static void openAbout() {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationService.class.getResource("/fxml/About.fxml"));
            Parent root = loader.load();
            
            Stage stage = new Stage();
            stage.setTitle("About TaskTorch");
            Scene scene = new Scene(root, 700, 600);
            ThemeService.applyTheme(scene);
            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

