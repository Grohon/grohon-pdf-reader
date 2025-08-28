# Quick Setup Guide

## Clone the Repository

```bash
# Using SSH (recommended if you have SSH keys set up)
git clone git@github.com:Grohon/grohon-pdf-reader.git

# Or using HTTPS
git clone https://github.com/Grohon/grohon-pdf-reader.git

cd grohon-pdf-reader
```

## Build Options

### 1. GitHub Actions (Automatic - Recommended)
- Push to the repository
- GitHub will automatically build APK files
- Download from Actions tab: https://github.com/Grohon/grohon-pdf-reader/actions

### 2. Command Line Build
```bash
# Set up local.properties with your Android SDK path
echo "sdk.dir=C:\\android-sdk" > local.properties

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease
```

### 3. Docker Build
```bash
# Build using Docker
docker build -t grohon-pdf-reader .

# Extract APK from container
docker run --rm -v $(pwd)/output:/output grohon-pdf-reader cp app/build/outputs/apk/debug/app-debug.apk /output/
```

## Repository Links

- **Main Repository**: https://github.com/Grohon/grohon-pdf-reader
- **Issues**: https://github.com/Grohon/grohon-pdf-reader/issues
- **Releases**: https://github.com/Grohon/grohon-pdf-reader/releases
- **Actions (CI/CD)**: https://github.com/Grohon/grohon-pdf-reader/actions

## Project Status

![Build Status](https://github.com/Grohon/grohon-pdf-reader/actions/workflows/android.yml/badge.svg?branch=master)

The project includes:
- ✅ Complete Android PDF Reader implementation
- ✅ MIT License
- ✅ Privacy Policy
- ✅ CI/CD with GitHub Actions
- ✅ Multi-platform build support
- ✅ Comprehensive documentation