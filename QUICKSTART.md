# KCF Integration — Step-by-Step for New Project

Follow this guide to integrate KCF into a brand new Kotlin Multiplatform project in **15 minutes**.

---

## Prerequisites

- Android Studio / IntelliJ IDEA
- Kotlin 2.3.0+
- Gradle 8.0+
- Git (for submodule)

---

## Step 1: Get KCF (2 minutes)

### Option A: Git Submodule (Recommended for local dev)

```bash
cd your-kmp-project
mkdir -p frameworks
cd frameworks
git clone https://github.com/yourusername/kanha-core-framework.git kcf
cd ..
```

### Option B: Copy Files Directly

Download and copy the `core/` folder from KCF into your project as `frameworks/kcf/core/`.

---

## Step 2: Update Gradle Configuration (3 minutes)

### File: `settings.gradle.kts`

Add the KCF module and Compose repository:

```kotlin
pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        // ✅ ADD THIS for Compose artifacts
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev") {
            mavenContent {
                includeGroupAndSubgroups("org.jetbrains.compose")
            }
        }
        
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":composeApp")
include(":frameworks:kcf:core")  // ✅ ADD THIS
```

### File: `gradle/libs.versions.toml`

Ensure these versions exist. If not, add them:

```toml
[versions]
# ─── Compose ───────────────────────────────────────────────────────────────
composeMultiplatform = "1.10.0"
composeMaterialIcons = "1.7.3"     # ✅ Must be 1.7.3 (not 1.10.0)
material3 = "1.10.0-alpha05"

# ─── Kotlin ────────────────────────────────────────────────────────────────
kotlin = "2.3.0"
kotlinx-coroutines = "1.10.2"
kotlinSerialization = "2.3.0"

# ─── KCF Dependencies ──────────────────────────────────────────────────────
ksafe = "1.7.1"
materialKolor = "4.1.1"
voyager = "1.1.0-beta03"
colorpicker = "1.1.3"

[libraries]
# ─── Compose Multiplatform ────────────────────────────────────────────────
compose-runtime = { module = "org.jetbrains.compose.runtime:runtime", version.ref = "composeMultiplatform" }
compose-foundation = { module = "org.jetbrains.compose.foundation:foundation", version.ref = "composeMultiplatform" }
compose-ui = { module = "org.jetbrains.compose.ui:ui", version.ref = "composeMultiplatform" }
compose-material3 = { module = "org.jetbrains.compose.material3:material3", version.ref = "material3" }
compose-material-icons-extended = { module = "org.jetbrains.compose.material:material-icons-extended", version.ref = "composeMaterialIcons" }

# ─── KSafe ────────────────────────────────────────────────────────────────
ksafe = { module = "eu.anifantakis:ksafe", version.ref = "ksafe" }
ksafe-compose = { module = "eu.anifantakis:ksafe-compose", version.ref = "ksafe" }

# ─── KCF Libraries ────────────────────────────────────────────────────────
material-kolor = { module = "com.materialkolor:material-kolor", version.ref = "materialKolor" }
voyager-navigator = { module = "cafe.adriel.voyager:voyager-navigator", version.ref = "voyager" }
voyager-screenmodel = { module = "cafe.adriel.voyager:voyager-screenmodel", version.ref = "voyager" }
voyager-transitions = { module = "cafe.adriel.voyager:voyager-transitions", version.ref = "voyager" }
colorpicker-compose = { module = "com.github.skydoves:colorpicker-compose", version.ref = "colorpicker" }
```

### File: `composeApp/build.gradle.kts`

Add KCF core as a dependency:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            // ✅ ADD THIS LINE
            implementation(projects.frameworks.kcf.core)
            
            // ... rest of your dependencies
        }
    }
}
```

---

## Step 3: Initialize KSafe (2 minutes)

### Android: `MainActivity.kt`

Update your main activity:

```kotlin
package com.example.myapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.kanhaji.core.storage.initKSafe  // ✅ Import

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // ✅ Initialize KSafe BEFORE setContent
        initKSafe(context = this)
        
        setContent {
            App()
        }
    }
}
```

### iOS: `iOSApp.swift`

Update your iOS app delegate:

```swift
import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        // ✅ Initialize KSafe
        KSafePlatformInitKt.initKSafe()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
```

### Desktop: `Main.kt`

Update your main function:

```kotlin
package com.example.myapp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.kanhaji.core.storage.initKSafe  // ✅ Import

fun main() = application {
    // ✅ Initialize KSafe
    initKSafe()
    
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
```

---

## Step 4: Wrap App with KCF (2 minutes)

### Create/Update: `composeApp/src/commonMain/kotlin/App.kt`

```kotlin
package com.example.myapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.transitions.SlideTransition
import cafe.adriel.voyager.core.screen.Screen
import com.kanhaji.core.theme.KCF
import com.kanhaji.core.settings.KCFSettingsScreen
import androidx.compose.material3.Scaffold
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings

// Define screens
object HomeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        
        // Your home screen UI
        Text("Welcome to KCF!")
    }
}

object SettingsScreen : Screen {
    @Composable
    override fun Content() {
        // ✅ Use KCFSettingsScreen for complete theming UI
        KCFSettingsScreen(groups = emptyList())
    }
}

// Main app composable
@Composable
@Preview
fun App() {
    // ✅ Wrap entire app with KCF for theming
    KCF {
        Navigator(HomeScreen()) { navigator ->
            Scaffold(
                // Optional: Add bottom navigation
                bottomBar = {
                    NavigationBar {
                        NavigationBarItem(
                            selected = navigator.lastItem is HomeScreen,
                            onClick = { navigator.popUntilRoot() },
                            icon = { Icon(Icons.Outlined.Home, null) },
                            label = { Text("Home") }
                        )
                        NavigationBarItem(
                            selected = navigator.lastItem is SettingsScreen,
                            onClick = { navigator.push(SettingsScreen) },
                            icon = { Icon(Icons.Outlined.Settings, null) },
                            label = { Text("Settings") }
                        )
                    }
                }
            ) { padding ->
                // ✅ Add SlideTransition for screen animations
                SlideTransition(navigator)
            }
        }
    }
}
```

---

## Step 5: Verify & Build (3 minutes)

### Sync Gradle

```bash
./gradlew clean
./gradlew build
```

### Expected Output

```
BUILD SUCCESSFUL in Xs
X actionable tasks: X executed
```

### Run App

- **Android**: `./gradlew :composeApp:installDebug`
- **iOS**: Open `iosApp/iosApp.xcodeproj` in Xcode and run
- **Desktop**: `./gradlew :composeApp:run`

---

## Step 6: Test Theme System (2 minutes)

### Launch app and navigate to Settings

1. Tap the **Settings** icon in bottom navigation
2. You should see:
   - ✅ **App Theme** button (Light/Dark/System)
   - ✅ **Pitch Black** toggle (if dark mode active)
   - ✅ **Dynamic Color** toggle (Android 12+ only)
   - ✅ **App Color** picker

### Change Theme

1. Tap **App Theme**
2. Select **Dark**
3. Screen immediately switches to dark mode
4. Settings automatically saved!

### Change Custom Color

1. Tap **App Color**
2. Use color picker to select new color
3. Tap **Confirm**
4. App theme updates immediately

---

## Step 7: Add Your App Settings (Optional)

### Update `App.kt` to include custom settings

```kotlin
import com.kanhaji.core.settings.Group
import com.kanhaji.core.settings.SettingItems
import androidx.compose.material3.Switch

object SettingsScreen : Screen {
    @Composable
    override fun Content() {
        KCFSettingsScreen(
            groups = listOf(
                // Your app-specific settings
                Group(
                    header = "Notifications",
                    items = listOf(
                        SettingItems(
                            id = "notifications_enabled",
                            title = "Push Notifications",
                            description = "Receive app notifications",
                            icon = Icons.Outlined.Notifications,
                            widget = {
                                Switch(
                                    checked = notificationsEnabled,
                                    onCheckedChange = { enabled ->
                                        notificationsEnabled = enabled
                                    }
                                )
                            },
                            onClick = {
                                notificationsEnabled = !notificationsEnabled
                            }
                        )
                    )
                )
            )
        )
    }
}
```

---

## 🎉 Done!

Your app now has:

- ✅ **Full theming system** (Light/Dark/System)
- ✅ **AMOLED mode** (save battery on OLED screens)
- ✅ **Dynamic color** (Android 12+)
- ✅ **Custom color picker** with hex input
- ✅ **Persistent settings** (survives app restart)
- ✅ **Beautiful animations** (slide transitions)
- ✅ **KMP support** (Android, iOS, Desktop)

---

## Troubleshooting This Guide

| Issue | Solution |
|-------|----------|
| "Cannot resolve projects.frameworks.kcf" | Run `./gradlew sync` or restart IDE |
| "KSafeProvider not initialized" | Make sure `initKSafe()` is called in `onCreate()` or `main()` |
| "Color picker crashes" | Verify `composeMaterialIcons = "1.7.3"` in `libs.versions.toml` |
| Settings not persisting | Check app storage permissions on Android |
| Theme changes but UI doesn't update | Wrap component with `remember { ThemeManager.isDarkTheme }` |

---

## Next Steps

- 📖 Read full **GUIDE.md** for advanced features
- 📋 Review **CHEATSHEET.md** for API reference
- 🎨 Customize colors with `ThemeManager.customColor`
- 📱 Add more settings to your `Group` list

---

**Total Setup Time: ~15 minutes ⏱️**

**Questions?** Check the GUIDE.md or file an issue on GitHub!
