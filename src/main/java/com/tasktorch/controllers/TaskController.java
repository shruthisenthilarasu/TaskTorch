package com.tasktorch.controllers;

import com.tasktorch.models.*;
import com.tasktorch.utils.GoogleCalendarService;
import com.tasktorch.utils.NavigationService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * Controller for creating and editing tasks.
 * Creates, edits, and deletes tasks while saving updates to the file.
 */
public class TaskController {
    @FXML
    private TextField titleField;
    
    @FXML
    private DatePicker dueDatePicker;
    
    @FXML
    private ComboBox<String> classNameComboBox;
    
    @FXML
    private TextArea notesArea;
    
    @FXML
    private ComboBox<Priority> priorityComboBox;
    
    @FXML
    private ComboBox<Status> statusComboBox;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button cancelButton;
    
    @FXML
    private Button deleteButton;
    
    private TaskManager taskManager;
    private CourseManager courseManager;
    private Task currentTask;
    private boolean isEditMode;

    /**
     * Initialize the task controller.
     */
    @FXML
    public void initialize() {
        taskManager = new TaskManager();
        courseManager = new CourseManager();
        
        // Initialize priority combo box
        priorityComboBox.getItems().addAll(Priority.values());
        priorityComboBox.setValue(Priority.MEDIUM);
        
        // Initialize status combo box
        statusComboBox.getItems().addAll(Status.values());
        statusComboBox.setValue(Status.PENDING);
        
        // Set default due date to today
        dueDatePicker.setValue(LocalDate.now());
        
        // Make class name combo box editable so users can type class names
        classNameComboBox.setEditable(true);
        
        // Load course names into combo box
        loadCourseNames();
        
        // Set up button handlers
        saveButton.setOnAction(e -> handleSave());
        cancelButton.setOnAction(e -> handleCancel());
        deleteButton.setOnAction(e -> handleDelete());
        
        // Hide delete button for new tasks
        deleteButton.setVisible(false);
    }

    /**
     * Set task for editing mode.
     * 
     * @param task Task to edit
     */
    public void setTaskForEditing(Task task) {
        this.currentTask = task;
        this.isEditMode = true;
        
        if (task != null) {
            titleField.setText(task.getTitle());
            dueDatePicker.setValue(task.getDueDate());
            classNameComboBox.setValue(task.getClassName());
            classNameComboBox.getEditor().setText(task.getClassName());
            notesArea.setText(task.getNotes());
            priorityComboBox.setValue(task.getPriority());
            statusComboBox.setValue(task.getStatus());
            deleteButton.setVisible(true);
        }
    }

    /**
     * Set up for creating a new task.
     */
    public void setNewTaskMode() {
        this.currentTask = null;
        this.isEditMode = false;
        
        titleField.clear();
        dueDatePicker.setValue(LocalDate.now());
        classNameComboBox.getSelectionModel().clearSelection();
        classNameComboBox.getEditor().clear();
        notesArea.clear();
        priorityComboBox.setValue(Priority.MEDIUM);
        statusComboBox.setValue(Status.PENDING);
        deleteButton.setVisible(false);
    }

    /**
     * Load course names into the combo box.
     */
    private void loadCourseNames() {
        List<Course> courses = courseManager.loadCourses();
        for (Course course : courses) {
            classNameComboBox.getItems().add(course.getName());
        }
    }

    /**
     * Handle save button click.
     */
    private void handleSave() {
        String title = titleField.getText().trim();
        LocalDate dueDate = dueDatePicker.getValue();
        // Get value from editable combo box (either selected or typed)
        String classNameValue = classNameComboBox.getEditor().getText().trim();
        if (classNameValue.isEmpty()) {
            classNameValue = classNameComboBox.getValue() != null ? classNameComboBox.getValue() : "";
        }
        final String className = classNameValue;
        
        // Validation
        if (title.isEmpty()) {
            showAlert("Title cannot be empty.");
            return;
        }
        
        if (dueDate == null) {
            showAlert("Please select a due date.");
            return;
        }
        
        if (className.isEmpty()) {
            showAlert("Please enter a class name.");
            return;
        }

        List<Task> tasks = taskManager.loadTasks();
        
        if (isEditMode && currentTask != null) {
            // Update existing task in the list
            tasks.stream()
                .filter(task -> task.getTaskId().equals(currentTask.getTaskId()))
                .findFirst()
                .ifPresent(updatedTask -> {
                    updatedTask.setTitle(title);
                    updatedTask.setDueDate(dueDate);
                    updatedTask.setClassName(className);
                    updatedTask.setNotes(notesArea.getText());
                    updatedTask.setPriority(priorityComboBox.getValue());
                    updatedTask.setStatus(statusComboBox.getValue());
                    
                    // Sync with Google Calendar if connected
                    if (GoogleCalendarService.isConnected()) {
                        String eventId = updatedTask.getGoogleCalendarEventId();
                        if (eventId != null && !eventId.isEmpty()) {
                            // Update existing event
                            GoogleCalendarService.updateEvent(eventId, updatedTask);
                        } else {
                            // Create new event
                            String newEventId = GoogleCalendarService.createEvent(updatedTask);
                            if (newEventId != null) {
                                updatedTask.setGoogleCalendarEventId(newEventId);
                            }
                        }
                    }
                });
        } else {
            // Create new task
            String taskId = UUID.randomUUID().toString();
            Task newTask = new Task(
                taskId,
                title,
                dueDate,
                className,
                notesArea.getText(),
                statusComboBox.getValue(),
                priorityComboBox.getValue()
            );
            
            // Sync with Google Calendar if connected
            if (GoogleCalendarService.isConnected()) {
                String eventId = GoogleCalendarService.createEvent(newTask);
                if (eventId != null) {
                    newTask.setGoogleCalendarEventId(eventId);
                }
            }
            
            tasks.add(newTask);
        }
        
        taskManager.saveTasks(tasks);
        closeWindow();
    }

    /**
     * Handle cancel button click.
     */
    private void handleCancel() {
        closeWindow();
    }

    /**
     * Handle delete button click.
     */
    private void handleDelete() {
        if (currentTask != null && isEditMode) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Task");
            alert.setHeaderText("Are you sure you want to delete this task?");
            alert.setContentText("This action cannot be undone.");
            
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Delete from Google Calendar if connected
                    if (GoogleCalendarService.isConnected() && 
                        currentTask.getGoogleCalendarEventId() != null && 
                        !currentTask.getGoogleCalendarEventId().isEmpty()) {
                        GoogleCalendarService.deleteEvent(currentTask.getGoogleCalendarEventId());
                    }
                    
                    List<Task> tasks = taskManager.loadTasks();
                    tasks.removeIf(t -> t.getTaskId().equals(currentTask.getTaskId()));
                    taskManager.saveTasks(tasks);
                    closeWindow();
                }
            });
        }
    }

    /**
     * Close the window.
     */
    private void closeWindow() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    /**
     * Show an alert dialog.
     *
     * @param message Alert message
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

