package com.kanhaji.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.materialkolor.rememberDynamicColorScheme

@Composable
fun KCF(
    seedColor: Color = ThemeManager.customColor.value,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val resolvedDarkTheme = when (ThemeManager.currentThemeType) {
        ThemeManager.ThemeType.LIGHT -> false
        ThemeManager.ThemeType.DARK -> true
        ThemeManager.ThemeType.SYSTEM -> darkTheme
    }

    ThemeManager.isDarkTheme = resolvedDarkTheme

    val baseColorScheme = rememberPlatformDynamicColorScheme(isDarkTheme = resolvedDarkTheme)
        ?: rememberDynamicColorScheme(
            seedColor = seedColor,
            isDark = resolvedDarkTheme
        )

    val finalColorScheme = if (ThemeManager.isAmoled && resolvedDarkTheme) {
        baseColorScheme.copy(
            background = Color.Black,
            surface = Color.Black
        )
    } else {
        baseColorScheme
    }

    MaterialTheme(
        colorScheme = finalColorScheme,
        content = content
    )
}
