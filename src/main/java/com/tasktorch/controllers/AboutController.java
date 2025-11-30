package com.tasktorch.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controller for the About view.
 * Displays usage instructions and information about TaskTorch.
 */
public class AboutController {
    @FXML
    private Button closeButton;
    
    /**
     * Initialize the about controller.
     */
    @FXML
    public void initialize() {
        // Close button is handled in FXML with onAction="#handleClose"
    }
    
    /**
     * Handle close button click.
     */
    @FXML
    public void handleClose() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}

