# Snackbar Customization Reference

## Visual Structure

```
┌─────────────────────────────────────────────┐
│ ┌── containerPadding.start                  │
│ │  Text Message            [X] ← containerPadding.end
│ └── containerPadding.bottom                 │
└─────────────────────────────────────────────┘
     ↑
     └── containerColor (background)
     └── contentColor (text & icon)
     └── shape (corner radius)
     └── autoDismissDuration (3000ms default)
```

---

## SnackbarConfig Properties

### 1. containerColor

**Default:** `Color.Unspecified` (Material inverse surface)

```kotlin
// Use Material colors
containerColor = MaterialTheme.colorScheme.primary

// Or custom hex colors
containerColor = Color(0xFF2196F3)  // Blue
containerColor = Color(0xFF4CAF50)  // Green
containerColor = Color(0xFFE53935)  // Red
```

### 2. contentColor

**Default:** `Color.Unspecified` (Material inverse on surface)

```kotlin
// Match the container
contentColor = Color.White
contentColor = Color.Black

// Or use Material colors
contentColor = MaterialTheme.colorScheme.onPrimary
```

### 3. containerPadding

**Default:**
```kotlin
SnackbarPadding(
    start = 16.dp,
    end = 4.dp,
    top = 8.dp,
    bottom = 8.dp
)
```

**Customize individual sides:**
```kotlin
SnackbarPadding(
    start = 20.dp,   // More left space
    end = 8.dp,      // Space for X button
    top = 12.dp,     // More top space
    bottom = 12.dp   // More bottom space
)
```

### 4. autoDismissDuration

**Default:** `3000L` (3 seconds in milliseconds)

```kotlin
1000L   // 1 second
2000L   // 2 seconds
3000L   // 3 seconds (default)
4000L   // 4 seconds
5000L   // 5 seconds
0L      // No auto-dismiss (user must click X)
```

### 5. shape

**Default:** `null` (Material3 large shape)

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

## Quick Presets

### Preset 1: Default (Info)
```kotlin
SnackbarConfig(
    autoDismissDuration = 3000L
)
```

### Preset 2: Success Message
```kotlin
SnackbarConfig(
    containerColor = Color(0xFF4CAF50),
    contentColor = Color.White,
    autoDismissDuration = 2000L
)
```

### Preset 3: Error Message
```kotlin
SnackbarConfig(
    containerColor = Color(0xFFE53935),
    contentColor = Color.White,
    autoDismissDuration = 5000L
)
```

### Preset 4: Warning Message
```kotlin
SnackbarConfig(
    containerColor = Color(0xFFFFA726),
    contentColor = Color.White,
    autoDismissDuration = 4000L
)
```

### Preset 5: Confirmation (No auto-dismiss)
```kotlin
SnackbarConfig(
    containerColor = Color(0xFF1976D2),
    contentColor = Color.White,
    autoDismissDuration = 0L  // User must click X
)
```

### Preset 6: Dense (Compact padding)
```kotlin
SnackbarConfig(
    containerPadding = SnackbarPadding(
        start = 12.dp,
        end = 4.dp,
        top = 6.dp,
        bottom = 6.dp
    ),
    autoDismissDuration = 3000L
)
```

### Preset 7: Spacious (Large padding)
```kotlin
SnackbarConfig(
    containerPadding = SnackbarPadding(
        start = 24.dp,
        end = 8.dp,
        top = 16.dp,
        bottom = 16.dp
    ),
    autoDismissDuration = 3000L
)
```

### Preset 8: Rounded Corners
```kotlin
SnackbarConfig(
    shape = RoundedCornerShape(16.dp),
    autoDismissDuration = 3000L
)
```

### Preset 9: Minimal
```kotlin
SnackbarConfig(
    containerPadding = SnackbarPadding(
        start = 8.dp,
        end = 4.dp,
        top = 4.dp,
        bottom = 4.dp
    ),
    shape = RoundedCornerShape(4.dp),
    autoDismissDuration = 2000L
)
```

### Preset 10: Material Design 3
```kotlin
SnackbarConfig(
    containerColor = MaterialTheme.colorScheme.inverseSurface,
    contentColor = MaterialTheme.colorScheme.inverseOnSurface,
    shape = MaterialTheme.shapes.large,
    autoDismissDuration = 3000L
)
```

---

## Color Combinations

### Color Pairs (containerColor → contentColor)

```kotlin
// Primary variant
Color(0xFF2196F3) → Color.White       // Blue

// Success variant
Color(0xFF4CAF50) → Color.White       // Green

// Error variant
Color(0xFFE53935) → Color.White       // Red

// Warning variant
Color(0xFFFFA726) → Color.White       // Orange

// Info variant
Color(0xFF29B6F6) → Color.White       // Light Blue

// Dark variant
Color(0xFF212121) → Color.White       // Dark Gray

// Light variant
Color(0xFFF5F5F5) → Color.Black       // Light Gray
```

---

## Duration Presets (milliseconds)

```
Brief:      1000L  (1 second)
Short:      2000L  (2 seconds)
Normal:     3000L  (3 seconds) ← default
Long:       4000L  (4 seconds)
Longer:     5000L  (5 seconds)
Extended:   7000L  (7 seconds)
Persistent: 0L     (no auto-dismiss)
```

---

## Padding Reference (in dp)

```
Compact:   start=12, end=4, top=6, bottom=6
Default:   start=16, end=4, top=8, bottom=8
Spacious:  start=20, end=8, top=12, bottom=12
Generous:  start=24, end=8, top=16, bottom=16
```

---

## Shape Reference (corner radius in dp)

```
Square:        RoundedCornerShape(0.dp)
Slightly:      RoundedCornerShape(4.dp)
Subtle:        RoundedCornerShape(8.dp)
Medium:        RoundedCornerShape(12.dp)
Rounded:       RoundedCornerShape(16.dp)
VeryRounded:   RoundedCornerShape(24.dp)
Pill:          RoundedCornerShape(50.dp)
```

---

## Complete Configuration Examples

### 1. Minimal Dark
```kotlin
val config = SnackbarConfig(
    containerColor = Color(0xFF212121),
    contentColor = Color.White,
    containerPadding = SnackbarPadding(
        start = 12.dp,
        end = 4.dp,
        top = 4.dp,
        bottom = 4.dp
    ),
    autoDismissDuration = 2000L,
    shape = RoundedCornerShape(4.dp)
)
```

### 2. Material Design 3 Primary
```kotlin
val config = SnackbarConfig(
    containerColor = MaterialTheme.colorScheme.primary,
    contentColor = MaterialTheme.colorScheme.onPrimary,
    containerPadding = SnackbarPadding(
        start = 16.dp,
        end = 8.dp,
        top = 12.dp,
        bottom = 12.dp
    ),
    autoDismissDuration = 3000L,
    shape = MaterialTheme.shapes.medium
)
```

### 3. Custom Brand Colors
```kotlin
val config = SnackbarConfig(
    containerColor = Color(0xFF1E88E5),  // Brand blue
    contentColor = Color(0xFFFFFFFF),    // White
    containerPadding = SnackbarPadding(
        start = 18.dp,
        end = 8.dp,
        top = 14.dp,
        bottom = 14.dp
    ),
    autoDismissDuration = 4000L,
    shape = RoundedCornerShape(12.dp)
)
```

### 4. Success Toast
```kotlin
val config = SnackbarConfig(
    containerColor = Color(0xFF66BB6A),  // Light green
    contentColor = Color.White,
    containerPadding = SnackbarPadding(
        start = 16.dp,
        end = 8.dp,
        top = 10.dp,
        bottom = 10.dp
    ),
    autoDismissDuration = 2000L,        // Quick dismiss
    shape = RoundedCornerShape(8.dp)
)
```

### 5. Error Alert
```kotlin
val config = SnackbarConfig(
    containerColor = Color(0xFFF44336),  // Red
    contentColor = Color.White,
    containerPadding = SnackbarPadding(
        start = 16.dp,
        end = 8.dp,
        top = 12.dp,
        bottom = 12.dp
    ),
    autoDismissDuration = 5000L,        // Longer visibility
    shape = RoundedCornerShape(10.dp)
)
```

---

## How to Use These Presets

```kotlin
object MyScreen : Screen {
    @Composable
    override fun Content() {
        val shellController = LocalShellController.current
        
        SideEffect {
            // Choose a preset
            shellController.setSnackbarConfig(
                SnackbarConfig(
                    containerColor = Color(0xFF4CAF50),  // Success green
                    contentColor = Color.White,
                    autoDismissDuration = 2000L
                )
            )
        }
        
        Button(onClick = { 
            shellController.showSnackbar("Operation successful!")
        }) {
            Text("Show Success")
        }
    }
}
```

---

## Responsive Design Notes

The snackbar automatically adapts to:
- **With FAB:** Positioned above with 84.dp bottom padding
- **Without FAB:** Positioned at bottom with 16.dp bottom padding
- **Rotation:** Repositions on device rotation
- **Keyboard:** Adjusts for on-screen keyboard

No additional customization needed for responsive behavior!

---

## Performance Tips

1. **Set once, reuse many:** Set config in `SideEffect`, then call `showSnackbar()` multiple times
2. **Avoid frequent resets:** Changing config repeatedly causes recomposition
3. **Use Material colors:** They're pre-computed and more efficient
4. **Keep animations:** (250ms default) is optimal for smooth UX

---

**Last Updated:** March 22, 2026

