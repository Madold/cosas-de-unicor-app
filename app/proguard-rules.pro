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

-keep class com.markusw.cosasdeunicorapp.home.domain.model.* { *; }
-keep class com.markusw.cosasdeunicorapp.core.domain.model.* { *; }
-keep class com.markusw.cosasdeunicorapp.tabulator.domain.model.* { *; }
-keep class com.markusw.cosasdeunicorapp.tabulator.domain.* { *; }
-keep class com.markusw.cosasdeunicorapp.teacher_rating.domain.model.* { *; }
-keep class com.markusw.cosasdeunicorapp.teacher_rating.data.model.* { *; }