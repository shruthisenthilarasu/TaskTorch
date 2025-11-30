package com.tasktorch.models;

/**
 * Theme options for the application.
 */
public enum Theme {
    LIGHT,
    DARK;
    
    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}

