#!/bin/bash

# Android CI/CD Compilation Diagnostic Script
# This script helps identify and fix common Kotlin compilation issues

echo "🔍 Android Compilation Diagnostic Script"
echo "========================================"

# Function to check file existence
check_file() {
    if [ -f "$1" ]; then
        echo "✅ $1 exists"
    else
        echo "❌ $1 is missing"
        return 1
    fi
}

# Function to check directory existence
check_directory() {
    if [ -d "$1" ]; then
        echo "✅ $1 directory exists"
    else
        echo "❌ $1 directory is missing"
        return 1
    fi
}

echo ""
echo "📁 Checking project structure..."

# Check essential files
check_file "app/build.gradle"
check_file "build.gradle"
check_file "settings.gradle"
check_file "gradle.properties"
check_file "app/src/main/AndroidManifest.xml"

echo ""
echo "📱 Checking source files..."

# Check source directories
check_directory "app/src/main/java/com/grohon/pdfreader"
check_directory "app/src/main/res/layout"
check_directory "app/src/main/res/values"

# Check key source files
check_file "app/src/main/java/com/grohon/pdfreader/MainActivity.kt"
check_file "app/src/main/java/com/grohon/pdfreader/PDFViewerActivity.kt"

echo ""
echo "📋 Checking layout files..."
check_file "app/src/main/res/layout/activity_main.xml"
check_file "app/src/main/res/layout/activity_pdf_viewer.xml"
check_file "app/src/main/res/layout/item_recent_file.xml"

echo ""
echo "📝 Checking resource files..."
check_file "app/src/main/res/values/strings.xml"
check_file "app/src/main/res/values/colors.xml"
check_file "app/src/main/res/values/themes.xml"

echo ""
echo "🛠️ Checking build configuration..."

# Check if ViewBinding is enabled
if grep -q "viewBinding true" app/build.gradle; then
    echo "✅ ViewBinding is enabled"
else
    echo "❌ ViewBinding is not enabled"
fi

# Check Kotlin version
KOTLIN_VERSION=$(grep "kotlin_version" build.gradle | cut -d'"' -f2)
echo "📦 Kotlin version: $KOTLIN_VERSION"

# Check Android Gradle Plugin version
AGP_VERSION=$(grep "com.android.tools.build:gradle" build.gradle | cut -d':' -f3 | tr -d "'\"")
echo "📦 Android Gradle Plugin version: $AGP_VERSION"

echo ""
echo "🔨 Attempting diagnostic build..."

# Try a clean build with verbose output
echo "Running: ./gradlew clean"
./gradlew clean

echo ""
echo "Running diagnostic build with verbose output..."
./gradlew assembleDebug --info --stacktrace 2>&1 | tee diagnostic-build.log

# Check if build succeeded
if [ ${PIPESTATUS[0]} -eq 0 ]; then
    echo "✅ Diagnostic build succeeded!"
else
    echo "❌ Diagnostic build failed. Analyzing errors..."
    
    echo ""
    echo "🔍 Error Analysis:"
    
    # Look for common error patterns
    if grep -q "cannot find symbol" diagnostic-build.log; then
        echo "❌ Cannot find symbol error detected"
        echo "   This usually indicates missing imports or ViewBinding issues"
    fi
    
    if grep -q "Unresolved reference" diagnostic-build.log; then
        echo "❌ Unresolved reference error detected"
        echo "   Check for missing dependencies or incorrect imports"
    fi
    
    if grep -q "ViewBinding" diagnostic-build.log; then
        echo "❌ ViewBinding error detected"
        echo "   Check layout files and ViewBinding configuration"
    fi
    
    if grep -q "ActivityMainBinding" diagnostic-build.log; then
        echo "❌ ActivityMainBinding error detected"
        echo "   Check activity_main.xml layout file"
    fi
    
    echo ""
    echo "📊 Build artifacts check:"
    find app/build -name "*Binding*" 2>/dev/null | head -5 || echo "No binding files generated"
    
    echo ""
    echo "📄 Last 20 lines of build output:"
    tail -20 diagnostic-build.log
fi

echo ""
echo "🎯 Diagnostic completed. Check diagnostic-build.log for full details."