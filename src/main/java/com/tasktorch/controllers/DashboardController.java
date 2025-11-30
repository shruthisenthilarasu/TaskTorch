package com.tasktorch.controllers;

import com.tasktorch.models.*;
import com.tasktorch.utils.NavigationService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Dashboard view.
 * Displays upcoming tasks and allows navigation to other views.
 */
public class DashboardController implements Initializable {
    @FXML
    private ListView<Task> upcomingTasksList;
    
    @FXML
    private Button addTaskButton;
    
    @FXML
    private Button calendarButton;
    
    @FXML
    private Button settingsButton;
    
    @FXML
    private Button aboutButton;
    
    private TaskManager taskManager;
    private List<Task> upcomingTasks;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        taskManager = new TaskManager();
        
        // Set up button handlers
        addTaskButton.setOnAction(e -> NavigationService.openAddTask());
        calendarButton.setOnAction(e -> NavigationService.navigateToCalendar());
        settingsButton.setOnAction(e -> NavigationService.openSettings());
        aboutButton.setOnAction(e -> NavigationService.openAbout());
        
        // Load and display tasks
        loadUpcomingTasks();
        
        // Set up custom cell factory for task list with status dots
        upcomingTasksList.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
            @Override
            public ListCell<Task> call(ListView<Task> param) {
                return new TaskListCell();
            }
        });
        
        // Handle task clicks
        upcomingTasksList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 1) {
                Task selected = upcomingTasksList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    toggleTaskStatus(selected);
                }
            } else if (e.getClickCount() == 2) {
                Task selected = upcomingTasksList.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    NavigationService.openEditTask(selected);
                    loadUpcomingTasks(); // Refresh after editing
                }
            }
        });
    }
    
    /**
     * Load upcoming tasks (within 2 weeks).
     */
    private void loadUpcomingTasks() {
        List<Task> allTasks = taskManager.loadTasks();
        LocalDate today = LocalDate.now();
        LocalDate twoWeeksLater = today.plusWeeks(2);
        LocalDate twoWeeksAgo = today.minusWeeks(2);
        
        upcomingTasks = new ArrayList<>();
        for (Task task : allTasks) {
            LocalDate dueDate = task.getDueDate();
            if (dueDate != null && 
                (dueDate.isAfter(twoWeeksAgo) || dueDate.isEqual(twoWeeksAgo)) &&
                (dueDate.isBefore(twoWeeksLater) || dueDate.isEqual(twoWeeksLater))) {
                upcomingTasks.add(task);
            }
        }
        
        // Sort by due date
        upcomingTasks.sort((t1, t2) -> {
            if (t1.getDueDate() == null && t2.getDueDate() == null) return 0;
            if (t1.getDueDate() == null) return 1;
            if (t2.getDueDate() == null) return -1;
            return t1.getDueDate().compareTo(t2.getDueDate());
        });
        
        upcomingTasksList.getItems().setAll(upcomingTasks);
    }
    
    /**
     * Toggle task status on click.
     */
    private void toggleTaskStatus(Task task) {
        Status currentStatus = task.getStatus();
        Status newStatus;
        
        switch (currentStatus) {
            case PENDING:
                newStatus = Status.IN_PROGRESS;
                break;
            case IN_PROGRESS:
                newStatus = Status.COMPLETED;
                break;
            case COMPLETED:
                newStatus = Status.PENDING;
                break;
            default:
                newStatus = Status.PENDING;
        }
        
        task.setStatus(newStatus);
        
        // Save changes
        List<Task> allTasks = taskManager.loadTasks();
        for (Task t : allTasks) {
            if (t.getTaskId().equals(task.getTaskId())) {
                t.setStatus(newStatus);
                break;
            }
        }
        taskManager.saveTasks(allTasks);
        
        // Refresh display
        loadUpcomingTasks();
    }
    
    /**
     * Custom ListCell for displaying tasks with status dots.
     */
    private class TaskListCell extends ListCell<Task> {
        @Override
        protected void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);
            
            if (empty || task == null) {
                setText(null);
                setGraphic(null);
            } else {
                HBox hbox = new HBox(10);
                
                // Status dot
                Circle statusDot = new Circle(6);
                switch (task.getStatus()) {
                    case PENDING:
                        statusDot.setFill(Color.web("#F44336")); // Red
                        break;
                    case IN_PROGRESS:
                        statusDot.setFill(Color.web("#FFC107")); // Yellow
                        break;
                    case COMPLETED:
                        statusDot.setFill(Color.web("#4CAF50")); // Green
                        break;
                }
                
                // Task text
                String taskText = task.getTitle() + " - " + task.getClassName();
                if (task.getDueDate() != null) {
                    long days = ChronoUnit.DAYS.between(LocalDate.now(), task.getDueDate());
                    if (days < 0) {
                        taskText += " (" + Math.abs(days) + " days overdue)";
                    } else if (days == 0) {
                        taskText += " (Due today)";
                    } else {
                        taskText += " (" + days + " days)";
                    }
                }
                
                Label taskLabel = new Label(taskText);
                
                // Apply strikethrough for completed tasks
                if (task.getStatus() == Status.COMPLETED) {
                    taskLabel.setStyle("-fx-text-fill: #9E9E9E; -fx-strikethrough: true; -fx-opacity: 0.7;");
                }
                
                hbox.getChildren().addAll(statusDot, taskLabel);
                setGraphic(hbox);
            }
        }
    }
}

