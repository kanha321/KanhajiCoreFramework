# KCF Project Structure Reference

Understanding how KCF is organized and how to use each module.

---

## Directory Layout

```
Template/                                          (root project)
├── gradle/
│   ├── libs.versions.toml                       (dependency catalog)
│   └── wrapper/
├── settings.gradle.kts                          (Gradle settings + modules)
├── build.gradle.kts                             (root build config)
│
├── frameworks/
│   └── kcf/
│       └── core/                                (← KCF Core Module)
│           ├── build.gradle.kts
│           ├── src/
│           │   ├── commonMain/
│           │   │   └── kotlin/com/kanhaji/core/
│           │   │       ├── settings/            (Settings UI & state)
│           │   │       │   ├── KCFSettingsScreen.kt
│           │   │       │   ├── KCFSettingsComponent.kt
│           │   │       │   ├── KCFSettingsScreenModel.kt
│           │   │       │   ├── KCFThemeGroup.kt
│           │   │       │   ├── SettingsCard.kt
│           │   │       │   ├── SettingItems.kt
│           │   │       │   └── components/
│           │   │       │       ├── ThemeSelectionDialog.kt
│           │   │       │       └── ColorPickerDialog.kt
│           │   │       ├── theme/               (Theme management)
│           │   │       │   ├── KCF.kt
│           │   │       │   ├── ThemeManager.kt
│           │   │       │   └── DynamicColorSupport.kt
│           │   │       └── storage/             (Persistence layer)
│           │   │           └── KSafeProvider.kt
│           │   ├── androidMain/
│           │   │   ├── AndroidManifest.xml
│           │   │   └── kotlin/com/kanhaji/core/
│           │   │       ├── theme/DynamicColorSupport.android.kt
│           │   │       └── storage/
│           │   │           ├── KSafePlatformInit.android.kt
│           │   │           └── KSafeWrapper.android.kt
│           │   ├── iosMain/
│           │   │   └── kotlin/com/kanhaji/core/
│           │   │       ├── theme/DynamicColorSupport.ios.kt
│           │   │       └── storage/
│           │   │           ├── KSafePlatformInit.ios.kt
│           │   │           └── KSafeWrapper.ios.kt
│           │   └── jvmMain/
│           │       └── kotlin/com/kanhaji/core/
│           │           ├── theme/DynamicColorSupport.jvm.kt
│           │           └── storage/
│           │               ├── KSafePlatformInit.jvm.kt
│           │               └── KSafeWrapper.jvm.kt
│
├── composeApp/                                  (← Your App Module)
│   ├── build.gradle.kts
│   └── src/
│       ├── commonMain/
│       │   └── kotlin/com/example/myapp/
│       │       ├── App.kt
│       │       ├── HomeScreen.kt
│       │       └── SettingsScreen.kt
│       ├── androidMain/
│       │   ├── AndroidManifest.xml
│       │   └── kotlin/com/example/myapp/
│       │       └── MainActivity.kt
│       ├── iosMain/
│       │   └── kotlin/com/example/myapp/
│       └── jvmMain/
│           └── kotlin/com/example/myapp/
│               └── Main.kt
│
├── GUIDE.md                                     (← Full documentation)
├── QUICKSTART.md                                (← 15-minute setup)
├── CHEATSHEET.md                                (← API reference)
└── README.md
```

---

## Module Breakdown

### `frameworks/kcf/core/` (KCF Core)

The reusable framework library. This is **not** an app; it's a library that any KMP app can depend on.

#### Responsibilities:
- ✅ Theme management (`ThemeManager`)
- ✅ Settings UI (`KCFSettingsScreen`, `KCFThemeGroup`)
- ✅ Persistent storage abstraction (`KSafeProvider`)
- ✅ Dynamic color support (`isDynamicColorSupported()`)
- ✅ Reusable components (`SettingsCard`, dialogs)
- ✅ Voyager screen models

#### What It Does NOT Do:
- ❌ Not an app (no `Main.kt` or `MainActivity`)
- ❌ Not opinionated about navigation structure
- ❌ Not a UI design system (just settings + theming)
- ❌ Not enforcing any architecture pattern

#### Key Files:

| File | Purpose |
|------|---------|
| `KCFSettingsScreen.kt` | Voyager Screen that renders the entire settings UI |
| `KCFSettingsComponent.kt` | Pure composable UI (renders groups of settings) |
| `KCFSettingsScreenModel.kt` | Voyager ScreenModel managing settings state |
| `KCFThemeGroup.kt` | Pre-built theme settings group (theme, AMOLED, color) |
| `ThemeManager.kt` | Global state holder for all theme values |
| `KCF.kt` | Root theme wrapper composable |
| `KSafeProvider.kt` | Abstraction over KSafe persistence |
| `DynamicColorSupport.kt` | expect/actual for platform-specific dynamic color |

---

### `composeApp/` (Sample App)

A minimal example app showing how to use KCF. This is what you would replicate for your own app.

#### Structure:
- **commonMain/** — Shared code across all platforms
- **androidMain/** — Android-specific code (manifests, permissions)
- **iosMain/** — iOS-specific code (Swift interop)
- **jvmMain/** — Desktop (JVM) code

#### Key Files:

| File | Purpose |
|------|---------|
| `App.kt` | Main composable wrapping app with `KCF` theme |
| `MainActivity.kt` | Android entry point (calls `initKSafe()`) |
| `Main.kt` | JVM/Desktop entry point |
| `iOSApp.swift` | iOS entry point |

---

## Understanding the Architecture

### Data Flow

```
User interacts with Settings UI
         ↓
KCFSettingsScreenModel updates ThemeManager
         ↓
ThemeManager updates mutableStateOf values
         ↓
KCF composable recomposes with new colors
         ↓
KSafeProvider saves to device storage
         ↓
Settings persisted across app restarts
```

### Component Hierarchy

```
KCF (theme provider)
├── Navigator (Voyager navigation)
│   └── KCFSettingsScreen (a Screen)
│       └── KCFSettingsComponent (pure UI)
│           ├── GroupedLazyColumn
│           │   ├── Group (Theme)
│           │   │   ├── SettingsCard (App Theme button)
│           │   │   ├── SettingsCard (Pitch Black switch)
│           │   │   └── SettingsCard (App Color picker)
│           │   └── Group (Your custom group)
│           │       └── SettingsCard (Your settings)
│           └── Dialogs
│               ├── ThemeSelectionDialog
│               └── ColorPickerDialog
```

### State Management

```
ThemeManager (object singleton)
├── currentThemeType: ThemeType (LIGHT, DARK, SYSTEM)
├── isDarkTheme: Boolean (resolved from currentThemeType)
├── isAmoled: Boolean (black backgrounds in dark)
├── isDynamicColor: Boolean (device-derived colors)
└── customColor: MutableState<Color>
    
    ↓ (state changes trigger recomposition)
    
KCF composable recomposes
    
    ↓
    
KSafeProvider.prefs.putDirect() persists
```

---

## How to Add Your App

### Minimal App Setup

Create your own `composeApp` (or rename the sample one):

```
myapp/
├── src/
│   ├── commonMain/
│   │   └── kotlin/
│   │       ├── App.kt              ← Wrap with KCF
│   │       ├── HomeScreen.kt
│   │       └── SettingsScreen.kt
│   ├── androidMain/
│   │   └── kotlin/
│   │       └── MainActivity.kt      ← Call initKSafe()
│   ├── iosMain/
│   │   └── kotlin/ (or .swift for iOS)
│   └── jvmMain/
│       └── kotlin/
│           └── Main.kt             ← Call initKSafe()
└── build.gradle.kts                ← Depend on `:frameworks:kcf:core`
```

### Minimal Code

**MainActivity.kt**
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initKSafe(context = this)
        setContent { App() }
    }
}
```

**App.kt**
```kotlin
@Composable
fun App() {
    KCF {
        Navigator(KCFSettingsScreen(groups = emptyList())) { navigator ->
            SlideTransition(navigator)
        }
    }
}
```

Done! Your app now has full theming.

---

## Platform-Specific Implementations

### Expect/Actual Pattern

KCF uses Kotlin's expect/actual mechanism for platform-specific features:

#### Common (commonMain)
```kotlin
expect class KSafeWrapper
expect fun isDynamicColorSupported(): Boolean
```

#### Actual (androidMain/iosMain/jvmMain)
```kotlin
actual typealias KSafeWrapper = KSafe
actual fun isDynamicColorSupported(): Boolean = ...
```

**Benefits:**
- ✅ Common code stays platform-agnostic
- ✅ Platform-specific logic is isolated
- ✅ Compiler ensures all platforms implement required functions
- ✅ No runtime checks needed

---

## Dependency Graph

```
frameworks/kcf/core/
├── compose-runtime:1.10.0
├── compose-material3:1.10.0-alpha05
├── voyager-navigator:1.1.0-beta03
├── ksafe:1.7.1
├── material-kolor:4.1.1
└── colorpicker-compose:1.1.3

composeApp/
├── frameworks/kcf/core/
├── voyager-transitions:1.1.0-beta03
└── androidx-activity-compose:1.12.2
```

**Key Point:** `composeApp` depends on `kcf/core`, so all KCF dependencies are transitively available.

---

## File Organization Philosophy

### `commonMain/` — Platform-agnostic

All UI, state management, and business logic that works on all platforms:
- Settings screens
- Theme manager
- Dialogs and cards
- Navigation

**Rule:** Never import platform-specific APIs here (no `android.*`, no Swift)

### `androidMain/` — Android-only

Platform-specific implementations:
- KSafe initialization with Context
- Dynamic color detection (API level check)
- Android manifest configuration

**Rule:** Can import `android.*` packages

### `iosMain/` — iOS-only

Platform-specific implementations:
- KSafe initialization (no Context)
- iOS-specific threading

**Rule:** Can use Swift interop

### `jvmMain/` — Desktop-only

Platform-specific implementations:
- KSafe initialization (file-based)
- Desktop-specific UI tweaks

**Rule:** Can use JVM libraries

---

## Extending KCF

### Add Custom Theme Setting

```kotlin
// In your app code
@Composable
fun MyCustomThemeGroup(model: KCFSettingsScreenModel): Group<SettingItems> {
    return Group(
        header = "My Theme Settings",
        items = listOf(
            SettingItems(
                id = "my_theme_option",
                title = "My Option",
                description = "Description",
                icon = Icons.Outlined.Settings,
                widget = { Switch(...) },
                onClick = { /* handle */ }
            )
        )
    )
}

// Use it
KCFSettingsScreen(
    groups = listOf(MyCustomThemeGroup(model))
)
```

### Add Custom Persistence

```kotlin
// Use KSafeProvider for custom app data
val prefs = KSafeProvider.prefs
putDirect(prefs, "my_custom_key", "my_value")
val loaded = getDirect(prefs, "my_custom_key", "default")
```

### Override Theme Colors Programmatically

```kotlin
// Respond to external events
LaunchedEffect(externalEvent) {
    ThemeManager.customColor.value = newColor
    ThemeManager.setTheme(ThemeManager.ThemeType.DARK)
}
```

---

## Common Mistakes

### ❌ Mistake 1: Calling `initKSafe()` in composable

```kotlin
@Composable
fun App() {
    initKSafe()  // ❌ Too late! Will crash
    ...
}
```

**✅ Fix:**
```kotlin
fun main() {
    initKSafe()  // ✅ Before rendering
    runApplication { App() }
}
```

---

### ❌ Mistake 2: Not wrapping with KCF

```kotlin
@Composable
fun App() {
    MaterialTheme {  // ❌ Colors won't update
        MyScreen()
    }
}
```

**✅ Fix:**
```kotlin
@Composable
fun App() {
    KCF {  // ✅ Always use KCF
        MyScreen()
    }
}
```

---

### ❌ Mistake 3: Importing Android classes in commonMain

```kotlin
// commonMain/kotlin/App.kt
import android.content.Context  // ❌ Not available on iOS/Desktop!
```

**✅ Fix:**
```kotlin
// Put Android-specific code in androidMain/
// commonMain/ stays pure Kotlin
```

---

## Testing KCF

### Unit Test Example

```kotlin
@Test
fun themeManagerToggleDarkMode() {
    ThemeManager.setTheme(ThemeManager.ThemeType.DARK)
    assertTrue(ThemeManager.isDarkTheme)
    
    ThemeManager.setTheme(ThemeManager.ThemeType.LIGHT)
    assertFalse(ThemeManager.isDarkTheme)
}
```

### UI Test Example

```kotlin
@Test
fun settingsScreenShowsThemeButton() {
    composeTestRule.setContent {
        KCF {
            KCFSettingsScreen(groups = emptyList())
        }
    }
    
    composeTestRule.onNodeWithText("App Theme").assertExists()
}
```

---

## Performance Considerations

### Recomposition

- `ThemeManager` fields are mutable state → triggers recomposition
- Wrap expensive composables with `remember` to avoid re-rendering
- Use `remember { ThemeManager.isDarkTheme }` to subscribe to theme changes

### Memory

- KSafeProvider is a singleton → lightweight
- Theme colors are cached in `MaterialTheme` → no recalculation
- Settings are lazy-loaded on first access

### Persistence

- KSafe writes happen synchronously (use `putDirect`, not async)
- Minimal I/O — only on theme changes (not per frame)
- Encrypted storage is handled by KSafe transparently

---

## Versioning & Updates

### Current Versions

| Component | Version | Status |
|-----------|---------|--------|
| KCF Core | 1.0.0 | Stable |
| Compose MP | 1.10.0 | Stable |
| Voyager | 1.1.0-beta03 | Pre-release |
| KSafe | 1.7.1 | Stable |

### Checking for Updates

```bash
# View available versions
./gradlew dependencyUpdates

# Update version in libs.versions.toml
# Then rebuild
./gradlew clean build
```

---

## Support & Resources

| Resource | Purpose |
|----------|---------|
| `GUIDE.md` | Complete documentation with examples |
| `QUICKSTART.md` | 15-minute setup guide |
| `CHEATSHEET.md` | Quick API reference |
| `GRADLE_DEPENDENCY_VERIFICATION_REPORT.md` | Dependency audit |

---

**Architecture designed for simplicity and reusability** ✨

---

**Last updated**: March 22, 2026  
**KCF Version**: 1.0.0-stable
