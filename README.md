# Alarm Reminder Notes App

This is an Android application that demonstrates how to schedule note reminders using **AlarmManager** and display notifications at the exact scheduled time.

The app allows users to create notes with reminders, and even when the app is closed, the alarm triggers and shows a notification to the user.

## Features
- Create notes with reminder time
- Schedule alarms using AlarmManager
- Show notifications on alarm trigger
- Handles Android 13+ notification permission
- User-friendly permission rationale and settings redirection

## Tech Stack
- Kotlin
- AlarmManager
- BroadcastReceiver
- Notifications
- Android Jetpack components

## Permission Handling
The app requests notification permission on Android 13 and above.  
If permission is denied, a rationale dialog is shown.  
If permanently denied, users are redirected to system settings.

## Use Case
This project is intended for learning and demonstration purposes, showing how AlarmManager works with notifications in modern Android versions.
