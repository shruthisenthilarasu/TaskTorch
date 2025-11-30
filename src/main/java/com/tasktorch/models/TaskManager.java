package com.tasktorch.models;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading data from CSV files.
 */
public class TaskManager {
    private static final String TASKS_FILE = "data/tasks.csv";
    private static final String CLASSES_FILE = "data/classes.csv";
    private static final String SETTINGS_FILE = "data/settings.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Load tasks from CSV file.
     * 
     * @return List of Task objects
     */
    public List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        
        try {
            // Create data directory if it doesn't exist
            Files.createDirectories(Paths.get("data"));
            
            File file = new File(TASKS_FILE);
            if (!file.exists()) {
                return tasks; // Return empty list if file doesn't exist
            }

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String header = br.readLine(); // Skip header
                if (header == null) {
                    return tasks;
                }

                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty()) {
                        continue;
                    }
                    String[] values = parseCSVLine(line);
                    if (values.length >= 6) {
                        String googleEventId = values.length > 7 && !values[7].isEmpty() ? values[7] : null;
                        Task task;
                        if (googleEventId != null) {
                            task = new Task(
                                values[0], // taskId
                                values[1], // title
                                LocalDate.parse(values[2], DATE_FORMATTER), // dueDate
                                values[3], // className
                                values.length > 4 ? values[4] : "", // notes
                                Status.fromString(values.length > 5 ? values[5] : "pending"), // status
                                Priority.fromString(values.length > 6 ? values[6] : "medium"), // priority
                                googleEventId // googleCalendarEventId
                            );
                        } else {
                            task = new Task(
                                values[0], // taskId
                                values[1], // title
                                LocalDate.parse(values[2], DATE_FORMATTER), // dueDate
                                values[3], // className
                                values.length > 4 ? values[4] : "", // notes
                                Status.fromString(values.length > 5 ? values[5] : "pending"), // status
                                Priority.fromString(values.length > 6 ? values[6] : "medium") // priority
                            );
                        }
                        tasks.add(task);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading tasks: " + e.getMessage());
        }
        
        return tasks;
    }

    /**
     * Save tasks to CSV file.
     * 
     * @param tasks List of Task objects to save
     */
    public void saveTasks(List<Task> tasks) {
        try {
            // Create data directory if it doesn't exist
            Files.createDirectories(Paths.get("data"));

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(TASKS_FILE))) {
                // Write header
                bw.write("taskId,title,dueDate,className,notes,status,priority,googleCalendarEventId\n");

                // Write tasks
                for (Task task : tasks) {
                    bw.write(escapeCSV(task.getTaskId()) + ",");
                    bw.write(escapeCSV(task.getTitle()) + ",");
                    bw.write(task.getDueDate().format(DATE_FORMATTER) + ",");
                    bw.write(escapeCSV(task.getClassName()) + ",");
                    bw.write(escapeCSV(task.getNotes()) + ",");
                    bw.write(task.getStatus().getValue() + ",");
                    bw.write(task.getPriority().getValue() + ",");
                    bw.write(task.getGoogleCalendarEventId() != null ? escapeCSV(task.getGoogleCalendarEventId()) : "");
                    bw.write("\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving tasks: " + e.getMessage());
        }
    }

    /**
     * Load courses from CSV file.
     * 
     * @return List of Course objects
     */
    public List<Course> loadCourses() {
        List<Course> courses = new ArrayList<>();
        
        try {
            Files.createDirectories(Paths.get("data"));
            
            File file = new File(CLASSES_FILE);
            if (!file.exists()) {
                return courses;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String header = br.readLine();
                if (header == null) {
                    return courses;
                }

                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = parseCSVLine(line);
                    if (values.length >= 2) {
                        Course course = new Course(
                            values[0], // courseId
                            values[1], // name
                            values.length > 2 ? values[2] : "", // instructor
                            values.length > 3 ? values[3] : "", // location
                            values.length > 4 ? values[4] : ""  // schedule
                        );
                        courses.add(course);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading courses: " + e.getMessage());
        }
        
        return courses;
    }

    /**
     * Save courses to CSV file.
     * 
     * @param courses List of Course objects to save
     */
    public void saveCourses(List<Course> courses) {
        try {
            Files.createDirectories(Paths.get("data"));

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(CLASSES_FILE))) {
                bw.write("courseId,name,instructor,location,schedule\n");

                for (Course course : courses) {
                    bw.write(escapeCSV(course.getCourseId()) + ",");
                    bw.write(escapeCSV(course.getName()) + ",");
                    bw.write(escapeCSV(course.getInstructor()) + ",");
                    bw.write(escapeCSV(course.getLocation()) + ",");
                    bw.write(escapeCSV(course.getSchedule()) + "\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving courses: " + e.getMessage());
        }
    }

    /**
     * Load user settings from file.
     * 
     * @return UserSettings object
     */
    public UserSettings loadSettings() {
        UserSettings settings = new UserSettings();
        
        try {
            Files.createDirectories(Paths.get("data"));
            
            File file = new File(SETTINGS_FILE);
            if (!file.exists()) {
                return settings; // Return default settings
            }

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        
                        switch (key) {
                            case "theme":
                                try {
                                    settings.setTheme(Theme.valueOf(value.toUpperCase()));
                                } catch (IllegalArgumentException e) {
                                    settings.setTheme(Theme.LIGHT);
                                }
                                break;
                            case "dailyReminder":
                                settings.setDailyReminder(Boolean.parseBoolean(value));
                                break;
                            case "remindDaysBeforeDue":
                                try {
                                    settings.setRemindDaysBeforeDue(Integer.parseInt(value));
                                } catch (NumberFormatException e) {
                                    settings.setRemindDaysBeforeDue(1);
                                }
                                break;
                            case "googleCalendarEnabled":
                                settings.setGoogleCalendarEnabled(Boolean.parseBoolean(value));
                                break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading settings: " + e.getMessage());
        }
        
        return settings;
    }

    /**
     * Save user settings to file.
     * 
     * @param settings UserSettings object to save
     */
    public void saveSettings(UserSettings settings) {
        try {
            Files.createDirectories(Paths.get("data"));

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(SETTINGS_FILE))) {
                bw.write("theme=" + settings.getTheme().name() + "\n");
                bw.write("dailyReminder=" + settings.isDailyReminder() + "\n");
                bw.write("remindDaysBeforeDue=" + settings.getRemindDaysBeforeDue() + "\n");
                bw.write("googleCalendarEnabled=" + settings.isGoogleCalendarEnabled() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving settings: " + e.getMessage());
        }
    }

    // Helper methods for CSV parsing
    private String[] parseCSVLine(String line) {
        List<String> values = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        values.add(current.toString());
        
        return values.toArray(new String[0]);
    }

    private String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}

