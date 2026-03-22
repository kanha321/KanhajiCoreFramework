package com.kanhaji.core.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

actual fun isDynamicColorSupported(): Boolean = false

@Composable
actual fun rememberPlatformDynamicColorScheme(isDarkTheme: Boolean): ColorScheme? = null
