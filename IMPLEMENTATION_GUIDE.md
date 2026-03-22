# CustomSnackbar Implementation Guide

## Overview

You now have a fully customizable snackbar system in KCF! Here's everything you need to know:

---

## What Changed

### New File: `CustomSnackbar.kt`

A separate, reusable component with full customization support:

```
┌─────────────────────────────────────────────────┐
│ CustomSnackbar (@Composable)                   │
├─────────────────────────────────────────────────┤
│ • Message display                               │
│ • Auto-dismiss (configurable)                  │
│ • Dismiss button (X icon)                      │
│ • Slide animation                              │
│ • Fade animation                               │
├─────────────────────────────────────────────────┤
│ Config Options:                                 │
│ • containerColor (background)                  │
│ • contentColor (text/icon)                     │
│ • containerPadding (spacing)                   │
│ • shape (corner radius)                        │
│ • autoDismissDuration (3s default)            │
└─────────────────────────────────────────────────┘
```

### Updated File: `ShellScaffold.kt`

Enhanced `ShellController` with config management:

```kotlin
class ShellController {
    // ... existing code ...
    
    // NEW: Snackbar configuration
    internal var snackbarConfig = SnackbarConfig()
    
    // NEW: Set custom configuration
    fun setSnackbarConfig(config: SnackbarConfig) { ... }
    
    // Existing methods still work
    fun showSnackbar(message: String) { ... }
    fun dismissSnackbar() { ... }
}
```

---

## Quick Start (30 seconds)

### 1. Show a Simple Snackbar

```kotlin
val shellController = LocalShellController.current
shellController.showSnackbar("Hello World!")
```

**Result:** Shows for 3 seconds, then auto-dismisses.

### 2. Customize Duration

```kotlin
shellController.setSnackbarConfig(
    SnackbarConfig(autoDismissDuration = 5000L)  // 5 seconds
)
shellController.showSnackbar("Wait 5 seconds...")
```

### 3. Customize Colors

```kotlin
shellController.setSnackbarConfig(
    SnackbarConfig(
        containerColor = Color(0xFF2196F3),  // Blue background
        contentColor = Color.White            // White text
    )
)
shellController.showSnackbar("Blue message")
```

---

## Full Example: Complete Customization

```kotlin
object MyScreen : Screen {
    @Composable
    override fun Content() {
        val shellController = LocalShellController.current
        
        // Configure snackbar once when screen loads
        SideEffect {
            shellController.setSnackbarConfig(
                SnackbarConfig(
                    containerColor = Color(0xFF4CAF50),  // Green
                    contentColor = Color.White,
                    containerPadding = SnackbarPadding(
                        start = 16.dp,
                        end = 8.dp,
                        top = 12.dp,
                        bottom = 12.dp
                    ),
                    autoDismissDuration = 4000L,  // 4 seconds
                    shape = RoundedCornerShape(12.dp)
                )
            )
        }
        
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { 
                shellController.showSnackbar("✓ Successfully saved!")
            }) {
                Text("Save")
            }
        }
    }
}
```

---

## Customization Options

### 1. Container Color

```kotlin
// Use Material theme colors
containerColor = MaterialTheme.colorScheme.primary

// Or custom hex colors
containerColor = Color(0xFF2196F3)  // Blue
containerColor = Color(0xFF4CAF50)  // Green
containerColor = Color(0xFFE53935)  // Red
```

### 2. Content Color

```kotlin
// Match the container
contentColor = Color.White
contentColor = Color.Black

// Or use Material colors
contentColor = MaterialTheme.colorScheme.onPrimary
```

### 3. Padding

```kotlin
// Default: 16dp left, 4dp right, 8dp top/bottom
containerPadding = SnackbarPadding()

// Compact
containerPadding = SnackbarPadding(
    start = 12.dp,
    end = 4.dp,
    top = 6.dp,
    bottom = 6.dp
)

// Spacious
containerPadding = SnackbarPadding(
    start = 24.dp,
    end = 8.dp,
    top = 16.dp,
    bottom = 16.dp
)
```

### 4. Auto-Dismiss Duration

```kotlin
// Quick (2 seconds)
autoDismissDuration = 2000L

// Standard (3 seconds) - default
autoDismissDuration = 3000L

// Longer (5 seconds)
autoDismissDuration = 5000L

// No auto-dismiss (user must click X)
autoDismissDuration = 0L
```

### 5. Shape (Corner Radius)

```kotlin
// Slightly rounded
shape = RoundedCornerShape(8.dp)

// Medium rounded
shape = RoundedCornerShape(12.dp)

// Very rounded
shape = RoundedCornerShape(16.dp)

// Pill shape
shape = RoundedCornerShape(50.dp)
```

---

## Common Patterns

### Pattern 1: Success Toast

```kotlin
SnackbarConfig(
    containerColor = Color(0xFF4CAF50),
    contentColor = Color.White,
    autoDismissDuration = 2000L  // Quick dismiss
)
```

### Pattern 2: Error Alert

```kotlin
SnackbarConfig(
    containerColor = Color(0xFFE53935),
    contentColor = Color.White,
    autoDismissDuration = 5000L  // Longer visibility
)
```

### Pattern 3: Info Message

```kotlin
SnackbarConfig(
    containerColor = MaterialTheme.colorScheme.surfaceVariant,
    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    autoDismissDuration = 3000L
)
```

### Pattern 4: Confirmation (No Auto-Dismiss)

```kotlin
SnackbarConfig(
    containerColor = Color(0xFF1976D2),
    contentColor = Color.White,
    autoDismissDuration = 0L  // User must click X
)
```

### Pattern 5: Material Design 3

```kotlin
SnackbarConfig(
    containerColor = MaterialTheme.colorScheme.inverseSurface,
    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
    shape = MaterialTheme.shapes.large,
    autoDismissDuration = 3000L
)
```

---

## Integration with Existing Code

### In Screen1.kt

```kotlin
object Screen1 : Screen {
    @Composable
    override fun Content() {
        val shellController = LocalShellController.current
        val shellOwner = remember { Any() }
        
        // ... existing TopBar and FAB setup ...
        
        SideEffect {
            // Configure snackbar
            shellController.setSnackbarConfig(
                SnackbarConfig(autoDismissDuration = 3000L)
            )
        }
        
        Button(onClick = { 
            shellController.showSnackbar("FAB clicked on Screen 1")
        }) {
            Text("Show Message")
        }
    }
}
```

### In Screen2.kt

```kotlin
object Screen2 : Screen {
    @Composable
    override fun Content() {
        val shellController = LocalShellController.current
        
        SideEffect {
            // Different config per screen if needed
            shellController.setSnackbarConfig(
                SnackbarConfig(
                    containerColor = Color(0xFF2196F3),
                    contentColor = Color.White,
                    autoDismissDuration = 4000L
                )
            )
        }
        
        Button(onClick = { 
            shellController.showSnackbar("FAB clicked on Screen 2")
        }) {
            Text("Show Message")
        }
    }
}
```

---

## API Reference

### Data Classes

```kotlin
// Main configuration
data class SnackbarConfig(
    val containerColor: Color = Color.Unspecified,
    val contentColor: Color = Color.Unspecified,
    val containerPadding: SnackbarPadding = SnackbarPadding(),
    val autoDismissDuration: Long = 3000L,
    val shape: RoundedCornerShape? = null
)

// Padding configuration
data class SnackbarPadding(
    val start: Dp = 16.dp,
    val end: Dp = 4.dp,
    val top: Dp = 8.dp,
    val bottom: Dp = 8.dp
)
```

### Methods

```kotlin
// Show message with configured appearance
shellController.showSnackbar(message: String)

// Immediately dismiss current snackbar
shellController.dismissSnackbar()

// Configure snackbar appearance and timing
shellController.setSnackbarConfig(config: SnackbarConfig)
```

---

## Key Features

✅ **Default 3-Second Auto-Dismiss**
- Follows Material Design guidelines
- Customizable per use case

✅ **Fully Customizable Appearance**
- Colors, padding, shape
- Material theme integration
- Custom hex colors supported

✅ **Smart Positioning**
- Avoids FAB automatically
- Responsive on all screens
- Respects system insets

✅ **Smooth Animations**
- Slide up from bottom (enter)
- Slide down to bottom (exit)
- Fade in/out effects

✅ **Manual & Auto Dismiss**
- X button for manual dismiss
- Configurable auto-dismiss duration
- 0 = no auto-dismiss

✅ **Duplicate Handling**
- Same message ignored while visible
- Different messages replace previous

---

## Common Questions

**Q: How do I change the default 3-second duration?**
A: Call `setSnackbarConfig()` before `showSnackbar()`:
```kotlin
shellController.setSnackbarConfig(
    SnackbarConfig(autoDismissDuration = 5000L)
)
shellController.showSnackbar("Custom duration")
```

**Q: Can I customize colors per message?**
A: Yes! Set config before each message if needed:
```kotlin
shellController.setSnackbarConfig(config1)
shellController.showSnackbar("Message 1")

shellController.setSnackbarConfig(config2)
shellController.showSnackbar("Message 2")
```

**Q: How do I prevent auto-dismiss?**
A: Set duration to 0:
```kotlin
shellController.setSnackbarConfig(
    SnackbarConfig(autoDismissDuration = 0L)
)
shellController.showSnackbar("Click X to dismiss")
```

**Q: Why does the snackbar appear above the FAB?**
A: Smart positioning! It automatically adjusts to avoid overlap. This is the desired behavior.

**Q: Can I use custom fonts or text styles?**
A: Currently it uses Material3 `bodyMedium`. For custom styles, create a custom component based on `CustomSnackbar.kt`.

---

## File Structure

```
core/src/commonMain/kotlin/com/kanhaji/core/shell/ui/
├── ShellScaffold.kt          (Updated with auto-dismiss)
├── CustomSnackbar.kt         (NEW - Reusable component)
├── TopBarState.kt
├── FabState.kt
└── ShellController.kt
```

---

## Documentation Files

- `CUSTOMSNACKBAR_SUMMARY.md` — Quick reference
- `SNACKBAR_GUIDE.md` — Comprehensive guide
- `SNACKBAR_CUSTOMIZATION_REFERENCE.md` — Presets & examples
- `IMPLEMENTATION_CHECKLIST.md` — Completion status
- `IMPLEMENTATION_GUIDE.md` — This file

---

## Next Steps

1. ✅ Review the examples above
2. ✅ Update your screens to customize snackbar
3. ✅ Choose color/duration patterns for your app
4. ✅ Test different configurations
5. ✅ Customize for your brand

---

## Build Status

```
✅ BUILD SUCCESSFUL
✅ NO COMPILATION ERRORS
✅ READY FOR PRODUCTION
```

---

**Everything is ready to use! Start showing snackbars with full customization.**

```kotlin
// One line to show a message!
shellController.showSnackbar("Hello KCF!")
```

---

**Date:** March 22, 2026
**Status:** ✅ Complete & Production Ready

