package com.tasktorch.controllers;

import com.tasktorch.models.Course;
import com.tasktorch.models.TaskManager;
import java.util.List;

/**
 * Manager for course-related operations.
 */
public class CourseManager {
    private TaskManager taskManager;
    
    public CourseManager() {
        this.taskManager = new TaskManager();
    }
    
    /**
     * Load all courses.
     * 
     * @return List of Course objects
     */
    public List<Course> loadCourses() {
        return taskManager.loadCourses();
    }
    
    /**
     * Save courses.
     * 
     * @param courses List of Course objects to save
     */
    public void saveCourses(List<Course> courses) {
        taskManager.saveCourses(courses);
    }
}

