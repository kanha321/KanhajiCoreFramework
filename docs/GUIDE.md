# Kanha Core Framework (KCF) — Integration Guide

A Kotlin Multiplatform (KMP) reusable foundation providing theming, settings UI, navigation, persistence, and more—with **zero boilerplate**.

---

## Table of Contents

1. [Quick Start (5 minutes)](#quick-start)
2. [Installation](#installation)
3. [Core Initialization](#core-initialization)
4. [Using the Theming System](#using-the-theming-system)
5. [Settings Screen Integration](#settings-screen-integration)
6. [Shell UI (TopBar, FAB, Snackbar)](#shell-ui-topbar-fab-snackbar)
7. [Customization Guide](#customization-guide)
8. [API Reference](#api-reference)
9. [Troubleshooting](#troubleshooting)

---

## Quick Start

### In 3 steps, you have a fully functional settings screen with theme switching:

**Step 1: Initialize KSafe (once at app startup)**

```kotlin
// MainActivity.kt (Android)
import com.kanhaji.core.storage.initKSafe

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    // Initialize KSafe BEFORE rendering any composables
    initKSafe(context = this)
    
    setContent {
        App()
    }
}
```

```kotlin
// Main.kt (iOS/Desktop)
import com.kanhaji.core.storage.initKSafe

fun main() {
    initKSafe()  // No context needed on non-Android
    
    runApplication {
        App()
    }
}
```

**Step 2: Wrap your app with KCF theme provider**

```kotlin
// App.kt (common)
import com.kanhaji.core.theme.KCF
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.kanhaji.core.settings.KCFSettingsScreen

@Composable
fun App() {
    KCF {
        Navigator(KCFSettingsScreen(groups = emptyList())) { navigator ->
            SlideTransition(navigator)
        }
    }
}
```

**Step 3: Done!** 🎉

You now have:
- ✅ Full theme selection (Light/Dark/System)
- ✅ Pitch Black AMOLED toggle
- ✅ Dynamic color support (Android 12+)
- ✅ Custom color picker with hex input
- ✅ Persistent state saved to device storage
- ✅ Slide transition animations

---

## Shell UI (TopBar, FAB, Snackbar)

KCF provides a parent shell scaffold and a single binding helper so child screens stay clean.

### 1) Root app host

```kotlin
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.kanhaji.core.shell.ui.ShellScaffold

@Composable
fun App() {
    KCF {
        Navigator(Screen1) { navigator ->
            ShellScaffold(
                canPop = navigator.canPop,
                onBack = { navigator.pop() }
            ) {
                SlideTransition(navigator)
            }
        }
    }
}
```

### 2) Child screen configuration (no local Scaffold needed)

```kotlin
import com.kanhaji.core.shell.ui.BindShellState
import com.kanhaji.core.shell.ui.FabState
import com.kanhaji.core.shell.ui.LocalShellController
import com.kanhaji.core.shell.ui.TopBarState

@Composable
override fun Content() {
    val shell = LocalShellController.current

    BindShellState(
        topBar = TopBarState(title = "Home", showBack = false),
        fab = FabState(
            isVisible = true,
            icon = Icons.Outlined.Add,
            onClick = { shell.showSnackbar("FAB clicked") }
        )
    )

    // Screen body only
}
```

This helper handles owner tracking and cleanup automatically, so transitions do not leak stale top bar/FAB state.

---

## Installation

### For a New Project

#### 1. Add KCF as a Git Submodule (Recommended for local dev)

```bash
cd your-kmp-project
git submodule add https://github.com/yourusername/kanha-core-framework.git frameworks/kcf
```

#### 2. Update `settings.gradle.kts`

```kotlin
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev") {
            mavenContent {
                includeGroupAndSubgroups("org.jetbrains.compose")
            }
        }
        google()
        mavenCentral()
    }
}

include(":composeApp")
include(":core")
include(":frameworks:kcf:core")  // ← Add this line
```

#### 3. Update `composeApp/build.gradle.kts`

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.frameworks.kcf.core)  // ← Add this
        }
    }
}
```

#### 4. Verify `gradle/libs.versions.toml` has these versions

```toml
[versions]
composeMultiplatform = "1.10.0"
composeMaterialIcons = "1.7.3"
kotlin = "2.3.0"
ksafe = "1.7.1"
materialKolor = "4.1.1"
voyager = "1.1.0-beta03"
colorpicker = "1.1.3"

[libraries]
compose-runtime = { module = "org.jetbrains.compose.runtime:runtime", version.ref = "composeMultiplatform" }
compose-foundation = { module = "org.jetbrains.compose.foundation:foundation", version.ref = "composeMultiplatform" }
compose-material3 = { module = "org.jetbrains.compose.material3:material3", version.ref = "composeMultiplatform" }
compose-ui = { module = "org.jetbrains.compose.ui:ui", version.ref = "composeMultiplatform" }
compose-material-icons-extended = { module = "org.jetbrains.compose.material:material-icons-extended", version.ref = "composeMaterialIcons" }

ksafe = { module = "eu.anifantakis:ksafe", version.ref = "ksafe" }
ksafe-compose = { module = "eu.anifantakis:ksafe-compose", version.ref = "ksafe" }
material-kolor = { module = "com.materialkolor:material-kolor", version.ref = "materialKolor" }
voyager-navigator = { module = "cafe.adriel.voyager:voyager-navigator", version.ref = "voyager" }
voyager-screenmodel = { module = "cafe.adriel.voyager:voyager-screenmodel", version.ref = "voyager" }
voyager-transitions = { module = "cafe.adriel.voyager:voyager-transitions", version.ref = "voyager" }
colorpicker-compose = { module = "com.github.skydoves:colorpicker-compose", version.ref = "colorpicker" }
```

---

## Core Initialization

### Understanding KSafe

KSafe is the persistence layer used by KCF. It securely stores theme settings across app restarts.

#### Android

```kotlin
// MainActivity.kt
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.kanhaji.core.storage.initKSafe

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Must be called BEFORE setContent
        initKSafe(context = this)
        
        setContent {
            App()
        }
    }
}
```

**Why?**
- KSafe needs the Android `Context` to access secure storage
- Initializing before composables ensures settings are loaded before rendering

#### iOS

```kotlin
// iOSApp.swift
import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    init() {
        // Initialize KSafe for iOS (no context needed)
        KSafePlatformInitKt.initKSafe()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
```

#### Desktop (JVM)

```kotlin
// Main.kt
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.kanhaji.core.storage.initKSafe

fun main() = application {
    // Initialize KSafe for Desktop
    initKSafe()
    
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
```

**What happens if you forget `initKSafe()`?**

The app will crash with:
```
KSafeProvider not initialized. Call initKSafe() at app entry point.
```

---

## Using the Theming System

### Wrapping Your App with KCF

All theme functionality comes through the `KCF` composable:

```kotlin
// App.kt
import androidx.compose.runtime.Composable
import com.kanhaji.core.theme.KCF
import androidx.compose.material3.MaterialTheme

@Composable
fun App() {
    KCF {
        // Your entire app goes here
        MyHomeScreen()
    }
}
```

**What `KCF` does:**
- ✅ Applies Material3 theme with Material Kolor color scheme
- ✅ Reads `ThemeManager` state (theme type, dark mode, colors)
- ✅ Auto-applies AMOLED black backgrounds if enabled
- ✅ Supports dynamic color on Android 12+
- ✅ Re-composes when theme changes (live preview in settings)

### Accessing Current Theme in Your Code

```kotlin
import com.kanhaji.core.theme.ThemeManager
import androidx.compose.runtime.remember

@Composable
fun MyComponent() {
    val isDarkTheme = remember { ThemeManager.isDarkTheme }
    val customColor = remember { ThemeManager.customColor }
    val isAmoled = remember { ThemeManager.isAmoled }
    
    Text(
        text = "Current theme: ${if (isDarkTheme) "Dark" else "Light"}",
        color = MaterialTheme.colorScheme.onSurface
    )
}
```

### Theme Types Available

```kotlin
// Programmatically change theme
import com.kanhaji.core.theme.ThemeManager

// Option 1: Light mode always
ThemeManager.setTheme(ThemeManager.ThemeType.LIGHT)

// Option 2: Dark mode always
ThemeManager.setTheme(ThemeManager.ThemeType.DARK)

// Option 3: Follow system settings
ThemeManager.setTheme(ThemeManager.ThemeType.SYSTEM)

// Toggle AMOLED (black backgrounds in dark mode)
ThemeManager.isAmoled = true

// Enable dynamic color (Android 12+ only)
ThemeManager.isDynamicColor = true

// Set custom seed color
ThemeManager.customColor.value = Color(0xFF6750A4)
```

**Note:** Changes to `ThemeManager` are automatically saved to device storage via KSafe!

---

## Settings Screen Integration

### Basic Usage: Show Built-in Theming Settings Only

```kotlin
// SettingsScreen.kt
import com.kanhaji.core.settings.KCFSettingsScreen
import cafe.adriel.voyager.core.screen.Screen

object SettingsScreen : Screen {
    @Composable
    override fun Content() {
        KCFSettingsScreen(groups = emptyList())
    }
}
```

This shows:
1. **App Theme** — Light/Dark/System selector
2. **Pitch Black** — Toggle for AMOLED mode (only when dark)
3. **Dynamic Color** — Toggle for system-derived colors (Android 12+ only)
4. **App Color** — Custom color picker (when dynamic color is off)

### Advanced: Mix Built-in Theming with App-specific Settings

```kotlin
// SettingsScreen.kt
import com.kanhaji.core.settings.KCFSettingsScreen
import com.kanhaji.core.settings.Group
import com.kanhaji.core.settings.SettingItems
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Switch
import androidx.compose.material3.Text

object SettingsScreen : Screen {
    @Composable
    override fun Content() {
        KCFSettingsScreen(
            groups = listOf(
                // App-specific settings appear after theme settings
                Group(
                    header = "Notifications",
                    items = listOf(
                        SettingItems(
                            id = "enable_notifications",
                            title = "Push Notifications",
                            description = "Receive app notifications",
                            icon = Icons.Outlined.Notifications,
                            widget = {
                                Switch(
                                    checked = notificationsEnabled,
                                    onCheckedChange = { enabled ->
                                        notificationsEnabled = enabled
                                        // Save to preferences
                                        saveNotificationPref(enabled)
                                    }
                                )
                            },
                            onClick = {
                                notificationsEnabled = !notificationsEnabled
                                saveNotificationPref(notificationsEnabled)
                            }
                        )
                    )
                )
            )
        )
    }
}
```

**Result:**
```
Settings
├── Theme (built-in)
│   ├── App Theme
│   ├── Pitch Black (if dark)
│   ├── Dynamic Color (if supported)
│   └── App Color (if dynamic off)
├── ─────────────────
└── Notifications (your app)
    └── Push Notifications
```

---

## Customization Guide

### Custom Color Picker Initial Value

```kotlin
import com.kanhaji.core.theme.ThemeManager
import androidx.compose.ui.graphics.Color

// Set initial app color before wrapping with KCF
@Composable
fun App() {
    // Load saved color from your own preferences
    val savedColor = remember { 
        loadCustomColor() ?: Color(0xFF6750A4)  // Default purple
    }
    
    LaunchedEffect(Unit) {
        ThemeManager.customColor.value = savedColor
    }
    
    KCF {
        // Rest of app...
    }
}
```

### Custom Theme Group Header

If you don't like the default "Theme" header, you can create your own group:

```kotlin
import com.kanhaji.core.settings.KCFThemeGroup
import com.kanhaji.core.settings.Group
import com.kanhaji.core.settings.KCFSettingsScreenModel

@Composable
fun CustomThemeGroup(model: KCFSettingsScreenModel): Group<SettingItems> {
    val builtIn = KCFThemeGroup(model)
    
    // Return with custom header
    return builtIn.copy(header = "Appearance")
}
```

### Hide Certain Theme Options

```kotlin
import com.kanhaji.core.settings.KCFThemeGroup
import com.kanhaji.core.settings.SettingItems
import com.kanhaji.core.theme.ThemeManager
import com.kanhaji.core.theme.isDynamicColorSupported

@Composable
fun CustomThemeGroup(model: KCFSettingsScreenModel): Group<SettingItems> {
    val allItems = KCFThemeGroup(model).items.toMutableList()
    
    // Remove Dynamic Color option
    allItems.removeIf { it.id == "dynamic_color" }
    
    return Group(header = "Theme", items = allItems)
}
```

### Sync Theme with Your Navigation

```kotlin
import cafe.adriel.voyager.navigator.Navigator

@Composable
fun App() {
    KCF {
        Navigator(HomeScreen()) { navigator ->
            // When theme changes, trigger recomposition
            val isDarkTheme = remember { ThemeManager.isDarkTheme }
            
            SlideTransition(navigator)
        }
    }
}
```

---

## API Reference

### ThemeManager (State Management)

```kotlin
object ThemeManager {
    // Current theme type
    enum class ThemeType { LIGHT, DARK, SYSTEM }
    var currentThemeType: ThemeType
    
    // Resolved dark mode (accounts for SYSTEM)
    var isDarkTheme: Boolean
    
    // AMOLED mode (black backgrounds)
    var isAmoled: Boolean
    
    // Use device-derived colors (Android 12+)
    var isDynamicColor: Boolean
    
    // Custom seed color for Material Kolor
    var customColor: MutableState<Color>
    
    // Set theme type and update isDarkTheme
    fun setTheme(type: ThemeType)
}
```

### KCF (Theme Provider)

```kotlin
@Composable
fun KCF(
    seedColor: Color = ThemeManager.customColor.value,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
)
```

### KCFSettingsScreen (Settings UI)

```kotlin
data class KCFSettingsScreen(
    val groups: List<Group<SettingItems>>
) : Screen {
    @Composable
    override fun Content()
}
```

### Group & SettingItems

```kotlin
data class Group<T>(
    val header: String,
    val items: List<T>
)

data class SettingItems(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val widget: @Composable () -> Unit = {},
    val onClick: () -> Unit = {}
)
```

### KCFSettingsScreenModel (State)

```kotlin
class KCFSettingsScreenModel : ScreenModel {
    var showColorPicker: Boolean
    var showThemeDialog: Boolean
    
    fun updateAmoled(enabled: Boolean)
    fun updateDynamicColor(enabled: Boolean)
    fun updateTheme(type: ThemeManager.ThemeType)
    fun updateCustomColor(color: Color)
}
```

### Persistence Functions

```kotlin
// Get value from KSafe with default fallback
fun getDirect(ksafe: KSafeWrapper, key: String, defaultValue: Int): Int
fun getDirect(ksafe: KSafeWrapper, key: String, defaultValue: Boolean): Boolean
fun getDirect(ksafe: KSafeWrapper, key: String, defaultValue: String): String

// Save value to KSafe
fun putDirect(ksafe: KSafeWrapper, key: String, value: Int)
fun putDirect(ksafe: KSafeWrapper, key: String, value: Boolean)
fun putDirect(ksafe: KSafeWrapper, key: String, value: String)
```

---

## Troubleshooting

### Issue: "KSafeProvider not initialized"

**Error:**
```
KSafeProvider not initialized. Call initKSafe() at app entry point.
```

**Solution:**
Ensure `initKSafe()` is called BEFORE any composables render:

```kotlin
// ❌ WRONG
@Composable
fun App() {
    initKSafe()  // Too late!
    KCF { ... }
}

// ✅ CORRECT
fun main() {
    initKSafe()  // Called before rendering
    runApplication { App() }
}
```

### Issue: Theme Not Persisting Across App Restarts

**Symptoms:** Settings change in-app but reset on restart

**Solution:** Verify KSafe storage paths:
- **Android:** Check app has permission: `android.permission.WRITE_EXTERNAL_STORAGE`
- **iOS:** Ensure app data is not cleared by system
- **Desktop:** Verify `~/.eu_anifantakis_ksafe/` directory exists and is writable

### Issue: Colors Look Washed Out or Wrong

**Symptoms:** Material3 colors don't match your custom seed

**Solution:** Check if Dynamic Color is enabled (overrides custom color):

```kotlin
// Disable dynamic color to use custom color
ThemeManager.isDynamicColor = false
```

### Issue: AMOLED Toggle Not Showing

**Symptoms:** "Pitch Black" option missing in settings

**Solution:** It only shows when dark mode is active:

```kotlin
if (ThemeManager.isDarkTheme) {
    // AMOLED option is shown
}
```

### Issue: Color Picker Crashes on Android

**Symptoms:** App crashes when opening color picker

**Solution:** Ensure `compose-material-icons-extended:1.7.3` is in your `libs.versions.toml`:

```toml
[versions]
composeMaterialIcons = "1.7.3"

[libraries]
compose-material-icons-extended = { module = "org.jetbrains.compose.material:material-icons-extended", version.ref = "composeMaterialIcons" }
```

Then rebuild:
```bash
./gradlew clean build
```

### Issue: Voyager Navigator Not Working

**Symptoms:** Screen doesn't transition, back button doesn't work

**Solution:** Wrap with `SlideTransition` and use `LocalNavigator.currentOrThrow`:

```kotlin
// ✅ CORRECT
KCF {
    Navigator(KCFSettingsScreen(groups = emptyList())) { navigator ->
        SlideTransition(navigator)
    }
}
```

---

## Complete Example Project

### Full `App.kt`

```kotlin
package com.example.myapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.kanhaji.core.settings.KCFSettingsScreen
import com.kanhaji.core.theme.KCF
import androidx.compose.material3.Scaffold
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings

@Composable
@Preview
fun App() {
    KCF {
        Navigator(HomeScreen()) { navigator ->
            Scaffold(
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
                SlideTransition(navigator)
            }
        }
    }
}
```

### Full `MainActivity.kt` (Android)

```kotlin
package com.example.myapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.kanhaji.core.storage.initKSafe

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize KSafe FIRST
        initKSafe(context = this)
        
        setContent {
            App()
        }
    }
}
```

### Full `SettingsScreen.kt`

```kotlin
package com.example.myapp

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.kanhaji.core.settings.KCFSettingsScreen

object SettingsScreen : Screen {
    @Composable
    override fun Content() {
        // Shows theme settings + any custom groups
        KCFSettingsScreen(groups = emptyList())
    }
}
```

### Full `HomeScreen.kt`

```kotlin
package com.example.myapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import cafe.adriel.voyager.core.screen.Screen

object HomeScreen : Screen {
    @Composable
    override fun Content() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Welcome to KCF!",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
```

---

## Tips & Best Practices

### 1. Always Initialize KSafe First

```kotlin
fun main() {
    initKSafe()  // ← Before anything else
    runApplication { App() }
}
```

### 2. Use `remember` for ThemeManager State

```kotlin
@Composable
fun MyComponent() {
    val isDark = remember { ThemeManager.isDarkTheme }
    // Now `isDark` triggers recomposition when theme changes
}
```

### 3. Persist Custom App Settings Too

```kotlin
// If your app has settings beyond theming:
val prefs = KSafeProvider.prefs
putDirect(prefs, "my_app_setting", "value")
val saved = getDirect(prefs, "my_app_setting", "default")
```

### 4. Test Theme Switching

```kotlin
// Quickly toggle theme during development
ThemeManager.setTheme(ThemeManager.ThemeType.DARK)
ThemeManager.isAmoled = true
ThemeManager.isDynamicColor = false
```

### 5. Always Use KCF Wrapper

Never use `MaterialTheme` directly without `KCF`:

```kotlin
// ✅ CORRECT
KCF {
    MyApp()
}

// ❌ WRONG - colors won't update
MaterialTheme {
    MyApp()
}
```

---

## Next Steps

- 🎨 **Customize colors** → Modify `ThemeManager.customColor`
- 📱 **Add app settings** → Create custom `SettingItems` groups
- 🌐 **Multi-language** → Wrap settings strings with i18n
- 🔒 **Secure storage** → Use `KSafeProvider.secure` for tokens

---

## Support

For issues or questions:
1. Check [Troubleshooting](#troubleshooting) section
2. Review [API Reference](#api-reference)
3. File an issue on GitHub with:
   - Platform (Android/iOS/Desktop)
   - Error message
   - Minimal reproducible example

---

**Happy theming! 🎉**

