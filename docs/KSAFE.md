# KSafe Wrapper API Reference

## Overview

KCF provides a type-safe wrapper around **KSafe** — a Kotlin Multiplatform (KMP) encrypted preferences library. The wrapper abstracts platform-specific initialization and provides a unified API across Android, iOS, and Desktop (JVM) targets.

**Key Features:**
- ✅ Type-safe read/write operations
- ✅ No coroutines required (synchronous)
- ✅ Two-tier storage: normal (`prefs`) + encrypted (`secure`)
- ✅ Platform-agnostic API
- ✅ Automatic persistence across app restarts

---

## Architecture

### Components

```
commonMain/
├── KSafeProvider (singleton)
├── getDirect() (expect functions)
└── putDirect() (expect functions)

androidMain/ iosMain/ jvmMain/
├── KSafeWrapper (actual typealias)
├── getDirect() (actual implementations)
├── putDirect() (actual implementations)
└── initKSafe() (platform init)
```

### Storage Tiers

| Tier | Use Case | Encryption | Location |
|------|----------|-----------|----------|
| `prefs` | General app settings (theme, language, etc.) | ❌ Plain text | App internal cache |
| `secure` | Sensitive data (tokens, passwords, API keys) | ✅ Encrypted | App internal cache |

---

## Initialization

### Before Using Any Storage Function

KSafe must be initialized **before any composable renders**. Call the platform-specific `initKSafe()` function at app startup.

#### Android

```kotlin
import android.app.Activity
import android.os.Bundle
import com.kanhaji.core.storage.initKSafe

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initKSafe(context = this)  // ← Initialize here, before setContent()
        setContent { App() }
    }
}
```

**Permissions:**
No special permissions required. KSafe uses app-internal storage automatically.

#### iOS

```kotlin
import com.kanhaji.core.storage.initKSafe

@main
struct iOSApp: App {
    init() {
        initKSafe()  // ← Initialize at app launch
    }
    
    var body: some Scene { ... }
}
```

**File Location:**
- macOS: `~/Library/Application Support/`
- iOS: App Documents folder

#### Desktop (JVM)

```kotlin
import com.kanhaji.core.storage.initKSafe

fun main() {
    initKSafe()  // ← Initialize before creating UI
    application {
        Window(onCloseRequest = ::exitApplication) {
            App()
        }
    }
}
```

**File Location:**
- Linux/macOS: `~/.eu_anifantakis_ksafe/`
- Windows: `%APPDATA%\eu_anifantakis_ksafe\`

---

## API Reference

### KSafeProvider Singleton

Access the two storage instances:

```kotlin
import com.kanhaji.core.storage.KSafeProvider

// General settings
val prefs = KSafeProvider.prefs

// Sensitive data
val secure = KSafeProvider.secure
```

**Error Handling:**
If `initKSafe()` hasn't been called:
```
IllegalStateException: KSafeProvider not initialized. Call initKSafe() at app entry point.
```

---

### getDirect() — Read Operations

**Signature:**
```kotlin
fun getDirect(ksafe: KSafeWrapper, key: String, defaultValue: T): T
```

Retrieves a value from storage. Returns `defaultValue` if key doesn't exist.

#### Get Integer

```kotlin
import com.kanhaji.core.storage.getDirect
import com.kanhaji.core.storage.KSafeProvider

val maxRetries = getDirect(
    ksafe = KSafeProvider.prefs,
    key = "max_retries",
    defaultValue = 3
)
// Returns: Int
```

#### Get Boolean

```kotlin
val isDarkMode = getDirect(
    ksafe = KSafeProvider.prefs,
    key = "is_dark_mode",
    defaultValue = false
)
// Returns: Boolean
```

#### Get String

```kotlin
val apiUrl = getDirect(
    ksafe = KSafeProvider.prefs,
    key = "api_url",
    defaultValue = "https://api.example.com"
)
// Returns: String
```

#### Get from Encrypted Storage

```kotlin
val authToken = getDirect(
    ksafe = KSafeProvider.secure,
    key = "auth_token",
    defaultValue = ""
)
// Returns: String (encrypted in storage, decrypted on read)
```

---

### putDirect() — Write Operations

**Signature:**
```kotlin
fun putDirect(ksafe: KSafeWrapper, key: String, value: T)
```

Saves a value to storage synchronously. Overwrites existing value if key already exists.

#### Save Integer

```kotlin
import com.kanhaji.core.storage.putDirect
import com.kanhaji.core.storage.KSafeProvider

putDirect(
    ksafe = KSafeProvider.prefs,
    key = "max_retries",
    value = 5
)
```

#### Save Boolean

```kotlin
putDirect(
    ksafe = KSafeProvider.prefs,
    key = "is_dark_mode",
    value = true
)
```

#### Save String

```kotlin
putDirect(
    ksafe = KSafeProvider.prefs,
    key = "api_url",
    value = "https://new-api.example.com"
)
```

#### Save to Encrypted Storage

```kotlin
putDirect(
    ksafe = KSafeProvider.secure,
    key = "auth_token",
    value = "eyJhbGc..."  // JWT token
)
// Automatically encrypted before storage
```

---

## Common Patterns

### Pattern 1: Load Settings at App Startup

```kotlin
import com.kanhaji.core.storage.getDirect
import com.kanhaji.core.storage.KSafeProvider
import com.kanhaji.core.theme.ThemeManager

fun loadAppSettings() {
    val isDark = getDirect(KSafeProvider.prefs, "is_dark_mode", false)
    val isAmoled = getDirect(KSafeProvider.prefs, "is_amoled", false)
    val themeType = getDirect(KSafeProvider.prefs, "theme_type", "SYSTEM")
    
    ThemeManager.isDarkTheme = isDark
    ThemeManager.isAmoled = isAmoled
    ThemeManager.setTheme(ThemeManager.ThemeType.valueOf(themeType))
}
```

### Pattern 2: Save User Preference on Change

```kotlin
import com.kanhaji.core.storage.putDirect
import com.kanhaji.core.storage.KSafeProvider
import androidx.compose.material3.Switch
import androidx.compose.runtime.mutableStateOf

@Composable
fun DarkModeToggle() {
    var isDark by mutableStateOf(false)
    
    Switch(
        checked = isDark,
        onCheckedChange = { newValue ->
            isDark = newValue
            // Persist immediately
            putDirect(KSafeProvider.prefs, "is_dark_mode", newValue)
        }
    )
}
```

### Pattern 3: Use Default for First Launch

```kotlin
import com.kanhaji.core.storage.getDirect
import com.kanhaji.core.storage.KSafeProvider

fun getAppVersion(): String {
    // First launch returns "1.0.0", subsequent launches return saved value
    return getDirect(
        ksafe = KSafeProvider.prefs,
        key = "app_version",
        defaultValue = "1.0.0"
    )
}
```

### Pattern 4: Store Complex Data as String

Since KSafe only supports `Int`, `Boolean`, and `String`, serialize complex data:

```kotlin
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

data class UserPrefs(val theme: String, val language: String)

// Save
val userPrefs = UserPrefs(theme = "dark", language = "en")
val jsonString = Json.encodeToString(userPrefs)
putDirect(KSafeProvider.prefs, "user_prefs", jsonString)

// Load
val saved = getDirect(KSafeProvider.prefs, "user_prefs", "{}")
val userPrefs = Json.decodeFromString<UserPrefs>(saved)
```

### Pattern 5: Conditional Secure Storage

```kotlin
import com.kanhaji.core.storage.getDirect
import com.kanhaji.core.storage.putDirect
import com.kanhaji.core.storage.KSafeProvider

fun saveToken(token: String, isSecure: Boolean) {
    val storage = if (isSecure) KSafeProvider.secure else KSafeProvider.prefs
    putDirect(storage, "auth_token", token)
}

fun getToken(isSecure: Boolean): String {
    val storage = if (isSecure) KSafeProvider.secure else KSafeProvider.prefs
    return getDirect(storage, "auth_token", "")
}
```

---

## Key Points

### Synchronous Operations

All `getDirect()` and `putDirect()` calls are **blocking and synchronous**. No coroutines needed:

```kotlin
// ✅ Direct usage in Composables
@Composable
fun MyScreen() {
    val theme = getDirect(KSafeProvider.prefs, "theme", "light")
    // No launch(IO) required
    Text(theme)
}
```

### Type Safety

The API is overloaded by return type. Compiler ensures type safety:

```kotlin
// ✅ Correct — explicitly typed default
val count: Int = getDirect(prefs, "count", 0)

// ✅ Correct — Boolean default
val enabled: Boolean = getDirect(prefs, "enabled", false)

// ✅ Correct — String default
val name: String = getDirect(prefs, "name", "")
```

### Immutability

`getDirect()` returns immutable values. To update, call `putDirect()` again:

```kotlin
val oldValue = getDirect(prefs, "key", 0)
val newValue = oldValue + 1
putDirect(prefs, "key", newValue)
```

### Persistence

All values are automatically persisted to device storage:

```kotlin
putDirect(prefs, "score", 100)
// App closes
// App reopens
val score = getDirect(prefs, "score", 0)  // Returns: 100
```

### Encryption

Values stored in `KSafeProvider.secure` are automatically encrypted:

```kotlin
// ✅ Stored encrypted
putDirect(KSafeProvider.secure, "password", "secret123")

// ✅ Automatically decrypted on read
val password = getDirect(KSafeProvider.secure, "password", "")
```

---

## Best Practices

### 1. Use Constants for Keys

Avoid hardcoded strings scattered throughout code:

```kotlin
// ✅ Good
object StorageKeys {
    const val THEME_TYPE = "theme_type"
    const val IS_AMOLED = "is_amoled"
    const val API_TOKEN = "api_token"
}

putDirect(prefs, StorageKeys.THEME_TYPE, "DARK")
```

### 2. Choose Right Storage Tier

```kotlin
// ✅ Use prefs for non-sensitive
putDirect(KSafeProvider.prefs, "language", "en")

// ✅ Use secure for sensitive
putDirect(KSafeProvider.secure, "password", "***")
```

### 3. Provide Meaningful Defaults

```kotlin
// ✅ Good — clear intent
val isFirstLaunch = getDirect(prefs, "first_launch", true)

// ❌ Poor — unclear what empty string means
val apiKey = getDirect(prefs, "api_key", "")
```

### 4. Handle Large Data

For complex objects, use Kotlin Serialization + String storage:

```kotlin
// ✅ Good — serialized to JSON string
val json = Json.encodeToString(myObject)
putDirect(prefs, "object", json)
```

### 5. Initialize Early

```kotlin
// ✅ Good
fun main() {
    initKSafe()
    application { Window { App() } }
}

// ❌ Bad — too late
fun main() {
    application { 
        Window { 
            initKSafe()  // After rendering starts
            App() 
        }
    }
}
```

---

## Troubleshooting

### Issue: "KSafeProvider not initialized"

**Symptom:**
```
IllegalStateException: KSafeProvider not initialized. Call initKSafe() at app entry point.
```

**Solution:**
Call `initKSafe()` **before** any UI renders:

```kotlin
// Android
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initKSafe(context = this)  // ← Before setContent()
    setContent { App() }
}

// iOS
@main
struct App: ios.App {
    init() {
        initKSafe()  // ← At launch
    }
}

// JVM
fun main() {
    initKSafe()  // ← Before UI
    application { ... }
}
```

### Issue: Data Not Persisting

**Symptom:** Values reset after app restart

**Solution:** Verify:
1. `initKSafe()` was called
2. `putDirect()` succeeded (no exceptions)
3. Device storage permissions granted (Android)
4. Storage directory writable (iOS, JVM)

```kotlin
// Verify write succeeded
putDirect(prefs, "test_key", "test_value")
val readBack = getDirect(prefs, "test_key", "NOT_FOUND")
if (readBack == "test_value") {
    Log.d("Storage", "✓ Persistence working")
} else {
    Log.e("Storage", "✗ Persistence failed")
}
```

### Issue: Secure Storage Errors

**Symptom:** `putDirect(secure, ...)` throws exception

**Solution:**
- **Android:** No special permissions needed (uses app-internal storage)
- **iOS:** Verify entitlements allow keychain access
- **JVM:** Check directory permissions on `~/.eu_anifantakis_ksafe/`

---

## Type Support

KSafe wrapper supports **three types only**:

| Type | Example | Works |
|------|---------|-------|
| `Int` | `42` | ✅ Yes |
| `Boolean` | `true`, `false` | ✅ Yes |
| `String` | `"hello"` | ✅ Yes |
| `Long` | `100L` | ❌ Serialize as String |
| `Float` | `3.14f` | ❌ Serialize as String |
| `Double` | `3.14` | ❌ Serialize as String |
| `Object` | Custom class | ❌ Serialize as JSON String |

**Serializing Unsupported Types:**

```kotlin
// Float
val floatValue = 3.14f
putDirect(prefs, "pi", floatValue.toString())
val restored = getDirect(prefs, "pi", "0.0").toFloat()

// Long
val longValue = 1000000000000L
putDirect(prefs, "big_number", longValue.toString())
val restored = getDirect(prefs, "big_number", "0").toLong()

// Custom Object
val json = Json.encodeToString(myObject)
putDirect(prefs, "object", json)
val restored = Json.decodeFromString<MyObject>(
    getDirect(prefs, "object", "{}")
)
```

---

## Performance

| Operation | Time | Notes |
|-----------|------|-------|
| `getDirect()` | ~1ms | Fast (no I/O after cache) |
| `putDirect()` | ~5-10ms | Synchronous write to disk |
| Encryption overhead | <1ms | Negligible for strings |

**Optimization Tips:**
- ✅ Cache frequently accessed values in composable state
- ✅ Batch multiple writes when possible
- ✅ Use defaults to avoid unnecessary disk reads

```kotlin
// ✅ Good — cache in state
@Composable
fun MyScreen() {
    val theme = remember { 
        getDirect(prefs, "theme", "light") 
    }
    // theme not re-read on recomposition
}

// ❌ Bad — reads on every recomposition
@Composable
fun MyScreen() {
    val theme = getDirect(prefs, "theme", "light")
    // Re-read on every recomposition
}
```

---

## Migration Guide

### From SharedPreferences (Android only)

```kotlin
// Old (SharedPreferences)
val pref = context.getSharedPreferences("app", Context.MODE_PRIVATE)
pref.edit { putInt("count", 5) }
val count = pref.getInt("count", 0)

// New (KSafe)
initKSafe(context)
putDirect(KSafeProvider.prefs, "count", 5)
val count = getDirect(KSafeProvider.prefs, "count", 0)
```

### From DataStore (KMP)

```kotlin
// Old (DataStore — async)
context.dataStore.edit { prefs ->
    prefs[intPreferencesKey("count")] = 5
}

// New (KSafe — sync)
putDirect(KSafeProvider.prefs, "count", 5)
```

---

## Summary Table

| Task | Function | Example |
|------|----------|---------|
| Initialize | `initKSafe()` | `initKSafe(context)` |
| Read int | `getDirect()` | `getDirect(prefs, "count", 0)` |
| Read bool | `getDirect()` | `getDirect(prefs, "enabled", false)` |
| Read string | `getDirect()` | `getDirect(prefs, "name", "")` |
| Write int | `putDirect()` | `putDirect(prefs, "count", 5)` |
| Write bool | `putDirect()` | `putDirect(prefs, "enabled", true)` |
| Write string | `putDirect()` | `putDirect(prefs, "name", "John")` |
| Secure read | `getDirect()` | `getDirect(secure, "token", "")` |
| Secure write | `putDirect()` | `putDirect(secure, "token", "***")` |

---

## Related Docs

- [QUICKSTART.md](./QUICKSTART.md) — 5-minute setup guide
- [GUIDE.md](./GUIDE.md) — Complete integration guide
- [CHEATSHEET.md](./CHEATSHEET.md) — API quick reference
- [KSafe GitHub](https://github.com/ioannisa/KSafe) — Official KSafe documentation

---

**Last updated:** March 24, 2026  
**KCF Version:** 1.0.0




