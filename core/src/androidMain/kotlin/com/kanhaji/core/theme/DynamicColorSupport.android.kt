package com.kanhaji.core.theme

import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

actual fun isDynamicColorSupported(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
}

actual fun isSystemThemeModeSupported(): Boolean = true

@Composable
actual fun rememberPlatformDynamicColorScheme(isDarkTheme: Boolean): ColorScheme? {
    if (!ThemeManager.isDynamicColor || !isDynamicColorSupported()) {
        return null
    }

    val context = LocalContext.current
    return if (isDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
}
