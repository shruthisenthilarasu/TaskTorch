package com.tasktorch;

import com.tasktorch.utils.NavigationService;
import com.tasktorch.utils.ThemeService;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main application class for TaskTorch.
 */
public class TaskTorchApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            // Load theme from settings
            ThemeService.loadTheme();
            
            NavigationService.setPrimaryStage(primaryStage);
            NavigationService.navigateToLogin();
            primaryStage.setTitle("TaskTorch - Login");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

