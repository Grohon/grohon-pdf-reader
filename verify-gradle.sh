#!/bin/bash

# Gradle Installation Verification Script
echo "🔧 Gradle Installation Verification"
echo "===================================="

echo ""
echo "📋 Checking Gradle Installation..."

# Check if gradle command is available
if command -v gradle &> /dev/null; then
    echo "✅ Global Gradle installation found"
    gradle --version
    echo ""
else
    echo "❌ Global Gradle not found in PATH"
    echo "   (This is OK if using Gradle wrapper)"
    echo ""
fi

# Check project's Gradle wrapper
echo "📦 Checking Project Gradle Wrapper..."
if [ -f "gradlew" ] || [ -f "gradlew.bat" ]; then
    echo "✅ Gradle wrapper found"

    # Check wrapper properties
    if [ -f "gradle/wrapper/gradle-wrapper.properties" ]; then
        echo "✅ Wrapper properties exist"
        echo "   Gradle version: $(grep distributionUrl gradle/wrapper/gradle-wrapper.properties | cut -d'-' -f2 | cut -d'-' -f1)"
    fi

    # Try to run wrapper
    echo ""
    echo "🚀 Testing Gradle wrapper..."
    if [ -f "gradlew" ]; then
        chmod +x gradlew
        ./gradlew --version
    elif [ -f "gradlew.bat" ]; then
        ./gradlew.bat --version
    fi
else
    echo "❌ Gradle wrapper not found"
fi

echo ""
echo "☕ Checking Java Installation..."
if command -v java &> /dev/null; then
    echo "✅ Java found"
    java -version
else
    echo "❌ Java not found - Required for Gradle"
fi

echo ""
echo "📱 Android Project Compatibility Check..."
if [ -f "app/build.gradle" ]; then
    echo "✅ Android app module found"
    echo "   Checking Android Gradle Plugin version..."
    AGP_VERSION=$(grep "com.android.tools.build:gradle" build.gradle | cut -d':' -f3 | tr -d "'\"" | head -1)
    echo "   Android Gradle Plugin: $AGP_VERSION"

    echo "   Checking target SDK..."
    TARGET_SDK=$(grep "targetSdk" app/build.gradle | tr -d ' ' | cut -d'=' -f2 | head -1)
    echo "   Target SDK: $TARGET_SDK"
else
    echo "❌ Android project structure not detected"
fi

echo ""
echo "🎯 Recommendations:"
echo "   • For this project, use: ./gradlew [task] (wrapper)"
echo "   • Ensure JDK 17 is installed for optimal compatibility"
echo "   • Use './gradlew assembleDebug' to build the app"

echo ""
echo "✨ Verification completed!"