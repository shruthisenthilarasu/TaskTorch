package com.tasktorch.controllers;

import com.tasktorch.models.Theme;
import com.tasktorch.models.UserSettings;
import com.tasktorch.models.TaskManager;
import com.tasktorch.utils.ThemeService;
import com.tasktorch.utils.GoogleCalendarService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controller for the Settings view.
 * Loads/saves user settings, theme selection, and reminders.
 */
public class SettingsController {
    @FXML
    private ComboBox<Theme> themeComboBox;
    
    @FXML
    private CheckBox dailyReminderCheckBox;
    
    @FXML
    private Spinner<Integer> remindDaysSpinner;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Button connectGoogleCalendarButton;
    
    @FXML
    private Button disconnectGoogleCalendarButton;
    
    @FXML
    private Label googleCalendarStatusLabel;
    
    private TaskManager taskManager;
    private UserSettings currentSettings;

    /**
     * Initialize the settings controller.
     */
    @FXML
    public void initialize() {
        taskManager = new TaskManager();
        
        // Initialize theme combo box
        themeComboBox.getItems().addAll(Theme.values());
        
        // Initialize spinner for reminder days (1-30 days)
        SpinnerValueFactory.IntegerSpinnerValueFactory factory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 30, 1);
        remindDaysSpinner.setValueFactory(factory);
        
        // Load current settings
        loadSettings();
        
        // Set up button handlers
        saveButton.setOnAction(e -> handleSave());
        cancelButton.setOnAction(e -> handleCancel());
        connectGoogleCalendarButton.setOnAction(e -> handleConnectGoogleCalendar());
        disconnectGoogleCalendarButton.setOnAction(e -> handleDisconnectGoogleCalendar());
        
        // Update Google Calendar status
        updateGoogleCalendarStatus();
    }

    /**
     * Load user settings from file.
     */
    private void loadSettings() {
        currentSettings = taskManager.loadSettings();
        
        themeComboBox.setValue(currentSettings.getTheme());
        dailyReminderCheckBox.setSelected(currentSettings.isDailyReminder());
        remindDaysSpinner.getValueFactory().setValue(currentSettings.getRemindDaysBeforeDue());
    }

    /**
     * Handle save button click.
     */
    private void handleSave() {
        UserSettings settings = new UserSettings();
        Theme selectedTheme = themeComboBox.getValue();
        settings.setTheme(selectedTheme);
        settings.setDailyReminder(dailyReminderCheckBox.isSelected());
        settings.setRemindDaysBeforeDue(remindDaysSpinner.getValue());
        
        taskManager.saveSettings(settings);
        
        // Apply the new theme
        ThemeService.setTheme(selectedTheme);
        
        // Show success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Settings Saved");
        alert.setHeaderText(null);
        alert.setContentText("Your settings have been saved successfully.");
        alert.showAndWait();
        
        closeWindow();
    }

    /**
     * Handle cancel button click.
     */
    private void handleCancel() {
        closeWindow();
    }
    
    /**
     * Handle Google Calendar connection.
     */
    private void handleConnectGoogleCalendar() {
        try {
            boolean success = GoogleCalendarService.authenticate();
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Google Calendar Connected");
                alert.setHeaderText(null);
                alert.setContentText("Successfully connected to Google Calendar! Your tasks will now sync automatically.");
                alert.showAndWait();
                
                // Update settings
                currentSettings.setGoogleCalendarEnabled(true);
                taskManager.saveSettings(currentSettings);
                
                updateGoogleCalendarStatus();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Connection Failed");
                alert.setHeaderText(null);
                alert.setContentText("Failed to connect to Google Calendar. Please make sure:\n" +
                                   "1. You have placed credentials.json in the data/ directory\n" +
                                   "2. You have enabled the Google Calendar API in Google Cloud Console");
                alert.showAndWait();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Error connecting to Google Calendar: " + e.getMessage());
            alert.showAndWait();
        }
    }
    
    /**
     * Handle Google Calendar disconnection.
     */
    private void handleDisconnectGoogleCalendar() {
        GoogleCalendarService.disconnect();
        currentSettings.setGoogleCalendarEnabled(false);
        taskManager.saveSettings(currentSettings);
        updateGoogleCalendarStatus();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Disconnected");
        alert.setHeaderText(null);
        alert.setContentText("Disconnected from Google Calendar.");
        alert.showAndWait();
    }
    
    /**
     * Update Google Calendar status display.
     */
    private void updateGoogleCalendarStatus() {
        boolean isConnected = GoogleCalendarService.isConnected();
        if (isConnected) {
            googleCalendarStatusLabel.setText("Status: Connected");
            connectGoogleCalendarButton.setVisible(false);
            disconnectGoogleCalendarButton.setVisible(true);
        } else {
            googleCalendarStatusLabel.setText("Status: Not Connected");
            connectGoogleCalendarButton.setVisible(true);
            disconnectGoogleCalendarButton.setVisible(false);
        }
    }

    /**
     * Close the window.
     */
    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}

