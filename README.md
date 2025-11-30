# TaskTorch

Homework & Deadline Organizer - A JavaFX application for managing tasks and assignments.

## Quick Start

```bash
mvn clean compile javafx:run
```

## Project Structure

- `src/main/java/com/tasktorch/` - Main application code
  - `TaskTorchApp.java` - Application entry point
  - `controllers/` - UI controllers
  - `models/` - Data models
  - `utils/` - Utility services
- `src/main/resources/` - FXML layouts and CSS styles

## Requirements

- Java 21+
- Maven 3.6+

## Google Calendar Integration

TaskTorch supports syncing tasks with Google Calendar! See [GOOGLE_CALENDAR_SETUP.md](GOOGLE_CALENDAR_SETUP.md) for setup instructions.

**Note**: Google Calendar integration requires:
- Google Cloud project with Calendar API enabled
- OAuth 2.0 credentials file (`credentials.json`) in the `data/` directory
- Internet connectivity

The integration is implemented and ready to use once credentials are configured.
