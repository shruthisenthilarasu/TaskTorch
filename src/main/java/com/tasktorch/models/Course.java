package com.tasktorch.models;

/**
 * Represents a course or class.
 */
public class Course {
    private String courseId;
    private String name;
    private String instructor;
    private String location;
    private String schedule;

    /**
     * Default constructor.
     */
    public Course() {
        this.courseId = "";
        this.name = "";
        this.instructor = "";
        this.location = "";
        this.schedule = "";
    }

    /**
     * Constructor for Course.
     * 
     * @param courseId Unique identifier for the course
     * @param name Name of the course
     * @param instructor Instructor name
     * @param location Location/room
     * @param schedule Schedule information
     */
    public Course(String courseId, String name, String instructor, String location, String schedule) {
        this.courseId = courseId;
        this.name = name;
        this.instructor = instructor;
        this.location = location;
        this.schedule = schedule;
    }

    // Getters and Setters
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}

