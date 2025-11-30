package com.tasktorch.models;

/**
 * Represents user settings for the application.
 */
public class UserSettings {
    private Theme theme;
    private boolean dailyReminder;
    private int remindDaysBeforeDue;
    private boolean googleCalendarEnabled;
    private String googleCalendarTokenPath; // Path to stored OAuth token

    /**
     * Default constructor.
     */
    public UserSettings() {
        this.theme = Theme.LIGHT;
        this.dailyReminder = false;
        this.remindDaysBeforeDue = 1;
        this.googleCalendarEnabled = false;
        this.googleCalendarTokenPath = null;
    }

    /**
     * Constructor for UserSettings.
     * 
     * @param theme Theme preference
     * @param dailyReminder Whether daily reminders are enabled
     * @param remindDaysBeforeDue Number of days before due date to remind
     */
    public UserSettings(Theme theme, boolean dailyReminder, int remindDaysBeforeDue) {
        this.theme = theme != null ? theme : Theme.LIGHT;
        this.dailyReminder = dailyReminder;
        this.remindDaysBeforeDue = remindDaysBeforeDue;
    }

    // Getters and Setters
    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public boolean isDailyReminder() {
        return dailyReminder;
    }

    public void setDailyReminder(boolean dailyReminder) {
        this.dailyReminder = dailyReminder;
    }

    public int getRemindDaysBeforeDue() {
        return remindDaysBeforeDue;
    }

    public void setRemindDaysBeforeDue(int remindDaysBeforeDue) {
        this.remindDaysBeforeDue = remindDaysBeforeDue;
    }
    
    public boolean isGoogleCalendarEnabled() {
        return googleCalendarEnabled;
    }
    
    public void setGoogleCalendarEnabled(boolean googleCalendarEnabled) {
        this.googleCalendarEnabled = googleCalendarEnabled;
    }
    
    public String getGoogleCalendarTokenPath() {
        return googleCalendarTokenPath;
    }
    
    public void setGoogleCalendarTokenPath(String googleCalendarTokenPath) {
        this.googleCalendarTokenPath = googleCalendarTokenPath;
    }
}

