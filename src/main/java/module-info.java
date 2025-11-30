module com.tasktorch {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    
    // Google Calendar API - accessed via classpath (unnamed module)
    
    opens com.tasktorch to javafx.fxml;
    opens com.tasktorch.controllers to javafx.fxml;
    opens com.tasktorch.models to javafx.fxml, javafx.base;
    opens com.tasktorch.utils to javafx.fxml;
    
    exports com.tasktorch;
    exports com.tasktorch.controllers;
    exports com.tasktorch.models;
    exports com.tasktorch.utils;
}

