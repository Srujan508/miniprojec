# Medicine Reminder App

A modern Android application built with Jetpack Compose that helps users manage their medications and never miss a dose.

## Features

- User Authentication
  - Sign up and login functionality
  - Secure data storage
  - User-specific medicine lists

- Medicine Management
  - Add medicines with details (name, dosage, frequency, time)
  - Edit and delete medicines
  - Mark medicines as taken or missed
  - View medicine history

- Reminders
  - Push notifications for medicine time
  - Customizable reminder times
  - Background reminder service

- Chatbot Assistant
  - Interactive help system
  - Common questions and answers
  - User-friendly interface

## Technical Stack

- Kotlin
- Jetpack Compose for UI
- Material3 Design
- Room Database for local storage
- Firebase Authentication
- WorkManager for background tasks
- Hilt for dependency injection
- Coroutines for asynchronous operations
- Flow for reactive programming

## Setup

1. Clone the repository
2. Open the project in Android Studio
3. Add your Firebase configuration file (google-services.json)
4. Build and run the app

## Requirements

- Android 7.0 (API level 24) or higher
- Google Play Services
- Internet connection for authentication

## Permissions

The app requires the following permissions:
- Internet access
- Notification permissions
- Alarm permissions for reminders

## Contributing

Feel free to submit issues and enhancement requests! 