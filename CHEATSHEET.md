# KCF Quick Reference Cheat Sheet

## 🚀 One-Minute Setup

### 1. Initialize (once at app startup)
```kotlin
// Android
initKSafe(context = this)

// iOS/Desktop
initKSafe()
```

### 2. Wrap app
```kotlin
KCF {
    Navigator(KCFSettingsScreen(groups = emptyList())) { navigator ->
        SlideTransition(navigator)
    }
}
```

### 3. Done ✅

---

## 📦 Core Imports

```kotlin
// Theme
import com.kanhaji.core.theme.KCF
import com.kanhaji.core.theme.ThemeManager

// Settings
import com.kanhaji.core.settings.KCFSettingsScreen
import com.kanhaji.core.settings.Group
import com.kanhaji.core.settings.SettingItems

// Storage
import com.kanhaji.core.storage.initKSafe
import com.kanhaji.core.storage.KSafeProvider
import com.kanhaji.core.storage.getDirect
import com.kanhaji.core.storage.putDirect

// Navigation
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
```

---

## 🎨 ThemeManager API

| Operation | Code |
|-----------|------|
| Get current theme type | `ThemeManager.currentThemeType` |
| Set light mode | `ThemeManager.setTheme(ThemeType.LIGHT)` |
| Set dark mode | `ThemeManager.setTheme(ThemeType.DARK)` |
| Use system theme | `ThemeManager.setTheme(ThemeType.SYSTEM)` |
| Is dark active? | `ThemeManager.isDarkTheme` |
| Toggle AMOLED | `ThemeManager.isAmoled = true` |
| Use device colors | `ThemeManager.isDynamicColor = true` |
| Custom seed color | `ThemeManager.customColor.value = Color(0xFF6750A4)` |

---

## 💾 KSafe Persistence API

| Operation | Code |
|-----------|------|
| Get int | `getDirect(prefs, "key", 42)` |
| Get boolean | `getDirect(prefs, "key", false)` |
| Get string | `getDirect(prefs, "key", "default")` |
| Save int | `putDirect(prefs, "key", 42)` |
| Save boolean | `putDirect(prefs, "key", true)` |
| Save string | `putDirect(prefs, "key", "value")` |
| Access prefs | `KSafeProvider.prefs` |
| Access secure | `KSafeProvider.secure` |

---

## ⚙️ Settings UI

### Basic (Built-in theme settings only)
```kotlin
KCFSettingsScreen(groups = emptyList())
```

### With Custom Groups
```kotlin
KCFSettingsScreen(
    groups = listOf(
        Group(
            header = "My Settings",
            items = listOf(
                SettingItems(
                    id = "my_setting",
                    title = "My Setting",
                    description = "Description",
                    icon = Icons.Outlined.Settings,
                    widget = { Switch(checked = ..., onCheckedChange = ...) },
                    onClick = { /* Handle click */ }
                )
            )
        )
    )
)
```

---

## 🔧 Common Patterns

### Read theme state in composable
```kotlin
val isDark = remember { ThemeManager.isDarkTheme }
Text(if (isDark) "🌙 Dark" else "☀️ Light")
```

### Check platform support
```kotlin
if (isDynamicColorSupported()) {
    // Android 12+ only
}
```

### Conditional rendering based on theme
```kotlin
Box(
    modifier = Modifier.background(
        color = if (ThemeManager.isDarkTheme) 
            Color.Black else Color.White
    )
)
```

### Save custom preference
```kotlin
val prefs = KSafeProvider.prefs
putDirect(prefs, "my_color", "#FF5733")
val color = getDirect(prefs, "my_color", "#6750A4")
```

---

## 🐛 Common Issues & Fixes

| Issue | Fix |
|-------|-----|
| "KSafeProvider not initialized" | Call `initKSafe()` before rendering UI |
| Theme not persisting | Verify KSafe has device storage access |
| Colors look wrong | Check if `isDynamicColor` is enabled (override) |
| AMOLED toggle missing | Only shows when `isDarkTheme == true` |
| Color picker crashes | Verify `composeMaterialIcons = "1.7.3"` in versions |
| Navigator not transitioning | Wrap with `SlideTransition(navigator)` |

---

## 📋 File Checklist

- [ ] `gradle/libs.versions.toml` — has KCF dependencies
- [ ] `settings.gradle.kts` — includes KCF core module + Compose dev repo
- [ ] `composeApp/build.gradle.kts` — depends on core module
- [ ] `MainActivity.kt` — calls `initKSafe(context)` in `onCreate()`
- [ ] `App.kt` — wraps with `KCF { ... }`
- [ ] `SettingsScreen.kt` — uses `KCFSettingsScreen(groups = ...)`

---

## 🎯 Architecture

```
App (wrapped with KCF theme provider)
├── Navigator (Voyager)
│   ├── HomeScreen
│   ├── SettingsScreen (uses KCFSettingsScreen)
│   └── OtherScreens
├── ThemeManager (state)
├── KSafeProvider (persistence)
└── SlideTransition (animations)
```

---

## 📚 Full Docs

See **GUIDE.md** for:
- Detailed setup instructions
- Complete code examples
- Advanced customization
- Troubleshooting guide
- API reference

---

## 💡 Pro Tips

1. **Always remember state**: Use `remember { ThemeManager.isDarkTheme }` in composables to react to theme changes
2. **Batch updates**: Change multiple `ThemeManager` fields at once before rendering
3. **Test theme switching**: Toggle `ThemeManager.setTheme()` during development
4. **Use secure storage**: Store tokens in `KSafeProvider.secure` (encrypted)
5. **Monitor recompositions**: Wrap expensive composables with `remember`

---

**Last updated**: March 22, 2026  
**KCF Version**: 1.0.0
