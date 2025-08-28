# Grohon PDF Reader

![Build Status](https://github.com/Grohon/grohon-pdf-reader/actions/workflows/android.yml/badge.svg?branch=master)
![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)
![Android](https://img.shields.io/badge/Platform-Android-green.svg)
![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)

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

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Repository

- **GitHub**: https://github.com/Grohon/grohon-pdf-reader
- **Issues**: https://github.com/Grohon/grohon-pdf-reader/issues
- **Releases**: https://github.com/Grohon/grohon-pdf-reader/releases

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Building the App

The app can be built using:
- **Android Studio** (recommended)
- **Command line** with Android SDK
- **GitHub Actions** (automatic builds)
- **Docker** (containerized builds)

See the detailed build instructions above for each method.

## Support

If you encounter any issues or have questions:
- Check the [Issues](https://github.com/Grohon/grohon-pdf-reader/issues) page
- Create a new issue if your problem isn't already reported
- Review the [Privacy Policy](PRIVACY_POLICY.md) for data handling information