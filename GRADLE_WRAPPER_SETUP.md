# Gradle Wrapper Download Instructions

The gradle-wrapper.jar file is missing. You can download it using one of these methods:

## Method 1: Download from Gradle (Recommended)
```bash
# Navigate to your project directory
cd c:/laragon/www/grohon-pdf-reader

# Create gradle wrapper directory
mkdir -p gradle/wrapper

# Download gradle wrapper jar
curl -L https://services.gradle.org/distributions/gradle-8.2-wrapper.jar -o gradle/wrapper/gradle-wrapper.jar
```

## Method 2: Use Gradle Init (if you have Gradle installed)
```bash
cd c:/laragon/www/grohon-pdf-reader
gradle wrapper
```

## Method 3: Generate wrapper using existing Gradle installation
```bash
cd c:/laragon/www/grohon-pdf-reader
gradle wrapper --gradle-version 8.2
```

After downloading, your gradle/wrapper directory should contain:
- gradle-wrapper.jar
- gradle-wrapper.properties (already exists)

Then you can push your code and GitHub Actions will work properly.