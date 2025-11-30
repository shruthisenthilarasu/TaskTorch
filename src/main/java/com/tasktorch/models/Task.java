package com.tasktorch.models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Represents a homework or assignment task.
 */
public class Task {
    private String taskId;
    private String title;
    private LocalDate dueDate;
    private String className;
    private String notes;
    private Status status;
    private Priority priority;
    private String googleCalendarEventId; // ID of corresponding Google Calendar event

    /**
     * Constructor for Task.
     * 
     * @param taskId Unique identifier for the task
     * @param title Title of the task
     * @param dueDate Due date of the task
     * @param className Name of the class/course
     * @param notes Additional notes about the task
     * @param status Current status of the task
     * @param priority Priority level of the task
     */
    public Task(String taskId, String title, LocalDate dueDate, String className, 
                String notes, Status status, Priority priority) {
        this.taskId = taskId;
        this.title = title;
        this.dueDate = dueDate;
        this.className = className;
        this.notes = notes != null ? notes : "";
        this.status = status != null ? status : Status.PENDING;
        this.priority = priority != null ? priority : Priority.MEDIUM;
        this.googleCalendarEventId = null;
    }
    
    public Task(String taskId, String title, LocalDate dueDate, String className, 
                String notes, Status status, Priority priority, String googleCalendarEventId) {
        this.taskId = taskId;
        this.title = title;
        this.dueDate = dueDate;
        this.className = className;
        this.notes = notes != null ? notes : "";
        this.status = status != null ? status : Status.PENDING;
        this.priority = priority != null ? priority : Priority.MEDIUM;
        this.googleCalendarEventId = googleCalendarEventId;
    }

    /**
     * Mark the task as completed.
     */
    public void markCompleted() {
        this.status = Status.COMPLETED;
    }

    /**
     * Calculate the number of days until the due date.
     * 
     * @return Number of days until due date (negative if overdue)
     */
    public int daysUntilDue() {
        LocalDate today = LocalDate.now();
        return (int) ChronoUnit.DAYS.between(today, dueDate);
    }

    /**
     * Get a summary string of the task.
     * 
     * @return A formatted summary string
     */
    public String getSummary() {
        int days = daysUntilDue();
        String daysText;
        
        if (days < 0) {
            daysText = Math.abs(days) + " days overdue";
        } else if (days == 0) {
            daysText = "Due today";
        } else {
            daysText = days + " days remaining";
        }
        
        return title + " (" + className + ") - " + daysText + " - " + priority.getValue().toUpperCase();
    }

    // Getters and Setters
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    
    public String getGoogleCalendarEventId() {
        return googleCalendarEventId;
    }
    
    public void setGoogleCalendarEventId(String googleCalendarEventId) {
        this.googleCalendarEventId = googleCalendarEventId;
    }
}

