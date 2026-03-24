# Custom Snackbar Guide

## Overview

The KCF framework provides a fully customizable snackbar component with auto-dismiss capabilities. The snackbar is managed by `ShellController` and can be easily configured for appearance and behavior.

---

## Features

✅ **Auto-dismiss** — Automatically hides after a configurable duration (default: 3 seconds)
✅ **Customizable Colors** — Container and content colors
✅ **Customizable Padding** — Fine-grained control over spacing
✅ **Customizable Shape** — Rounded corners
✅ **Dismiss Button** — Close button (X icon) for manual dismissal
✅ **Adaptive Positioning** — Adjusts position when FAB is visible
✅ **Smooth Animations** — Slide up from bottom with fade effect

---

## Basic Usage

### Showing a Snackbar

From any screen, access the `ShellController` and call `showSnackbar()`:

```kotlin
object MyScreen : Screen {
    @Composable
    override fun Content() {
        val shellController = LocalShellController.current
        
        Button(onClick = { 
            shellController.showSnackbar("This is a message!")
        }) {
            Text("Show Snackbar")
        }
    }
}
```

---

## Customization

### 1. Default Configuration

The snackbar has built-in defaults:

```kotlin
SnackbarConfig(
    containerColor = Color.Unspecified,        // Uses Material inverse surface
    contentColor = Color.Unspecified,          // Uses Material inverse on surface
    containerPadding = SnackbarPadding(),      // Padding inside the snackbar
    autoDismissDuration = 3000L,               // Auto-dismiss after 3 seconds (in ms)
    shape = null                               // Uses Material3 large shape
)
```

### 2. Customize Auto-Dismiss Duration

Set a custom duration in milliseconds (0 to disable auto-dismiss):

```kotlin
val shellController = LocalShellController.current

// Custom 5-second duration
shellController.setSnackbarConfig(
    SnackbarConfig(autoDismissDuration = 5000L)
)
shellController.showSnackbar("5 seconds to dismiss...")

// Disable auto-dismiss (user must click X)
shellController.setSnackbarConfig(
    SnackbarConfig(autoDismissDuration = 0L)
)
shellController.showSnackbar("Click X to dismiss")
```

### 3. Customize Colors

Control container background and text color:

```kotlin
shellController.setSnackbarConfig(
    SnackbarConfig(
        containerColor = Color(0xFF2D2D2D),     // Dark background
        contentColor = Color.White,             // White text
        autoDismissDuration = 3000L
    )
)
shellController.showSnackbar("Custom colors!")
```

### 4. Customize Padding

Fine-grained control over spacing inside the snackbar:

```kotlin
shellController.setSnackbarConfig(
    SnackbarConfig(
        containerPadding = SnackbarPadding(
            start = 20.dp,      // Left padding
            end = 8.dp,         // Right padding (where X button is)
            top = 12.dp,        // Top padding
            bottom = 12.dp      // Bottom padding
        ),
        autoDismissDuration = 3000L
    )
)
shellController.showSnackbar("Custom padding!")
```

### 5. Customize Shape (Corner Radius)

```kotlin
shellController.setSnackbarConfig(
    SnackbarConfig(
        shape = RoundedCornerShape(16.dp),  // More rounded corners
        autoDismissDuration = 3000L
    )
)
shellController.showSnackbar("Rounded corners!")
```

### 6. Complete Example with All Options

```kotlin
object SettingsScreen : Screen {
    @Composable
    override fun Content() {
        val shellController = LocalShellController.current
        val shellOwner = remember { Any() }
        
        // Configure snackbar on screen load
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
                    autoDismissDuration = 4000L,  // 4 seconds
                    shape = RoundedCornerShape(12.dp)
                )
            )
        }
        
        Column {
            Button(onClick = { 
                shellController.showSnackbar("Settings saved successfully!")
            }) {
                Text("Save Settings")
            }
        }
    }
}
```

---

## API Reference

### SnackbarConfig

Data class for customizing snackbar appearance and behavior.

```kotlin
data class SnackbarConfig(
    val containerColor: Color = Color.Unspecified,
    val contentColor: Color = Color.Unspecified,
    val containerPadding: SnackbarPadding = SnackbarPadding(),
    val autoDismissDuration: Long = 3000L,  // milliseconds
    val shape: RoundedCornerShape? = null
)
```

**Parameters:**
- `containerColor` — Background color (default: Material inverse surface)
- `contentColor` — Text/icon color (default: Material inverse on surface)
- `containerPadding` — Internal spacing
- `autoDismissDuration` — Time before auto-dismiss in milliseconds (0 = disabled)
- `shape` — Border radius (default: Material3 large shape)

### SnackbarPadding

Data class for customizing snackbar internal spacing.

```kotlin
data class SnackbarPadding(
    val start: Dp = 16.dp,   // Left padding
    val end: Dp = 4.dp,      // Right padding
    val top: Dp = 8.dp,      // Top padding
    val bottom: Dp = 8.dp    // Bottom padding
)
```

### ShellController Methods

```kotlin
// Show a message with configured appearance
shellController.showSnackbar(message: String)

// Dismiss the current snackbar
shellController.dismissSnackbar()

// Configure snackbar appearance and auto-dismiss
shellController.setSnackbarConfig(config: SnackbarConfig)
```

---

## Important Notes

### Auto-Dismiss Behavior

- **Default:** 3000ms (3 seconds)
- **Disabled:** Set to 0 (user must click X to dismiss)
- **Custom:** Any positive value in milliseconds

### Smart Positioning

The snackbar automatically adjusts its position:
- **With FAB:** Positioned above FAB with 84.dp bottom padding
- **Without FAB:** Positioned near bottom with 16.dp bottom padding

### Duplicate Messages

If the same message is triggered multiple times, it is ignored while already visible (Option B behavior).

### Animations

- **Enter:** Slide up from bottom + fade in (250ms)
- **Exit:** Slide down to bottom + fade out (250ms)

---

## Best Practices

1. **Set config once per screen** in `SideEffect` to avoid repeated updates
2. **Use Material colors** for consistency with app theme
3. **Keep messages short** (2-3 words) for better readability
4. **Disable auto-dismiss** for critical messages that need user acknowledgment
5. **Use 3-4 second duration** for informational messages
6. **Use custom colors sparingly** to maintain UI consistency

---

## Examples by Use Case

### Success Message (Quick dismiss)
```kotlin
shellController.setSnackbarConfig(
    SnackbarConfig(
        containerColor = Color(0xFF4CAF50),  // Green
        contentColor = Color.White,
        autoDismissDuration = 2000L
    )
)
shellController.showSnackbar("✓ Saved successfully")
```

### Error Message (Longer visibility)
```kotlin
shellController.setSnackbarConfig(
    SnackbarConfig(
        containerColor = Color(0xFFE53935),  // Red
        contentColor = Color.White,
        autoDismissDuration = 5000L
    )
)
shellController.showSnackbar("✗ Failed to save")
```

### Info Message (No auto-dismiss)
```kotlin
shellController.setSnackbarConfig(
    SnackbarConfig(
        autoDismissDuration = 0L
    )
)
shellController.showSnackbar("Please confirm your action")
```

---

## Troubleshooting

### Snackbar appears below FAB
✅ This is expected behavior. The snackbar automatically positions itself above the FAB to avoid overlap. If you want to change the gap, modify the `snackbarBottomPadding` calculation in `ShellScaffold.kt`.

### Auto-dismiss not working
✅ Check that `autoDismissDuration > 0`. If set to 0, you must click X to dismiss.

### Colors not applying
✅ Make sure you're setting config **before** calling `showSnackbar()`. Use `SideEffect` on screen load to ensure it's set.

### Snackbar appears multiple times
✅ Duplicate messages while visible are ignored. If you see multiple snackbars, they have different messages or the previous one was dismissed.

---

## Complete Integration Example

```kotlin
object MyApp : Screen {
    @Composable
    override fun Content() {
        val shellController = LocalShellController.current
        
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Show default 3-second snackbar
            Button(onClick = { 
                shellController.showSnackbar("Standard message")
            }) {
                Text("Show Default")
            }
            
            // Show custom colored snackbar
            Button(onClick = { 
                shellController.setSnackbarConfig(
                    SnackbarConfig(
                        containerColor = Color(0xFF2196F3),
                        contentColor = Color.White,
                        autoDismissDuration = 4000L
                    )
                )
                shellController.showSnackbar("Custom styled message")
            }) {
                Text("Show Custom")
            }
            
            // Show no auto-dismiss snackbar
            Button(onClick = { 
                shellController.setSnackbarConfig(
                    SnackbarConfig(autoDismissDuration = 0L)
                )
                shellController.showSnackbar("Click X to dismiss")
            }) {
                Text("Show Persistent")
            }
        }
    }
}
```

---

**Status:** ✅ Complete & Ready to Use


