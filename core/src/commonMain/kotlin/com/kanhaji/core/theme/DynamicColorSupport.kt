package com.kanhaji.core.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

expect fun isDynamicColorSupported(): Boolean

expect fun isSystemThemeModeSupported(): Boolean

fun defaultThemeTypeForPlatform(): ThemeManager.ThemeType {
	return if (isSystemThemeModeSupported()) {
		ThemeManager.ThemeType.SYSTEM
	} else {
		ThemeManager.ThemeType.DARK
	}
}

fun normalizeThemeTypeForPlatform(themeType: ThemeManager.ThemeType): ThemeManager.ThemeType {
	return if (!isSystemThemeModeSupported() && themeType == ThemeManager.ThemeType.SYSTEM) {
		ThemeManager.ThemeType.DARK
	} else {
		themeType
	}
}

@Composable
expect fun rememberPlatformDynamicColorScheme(isDarkTheme: Boolean): ColorScheme?
