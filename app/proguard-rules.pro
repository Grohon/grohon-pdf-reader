# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep data classes and their properties
-keep class com.grohon.pdfreader.models.** { *; }

# Keep adapter classes for RecyclerView
-keep class com.grohon.pdfreader.adapters.** { *; }

# Keep utility classes
-keep class com.grohon.pdfreader.utils.** { *; }

# Keep Activity classes
-keep class com.grohon.pdfreader.MainActivity { *; }
-keep class com.grohon.pdfreader.PDFViewerActivity { *; }

# Keep SharedPreferences related classes
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Keep JSON related classes for PreferencesManager
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.json.** *;
}

# AndroidX and Support Library
-keep class androidx.** { *; }
-dontwarn androidx.**

# Material Design Components
-keep class com.google.android.material.** { *; }
-dontwarn com.google.android.material.**

# PDF Renderer
-keep class android.graphics.pdf.** { *; }
-dontwarn android.graphics.pdf.**