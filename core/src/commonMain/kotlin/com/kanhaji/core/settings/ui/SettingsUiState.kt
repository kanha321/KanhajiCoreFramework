package com.kanhaji.core.settings.ui

import androidx.compose.ui.graphics.Color
import com.kanhaji.core.settings.domain.ThemeSettings
import com.kanhaji.core.theme.ThemeManager

data class ThemeSettingsUiState(
    val themeType: ThemeManager.ThemeType = ThemeManager.ThemeType.SYSTEM,
    val isDarkTheme: Boolean = false,
    val isAmoled: Boolean = false,
    val isDynamicColor: Boolean = false,
    val customColor: Color = Color(0xFF6750A4)
) {
    val themeLabel: String
        get() = themeType.name.lowercase().replaceFirstChar { it.uppercase() }
}

data class SettingsUiState(
    val showColorPicker: Boolean = false,
    val showThemeDialog: Boolean = false,
    val theme: ThemeSettingsUiState = ThemeSettingsUiState()
)

fun ThemeSettings.toUiState(): ThemeSettingsUiState {
    val resolvedDarkTheme = when (themeType) {
        ThemeManager.ThemeType.DARK -> true
        ThemeManager.ThemeType.LIGHT -> false
        ThemeManager.ThemeType.SYSTEM -> ThemeManager.isDarkTheme
    }

    return ThemeSettingsUiState(
        themeType = themeType,
        isDarkTheme = resolvedDarkTheme,
        isAmoled = isAmoled,
        isDynamicColor = isDynamicColor,
        customColor = customColor
    )
}
