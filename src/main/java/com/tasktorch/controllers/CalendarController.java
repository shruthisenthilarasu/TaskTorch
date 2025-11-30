package com.tasktorch.controllers;

import com.tasktorch.models.*;
import com.tasktorch.utils.NavigationService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Calendar view.
 * Displays tasks organized by week and day.
 */
public class CalendarController implements Initializable {
    @FXML
    private VBox calendarGrid;
    
    @FXML
    private Label weekLabel;
    
    @FXML
    private Button prevWeekButton;
    
    @FXML
    private Button nextWeekButton;
    
    @FXML
    private Button addTaskButton;
    
    @FXML
    private Button dashboardButton;
    
    @FXML
    private Button settingsButton;
    
    @FXML
    private Button aboutButton;
    
    private TaskManager taskManager;
    private LocalDate currentWeekStart;
    private static final DateTimeFormatter WEEK_FORMAT = DateTimeFormatter.ofPattern("MMM d - MMM d, yyyy");
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        taskManager = new TaskManager();
        currentWeekStart = LocalDate.now().with(DayOfWeek.MONDAY);
        
        // Set up button handlers
        addTaskButton.setOnAction(e -> {
            NavigationService.openAddTask();
            displayCalendar(); // Refresh after adding
        });
        dashboardButton.setOnAction(e -> NavigationService.navigateToDashboard());
        settingsButton.setOnAction(e -> NavigationService.openSettings());
        aboutButton.setOnAction(e -> NavigationService.openAbout());
        
        prevWeekButton.setOnAction(e -> {
            currentWeekStart = currentWeekStart.minusWeeks(1);
            displayCalendar();
        });
        
        nextWeekButton.setOnAction(e -> {
            currentWeekStart = currentWeekStart.plusWeeks(1);
            displayCalendar();
        });
        
        displayCalendar();
    }
    
    /**
     * Display the calendar for the current week.
     */
    private void displayCalendar() {
        calendarGrid.getChildren().clear();
        
        // Update week label
        LocalDate weekEnd = currentWeekStart.plusDays(6);
        weekLabel.setText(currentWeekStart.format(WEEK_FORMAT) + " - " + weekEnd.format(WEEK_FORMAT));
        
        // Load all tasks
        List<Task> allTasks = taskManager.loadTasks();
        
        // Create a row for each day of the week
        HBox weekRow = new HBox(10);
        weekRow.setStyle("-fx-spacing: 10;");
        
        for (int i = 0; i < 7; i++) {
            LocalDate day = currentWeekStart.plusDays(i);
            VBox dayBox = createDayBox(day, allTasks);
            weekRow.getChildren().add(dayBox);
        }
        
        calendarGrid.getChildren().add(weekRow);
    }
    
    /**
     * Create a VBox for a specific day with its tasks.
     */
    private VBox createDayBox(LocalDate day, List<Task> allTasks) {
        VBox dayBox = new VBox(5);
        dayBox.getStyleClass().add("calendar-day");
        dayBox.setStyle("-fx-min-width: 120; -fx-pref-width: 120;");
        
        // Day header
        Label dayLabel = new Label(day.format(DateTimeFormatter.ofPattern("EEE M/d")));
        dayLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        dayBox.getChildren().add(dayLabel);
        
        // Find tasks for this day
        List<Task> dayTasks = new ArrayList<>();
        for (Task task : allTasks) {
            if (task.getDueDate() != null && task.getDueDate().equals(day)) {
                dayTasks.add(task);
            }
        }
        
        // Add tasks to day box
        for (Task task : dayTasks) {
            HBox taskBox = createTaskBox(task);
            dayBox.getChildren().add(taskBox);
        }
        
        return dayBox;
    }
    
    /**
     * Create an HBox for a task with status dot.
     */
    private HBox createTaskBox(Task task) {
        HBox taskBox = new HBox(5);
        taskBox.setStyle("-fx-alignment: center-left; -fx-padding: 3px;");
        
        // Status dot
        Circle statusDot = new Circle(5);
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
        
        // Task label
        Label taskLabel = new Label(task.getTitle());
        taskLabel.setStyle("-fx-font-size: 11px;");
        
        // Apply strikethrough for completed tasks
        if (task.getStatus() == Status.COMPLETED) {
            taskLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #9E9E9E; -fx-strikethrough: true; -fx-opacity: 0.7;");
        }
        
        taskBox.getChildren().addAll(statusDot, taskLabel);
        
        // Handle clicks
        taskBox.setOnMouseClicked(e -> {
            if (e.getClickCount() == 1) {
                toggleTaskStatus(task);
            } else if (e.getClickCount() == 2) {
                NavigationService.openEditTask(task);
                displayCalendar(); // Refresh after editing
            }
        });
        
        return taskBox;
    }
    
    /**
     * Toggle task status.
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
        
        // Refresh calendar
        displayCalendar();
    }
}

