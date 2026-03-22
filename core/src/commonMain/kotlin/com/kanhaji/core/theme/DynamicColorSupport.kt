package com.kanhaji.core.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

expect fun isDynamicColorSupported(): Boolean

@Composable
expect fun rememberPlatformDynamicColorScheme(isDarkTheme: Boolean): ColorScheme?
