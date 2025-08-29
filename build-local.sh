#!/bin/bash

# Grohon PDF Reader - Local Build Script
# This script helps you build the Android project locally

echo "🚀 Grohon PDF Reader - Local Build Script"
echo "=========================================="

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed or not in PATH"
    echo "Please install JDK 17 and set JAVA_HOME"
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | grep -oP 'version "?\K[^"]+')
echo "☕ Java version: $JAVA_VERSION"

# Check if Android SDK is configured
if [ -z "$ANDROID_HOME" ] && [ ! -f "local.properties" ]; then
    echo "⚠️  Android SDK not configured"
    echo "Please set ANDROID_HOME or create local.properties file"
    echo "See local.properties.template for example"
fi

# Make gradlew executable
chmod +x gradlew

echo ""
echo "🔨 Available build commands:"
echo "1. Clean project: ./gradlew clean"
echo "2. Build debug APK: ./gradlew assembleDebug"
echo "3. Build release APK: ./gradlew assembleRelease"
echo "4. Run tests: ./gradlew test"
echo "5. Install debug to device: ./gradlew installDebug"
echo ""

# Ask user what to build
read -p "What would you like to build? (1-5): " choice

case $choice in
    1)
        echo "🧹 Cleaning project..."
        ./gradlew clean
        ;;
    2)
        echo "🔨 Building debug APK..."
        ./gradlew assembleDebug
        echo "✅ Debug APK built: app/build/outputs/apk/debug/app-debug.apk"
        ;;
    3)
        echo "🔨 Building release APK..."
        ./gradlew assembleRelease
        echo "✅ Release APK built: app/build/outputs/apk/release/app-release.apk"
        ;;
    4)
        echo "🧪 Running tests..."
        ./gradlew test
        ;;
    5)
        echo "📱 Installing debug APK to device..."
        ./gradlew installDebug
        ;;
    *)
        echo "❌ Invalid choice. Please run the script again."
        ;;
esac

echo "✨ Build script completed!"