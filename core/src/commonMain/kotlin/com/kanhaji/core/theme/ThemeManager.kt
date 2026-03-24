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

    private val defaultThemeType = defaultThemeTypeForPlatform()

    var currentThemeType by mutableStateOf(defaultThemeType)
    var isDarkTheme by mutableStateOf(defaultThemeType == ThemeType.DARK)
    var isAmoled by mutableStateOf(false)
    var isDynamicColor by mutableStateOf(false)
    var customColor = mutableStateOf(Color(0xFF6750A4))

    fun setTheme(type: ThemeType) {
        val resolvedType = normalizeThemeTypeForPlatform(type)
        currentThemeType = resolvedType
        isDarkTheme = when (resolvedType) {
            ThemeType.LIGHT -> false
            ThemeType.DARK -> true
            ThemeType.SYSTEM -> false
        }
    }
}

