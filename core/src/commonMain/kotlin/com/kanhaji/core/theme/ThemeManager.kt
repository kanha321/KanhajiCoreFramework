package com.kanhaji.core.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

object ThemeManager {
    enum class ThemeType {
        LIGHT,
        DARK,
        SYSTEM
    }

    var currentThemeType by mutableStateOf(ThemeType.SYSTEM)
    var isDarkTheme by mutableStateOf(false)
    var isAmoled by mutableStateOf(false)
    var isDynamicColor by mutableStateOf(false)
    var customColor = mutableStateOf(Color(0xFF6750A4))

    fun setTheme(type: ThemeType) {
        currentThemeType = type
        isDarkTheme = when (type) {
            ThemeType.LIGHT -> false
            ThemeType.DARK -> true
            ThemeType.SYSTEM -> false
        }
    }
}

