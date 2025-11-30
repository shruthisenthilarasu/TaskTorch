# Google Calendar Integration Setup

TaskTorch can sync your tasks with Google Calendar! Follow these steps to enable it:

## Prerequisites

1. A Google account
2. Access to Google Cloud Console

## Setup Steps

### 1. Create a Google Cloud Project

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select an existing one
3. Enable the **Google Calendar API**:
   - Go to "APIs & Services" > "Library"
   - Search for "Google Calendar API"
   - Click "Enable"

### 2. Create OAuth 2.0 Credentials

1. Go to "APIs & Services" > "Credentials"
2. Click "Create Credentials" > "OAuth client ID"
3. If prompted, configure the OAuth consent screen:
   - Choose "External" user type
   - Fill in required fields (App name, User support email, Developer contact)
   - Add scopes: `https://www.googleapis.com/auth/calendar`
   - Add test users (your email)
4. Create OAuth 2.0 Client ID:
   - Application type: **Desktop app**
   - Name: "TaskTorch"
   - Click "Create"
5. Download the credentials:
   - Click the download icon next to your OAuth client
   - Save the file as `credentials.json`
   - Place it in the `data/` directory of your TaskTorch project

### 3. Connect in TaskTorch

1. Open TaskTorch
2. Go to Settings
3. Click "Connect Google Calendar"
4. A browser window will open for authentication
5. Sign in with your Google account
6. Grant permissions to TaskTorch
7. You're connected!

## Features

- **Automatic Sync**: Tasks are automatically synced to Google Calendar when created or updated
- **Event Management**: Tasks appear as events in your Google Calendar
- **Two-Way Sync**: Changes in TaskTorch are reflected in Google Calendar

## Troubleshooting

- **"Credentials file not found"**: Make sure `credentials.json` is in the `data/` directory
- **"Connection failed"**: Verify that Google Calendar API is enabled in your Google Cloud project
- **"Permission denied"**: Check that you've granted the necessary permissions during OAuth

## Note

The Google Calendar integration requires internet connectivity and a valid Google account. Tasks are synced in real-time when created, updated, or deleted.

