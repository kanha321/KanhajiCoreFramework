# KCF Documentation Index

Complete guide to using Kanha Core Framework in your Kotlin Multiplatform projects.

---

## 📚 Documentation Files

### For Getting Started (Pick One)

| Document | Time | Best For |
|----------|------|----------|
| **[QUICKSTART.md](./QUICKSTART.md)** | 15 min | **New to KCF?** Step-by-step setup guide |
| **[GUIDE.md](./GUIDE.md)** | 30 min | **Want all details?** Complete reference with examples |
| **[CHEATSHEET.md](./CHEATSHEET.md)** | 2 min | **Looking up an API?** Quick reference table |

---

### For Understanding Architecture

| Document | Purpose |
|----------|---------|
| **[PROJECT_STRUCTURE.md](./PROJECT_STRUCTURE.md)** | How KCF is organized and where each component lives |
| **[GRADLE_DEPENDENCY_VERIFICATION_REPORT.md](./GRADLE_DEPENDENCY_VERIFICATION_REPORT.md)** | Dependency verification and compatibility matrix |

---

## 🎯 Quick Navigation

### I want to...

#### **"Get KCF working in my project in 15 minutes"**
→ Read **[QUICKSTART.md](./QUICKSTART.md)**
- 7 short steps
- Copy-paste code examples
- Troubleshooting included

#### **"Understand how KCF works"**
→ Read **[PROJECT_STRUCTURE.md](./PROJECT_STRUCTURE.md)**
- Architecture overview
- Data flow diagrams
- Module breakdown

#### **"Use KCF in my app"**
→ Read **[GUIDE.md](./GUIDE.md)**
- Complete API reference
- Customization patterns
- Advanced features

#### **"Quickly look up an API"**
→ Check **[CHEATSHEET.md](./CHEATSHEET.md)**
- Tables of all functions
- Common patterns
- Quick examples

#### **"Fix a problem"**
→ Search in **[GUIDE.md](./GUIDE.md#troubleshooting)** Troubleshooting section

#### **"Verify dependencies are correct"**
→ Check **[GRADLE_DEPENDENCY_VERIFICATION_REPORT.md](./GRADLE_DEPENDENCY_VERIFICATION_REPORT.md)**

---

## 🚀 Three-Minute Overview

### What is KCF?

A **reusable Kotlin Multiplatform foundation** that provides:
- ✅ **Theming system** (Light/Dark/System mode)
- ✅ **Color customization** (seed colors, dynamic colors)
- ✅ **AMOLED support** (black backgrounds on OLED)
- ✅ **Persistent settings** (survives app restart)
- ✅ **Ready-made UI** (settings screen, color picker)
- ✅ **Multi-platform** (Android, iOS, Desktop)

### How it works

```
1. Initialize KSafe (once at startup)
2. Wrap app with KCF composable
3. Use KCFSettingsScreen for settings UI
4. Access ThemeManager to read/update theme
5. All changes auto-save to device storage
```

### Minimal code

```kotlin
// Android MainActivity.kt
initKSafe(this)
setContent { App() }

// App.kt
KCF {
    Navigator(KCFSettingsScreen(groups = emptyList())) { navigator ->
        SlideTransition(navigator)
    }
}
```

That's it! You now have a complete theming system.

---

## 📖 Reading Order

### For Complete Beginners
1. **This file** (you are here) — overview
2. **[QUICKSTART.md](./QUICKSTART.md)** — hands-on setup
3. **[CHEATSHEET.md](./CHEATSHEET.md)** — quick reference
4. **[GUIDE.md](./GUIDE.md)** — deep dive

### For Experienced Developers
1. **[PROJECT_STRUCTURE.md](./PROJECT_STRUCTURE.md)** — architecture
2. **[GUIDE.md](./GUIDE.md)** — API reference
3. **[GRADLE_DEPENDENCY_VERIFICATION_REPORT.md](./GRADLE_DEPENDENCY_VERIFICATION_REPORT.md)** — dependencies

### For Reference During Development
- Keep **[CHEATSHEET.md](./CHEATSHEET.md)** open
- Refer to **[GUIDE.md](./GUIDE.md)** for API details
- Check **[PROJECT_STRUCTURE.md](./PROJECT_STRUCTURE.md)** for architecture questions

---

## 🎓 Learning Path

```
Start Here
    ↓
Are you new to KCF?
├─ YES → QUICKSTART.md (15 min)
│         ↓
│    Got it working?
│    ├─ YES → CHEATSHEET.md (for reference)
│    │       + GUIDE.md (when needed)
│    └─ NO  → Check GUIDE.md Troubleshooting
│
└─ NO  → Pick a task below
        ↓
        What do you need?
        ├─ API lookup     → CHEATSHEET.md
        ├─ Code example   → GUIDE.md
        ├─ Understand     → PROJECT_STRUCTURE.md
        ├─ Fix issue      → GUIDE.md Troubleshooting
        └─ Dependency Q   → GRADLE_DEPENDENCY_VERIFICATION_REPORT.md
```

---

## 📋 Core Concepts

### ThemeManager
Global state holder for all theme values. Changes here trigger app-wide recomposition.

```kotlin
ThemeManager.setTheme(ThemeType.DARK)
ThemeManager.isAmoled = true
ThemeManager.customColor.value = Color(0xFF6750A4)
```

**Learn more:** [GUIDE.md — Using the Theming System](./GUIDE.md#using-the-theming-system)

---

### KCF Composable
Root theme wrapper. Applies Material3 styling and handles color generation.

```kotlin
KCF {
    // Your entire app goes here
}
```

**Learn more:** [GUIDE.md — Wrapping Your App with KCF](./GUIDE.md#wrapping-your-app-with-kcf)

---

### KCFSettingsScreen
Voyager Screen showing theme settings + your custom settings.

```kotlin
KCFSettingsScreen(groups = listOf(...))
```

**Learn more:** [GUIDE.md — Settings Screen Integration](./GUIDE.md#settings-screen-integration)

---

### KSafeProvider
Persistence layer. Saves/loads settings from device storage.

```kotlin
val prefs = KSafeProvider.prefs
putDirect(prefs, "key", "value")
val value = getDirect(prefs, "key", "default")
```

**Learn more:** [GUIDE.md — Core Initialization](./GUIDE.md#core-initialization)

---

## 🔧 Common Tasks

### Task: Change the app theme

```kotlin
import com.kanhaji.core.theme.ThemeManager

ThemeManager.setTheme(ThemeManager.ThemeType.DARK)
```

**Where:** Anywhere in your code
**When:** Immediately updates entire app

---

### Task: Read current theme in a composable

```kotlin
import com.kanhaji.core.theme.ThemeManager

@Composable
fun MyComponent() {
    val isDark = remember { ThemeManager.isDarkTheme }
    Text(if (isDark) "Dark mode" else "Light mode")
}
```

**Why remember:** Subscribes to theme changes

---

### Task: Save custom app preference

```kotlin
import com.kanhaji.core.storage.KSafeProvider
import com.kanhaji.core.storage.putDirect
import com.kanhaji.core.storage.getDirect

putDirect(KSafeProvider.prefs, "my_key", "my_value")
val loaded = getDirect(KSafeProvider.prefs, "my_key", "default")
```

**When:** Persists immediately across app restarts

---

### Task: Add custom settings group

```kotlin
KCFSettingsScreen(
    groups = listOf(
        Group(
            header = "My Settings",
            items = listOf(
                SettingItems(
                    id = "my_setting",
                    title = "My Setting",
                    description = "...",
                    icon = Icons.Outlined.Settings,
                    widget = { Switch(...) },
                    onClick = { /* ... */ }
                )
            )
        )
    )
)
```

**Where:** In your `SettingsScreen`

---

## ✅ Checklist: Getting Started

- [ ] Read **[QUICKSTART.md](./QUICKSTART.md)**
- [ ] Add KCF module to your project
- [ ] Update `settings.gradle.kts` with KCF include
- [ ] Update `build.gradle.kts` with KCF dependency
- [ ] Call `initKSafe()` in `onCreate()` or `main()`
- [ ] Wrap app with `KCF { ... }`
- [ ] Use `KCFSettingsScreen(groups = emptyList())`
- [ ] Run app and test theme switching
- [ ] Save **[CHEATSHEET.md](./CHEATSHEET.md)** for reference

---

## 🐛 Troubleshooting Quick Links

| Problem | Solution |
|---------|----------|
| "KSafeProvider not initialized" | [GUIDE.md — Troubleshooting](./GUIDE.md#troubleshooting) |
| Theme not persisting | [GUIDE.md — Troubleshooting](./GUIDE.md#issue-theme-not-persisting-across-app-restarts) |
| Colors look wrong | [GUIDE.md — Troubleshooting](./GUIDE.md#issue-colors-look-washed-out-or-wrong) |
| Color picker crashes | [GUIDE.md — Troubleshooting](./GUIDE.md#issue-color-picker-crashes-on-android) |
| Can't find module | [QUICKSTART.md — Troubleshooting](./QUICKSTART.md#troubleshooting-this-guide) |

---

## 🎯 Success Metrics

After setting up KCF, you should have:

- ✅ App launches without crashing
- ✅ Settings screen accessible from navigation
- ✅ Theme button opens dialog with Light/Dark/System options
- ✅ Selecting theme immediately changes app colors
- ✅ Settings persist across app restart
- ✅ (Android 12+) Dynamic color toggle appears
- ✅ Color picker opens and allows custom colors

If any of the above is missing, check the [GUIDE.md Troubleshooting](./GUIDE.md#troubleshooting) section.

---

## 📞 Support Resources

### Documentation
- 📖 **[GUIDE.md](./GUIDE.md)** — Full reference with examples
- ⚡ **[CHEATSHEET.md](./CHEATSHEET.md)** — Quick API lookup
- 🏗️ **[PROJECT_STRUCTURE.md](./PROJECT_STRUCTURE.md)** — Architecture
- 🚀 **[QUICKSTART.md](./QUICKSTART.md)** — Setup guide

### External Resources
- 🔗 [Jetbrains Compose Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-multiplatform.html)
- 🔗 [Voyager Navigation](https://voyager.adriel.cafe/)
- 🔗 [Material Kolor](https://github.com/jordond/materialkolor)
- 🔗 [KSafe Storage](https://github.com/ioannisa/KSafe)

---

## 📝 Document Versions

| Document | Last Updated | Version |
|----------|--------------|---------|
| QUICKSTART.md | March 22, 2026 | 1.0.0 |
| GUIDE.md | March 22, 2026 | 1.0.0 |
| CHEATSHEET.md | March 22, 2026 | 1.0.0 |
| PROJECT_STRUCTURE.md | March 22, 2026 | 1.0.0 |
| GRADLE_DEPENDENCY_VERIFICATION_REPORT.md | March 22, 2026 | 1.0.0 |
| **INDEX.md (this file)** | **March 22, 2026** | **1.0.0** |

---

## 🎉 Ready to Begin?

### First time?
→ Start with **[QUICKSTART.md](./QUICKSTART.md)** (15 minutes)

### Want complete reference?
→ Read **[GUIDE.md](./GUIDE.md)** (30 minutes)

### Need quick lookup?
→ Use **[CHEATSHEET.md](./CHEATSHEET.md)** (anytime)

### Understanding architecture?
→ Check **[PROJECT_STRUCTURE.md](./PROJECT_STRUCTURE.md)**

---

**Choose a guide above and start building! 🚀**

---

*Made with ❤️ for Kotlin Multiplatform developers*

**Questions?** Check the relevant guide above. Most answers are there!

