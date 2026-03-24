package com.kanhaji.core.settings.domain

import androidx.compose.ui.graphics.Color
import com.kanhaji.core.theme.ThemeManager
import com.kanhaji.core.theme.defaultThemeTypeForPlatform

data class ThemeSettings(
    val themeType: ThemeManager.ThemeType = defaultThemeTypeForPlatform(),
    val isAmoled: Boolean = false,
    val isDynamicColor: Boolean = false,
    val customColor: Color = Color(0xFF6750A4)
)

