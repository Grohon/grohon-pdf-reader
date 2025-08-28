# Add these steps to your workflow for signed release builds

    - name: Decode Keystore
      if: matrix.build-type == 'release'
      run: |
        echo ${{ secrets.KEYSTORE_BASE64 }} | base64 -d > app/keystore.jks

    - name: Build signed release APK
      if: matrix.build-type == 'release'
      run: ./gradlew assembleRelease
      env:
        KEYSTORE_PATH: ../keystore.jks
        KEYSTORE_PASSWORD: ${{ secrets.KEYSTORE_PASSWORD }}
        KEY_ALIAS: ${{ secrets.KEY_ALIAS }}
        KEY_PASSWORD: ${{ secrets.KEY_PASSWORD }}

# Add to your app/build.gradle:
android {
    signingConfigs {
        release {
            storeFile file(System.getenv("KEYSTORE_PATH") ?: "keystore.jks")
            storePassword System.getenv("KEYSTORE_PASSWORD")
            keyAlias System.getenv("KEY_ALIAS")
            keyPassword System.getenv("KEY_PASSWORD")
        }
    }
    buildTypes {
        release {
            signingConfig signingConfigs.release
            // ... other configurations
        }
    }
}