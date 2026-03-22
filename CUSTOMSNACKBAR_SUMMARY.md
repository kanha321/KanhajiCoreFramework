# CustomSnackbar Implementation Summary

## What's New ✨

You now have a **fully customizable snackbar system** with:

1. ✅ **Auto-dismiss after 3 seconds** (customizable)
2. ✅ **Separate CustomSnackbar component** (reusable, clean)
3. ✅ **Customizable everything:**
   - Container background color
   - Content text/icon color
   - Padding (start, end, top, bottom)
   - Shape (corner radius)
   - Auto-dismiss duration

---

## Files Changed/Created

### New Files

1. **`core/src/commonMain/kotlin/com/kanhaji/core/shell/ui/CustomSnackbar.kt`**
   - Reusable `CustomSnackbar` composable
   - `SnackbarConfig` data class with all customization options
   - `SnackbarPadding` data class for spacing control

### Modified Files

1. **`core/src/commonMain/kotlin/com/kanhaji/core/shell/ui/ShellScaffold.kt`**
   - Replaced inline `ShellSnackbar` with `CustomSnackbar` import
   - Added `snackbarConfig` field to `ShellController`
   - Added `setSnackbarConfig()` method to `ShellController`
   - Added `LaunchedEffect` for auto-dismiss behavior
   - Removed unused import

---

## Quick Start Examples

### Default (3-second auto-dismiss)
```kotlin
val shellController = LocalShellController.current
shellController.showSnackbar("Hello World!")
```

### Custom Duration (5 seconds)
```kotlin
shellController.setSnackbarConfig(
    SnackbarConfig(autoDismissDuration = 5000L)
)
shellController.showSnackbar("5 seconds to dismiss...")
```

### Custom Colors
```kotlin
shellController.setSnackbarConfig(
    SnackbarConfig(
        containerColor = Color(0xFF2196F3),
        contentColor = Color.White,
        autoDismissDuration = 3000L
    )
)
shellController.showSnackbar("Blue snackbar!")
```

### Custom Padding
```kotlin
shellController.setSnackbarConfig(
    SnackbarConfig(
        containerPadding = SnackbarPadding(
            start = 20.dp,
            end = 8.dp,
            top = 12.dp,
            bottom = 12.dp
        ),
        autoDismissDuration = 3000L
    )
)
shellController.showSnackbar("Custom padding!")
```

### Complete Example
```kotlin
object MyScreen : Screen {
    @Composable
    override fun Content() {
        val shellController = LocalShellController.current
        
        SideEffect {
            shellController.setSnackbarConfig(
                SnackbarConfig(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerPadding = SnackbarPadding(
                        start = 16.dp,
                        end = 8.dp,
                        top = 10.dp,
                        bottom = 10.dp
                    ),
                    autoDismissDuration = 4000L,
                    shape = RoundedCornerShape(12.dp)
                )
            )
        }
        
        Column {
            Button(onClick = { 
                shellController.showSnackbar("Fully customized!")
            }) {
                Text("Show")
            }
        }
    }
}
```

---

## API Reference

### SnackbarConfig
```kotlin
data class SnackbarConfig(
    val containerColor: Color = Color.Unspecified,
    val contentColor: Color = Color.Unspecified,
    val containerPadding: SnackbarPadding = SnackbarPadding(),
    val autoDismissDuration: Long = 3000L,  // milliseconds
    val shape: RoundedCornerShape? = null
)
```

### SnackbarPadding
```kotlin
data class SnackbarPadding(
    val start: Dp = 16.dp,
    val end: Dp = 4.dp,
    val top: Dp = 8.dp,
    val bottom: Dp = 8.dp
)
```

### ShellController Methods
```kotlin
// Show a message
shellController.showSnackbar(message: String)

// Configure appearance and auto-dismiss
shellController.setSnackbarConfig(config: SnackbarConfig)

// Dismiss immediately
shellController.dismissSnackbar()
```

---

## Key Features Explained

### 1. Auto-Dismiss (Default: 3 seconds)

```kotlin
// 3 seconds (default)
SnackbarConfig(autoDismissDuration = 3000L)

// 5 seconds
SnackbarConfig(autoDismissDuration = 5000L)

// No auto-dismiss (user must click X)
SnackbarConfig(autoDismissDuration = 0L)
```

### 2. Customizable Colors

Use Material colors for consistency:
```kotlin
SnackbarConfig(
    containerColor = MaterialTheme.colorScheme.primary,
    contentColor = MaterialTheme.colorScheme.onPrimary
)
```

Or custom colors for specific use cases:
```kotlin
SnackbarConfig(
    containerColor = Color(0xFFE53935),  // Red for errors
    contentColor = Color.White
)
```

### 3. Customizable Padding

Control spacing inside the snackbar:
```kotlin
SnackbarConfig(
    containerPadding = SnackbarPadding(
        start = 20.dp,   // Left space
        end = 8.dp,      // Right space (where X is)
        top = 12.dp,     // Top space
        bottom = 12.dp   // Bottom space
    )
)
```

### 4. Customizable Shape

Control corner radius:
```kotlin
SnackbarConfig(
    shape = RoundedCornerShape(8.dp)   // Less rounded
)

SnackbarConfig(
    shape = RoundedCornerShape(16.dp)  // More rounded
)
```

---

## Implementation Details

### What Happens When You Call `showSnackbar()`

1. If a previous snackbar exists, it's dismissed (with 120ms delay for animation)
2. Message is stored in `ShellController.snackbarMessage`
3. `CustomSnackbar` composable reads this state and animates in
4. `LaunchedEffect` starts a timer based on `autoDismissDuration`
5. After duration expires, snackbar is automatically dismissed
6. User can also click X button to dismiss manually

### Smart Positioning

```kotlin
if (hasFab) {
    // Position above FAB
    snackbarBottomPadding = navigationBarsBottomPadding + 84.dp
} else {
    // Position at bottom
    snackbarBottomPadding = navigationBarsBottomPadding + 16.dp
}
```

### Animations

- **Enter:** Slide up from bottom (250ms) + Fade in
- **Exit:** Slide down to bottom (250ms) + Fade out

---

## Usage Best Practices

✅ **DO:**
- Set config once per screen in `SideEffect`
- Use Material colors for consistency
- Keep messages short (2-3 words)
- Use 3-4 second duration for info
- Use 5+ seconds for errors
- Use 0 for critical messages needing acknowledgment

❌ **DON'T:**
- Change config frequently (causes recomposition)
- Use overly long messages
- Use conflicting colors
- Set duration < 1000ms unless necessary

---

## Troubleshooting

**Q: Snackbar appears below FAB instead of above?**
A: This is expected. It automatically adapts to FAB position. The snackbar padding is calculated to avoid overlap.

**Q: Auto-dismiss not working?**
A: Make sure `autoDismissDuration > 0`. If set to 0, user must click X.

**Q: Colors not showing?**
A: Set config **before** showing snackbar. Use `SideEffect` on screen load.

**Q: Snackbar shows multiple times?**
A: Duplicate messages are ignored while visible. If you see multiple, they're different messages.

---

## File Structure

```
core/src/commonMain/kotlin/com/kanhaji/core/shell/ui/
├── ShellScaffold.kt          # Main shell container
├── CustomSnackbar.kt         # ✨ NEW: Reusable snackbar
├── TopBarState.kt
├── FabState.kt
└── ... other files
```

---

## Customization Examples

### Success Message
```kotlin
SnackbarConfig(
    containerColor = Color(0xFF4CAF50),
    contentColor = Color.White,
    autoDismissDuration = 2000L
)
```

### Error Message
```kotlin
SnackbarConfig(
    containerColor = Color(0xFFE53935),
    contentColor = Color.White,
    autoDismissDuration = 5000L
)
```

### Info Message
```kotlin
SnackbarConfig(
    containerColor = MaterialTheme.colorScheme.surfaceVariant,
    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    autoDismissDuration = 3000L
)
```

### No Auto-Dismiss
```kotlin
SnackbarConfig(
    containerColor = MaterialTheme.colorScheme.errorContainer,
    contentColor = MaterialTheme.colorScheme.onErrorContainer,
    autoDismissDuration = 0L  // Must click X
)
```

---

## Build Status

✅ **BUILD SUCCESSFUL** (No errors or critical warnings)

---

## Next Steps

1. Read `SNACKBAR_GUIDE.md` for comprehensive documentation
2. Try the examples in your screens
3. Customize colors and padding to match your brand
4. Use different configs for different types of messages

---

**Date:** March 22, 2026
**Status:** ✅ Complete & Ready to Use

