package com.kanhaji.core.settings.domain

import com.kanhaji.core.theme.ThemeManager

class ThemeSettingsApplier {
    fun apply(settings: ThemeSettings) {
        ThemeManager.customColor.value = settings.customColor
        ThemeManager.isAmoled = settings.isAmoled
        ThemeManager.isDynamicColor = settings.isDynamicColor
        ThemeManager.setTheme(settings.themeType)
    }
}

