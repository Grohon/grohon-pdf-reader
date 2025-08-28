# Grohon PDF Reader

A simple and elegant PDF reader application for Android.

## Features

- **PDF File Selection**: Browse and select PDF files from device storage
- **PDF Viewing**: High-quality PDF rendering with smooth scrolling
- **Zoom Controls**: Zoom in/out functionality for better readability
- **Navigation**: Easy page navigation with previous/next buttons
- **Recent Files**: Keep track of recently opened PDF files
- **File Sharing**: Share PDF files with other applications
- **Modern UI**: Clean Material Design interface

## Requirements

- Android 5.0 (API level 21) or higher
- Storage permission for accessing PDF files

## Build Instructions

1. Clone or download this project
2. Open the project in Android Studio
3. Sync project with Gradle files
4. Build and run on your device or emulator

## Permissions

The app requires the following permissions:
- `READ_EXTERNAL_STORAGE`: To access PDF files from device storage
- `WRITE_EXTERNAL_STORAGE` (API 28 and below): For file operations

## Architecture

The app follows Android development best practices:
- **Activities**: MainActivity for file selection, PDFViewerActivity for viewing
- **Adapters**: RecyclerView adapters for efficient list display
- **Models**: Data classes for PDF pages and recent files
- **Utils**: Helper classes for preferences and file management

## Key Components

### MainActivity
- Handles file selection using Storage Access Framework
- Manages recent files list
- Requests necessary permissions

### PDFViewerActivity
- Renders PDF pages using Android's PdfRenderer
- Implements zoom and navigation controls
- Provides sharing functionality

### PDF Rendering
- Uses Android's built-in PdfRenderer for high-quality rendering
- Implements lazy loading for better performance
- Supports zoom levels from 0.5x to 3.0x

## File Structure

```
app/src/main/
├── java/com/grohon/pdfreader/
│   ├── MainActivity.kt
│   ├── PDFViewerActivity.kt
│   ├── adapters/
│   │   ├── RecentFilesAdapter.kt
│   │   └── PDFPagesAdapter.kt
│   ├── models/
│   │   ├── RecentFile.kt
│   │   └── PDFPage.kt
│   └── utils/
│       └── PreferencesManager.kt
├── res/
│   ├── layout/
│   ├── drawable/
│   ├── values/
│   └── menu/
└── AndroidManifest.xml
```

## License

This project is created for educational purposes.