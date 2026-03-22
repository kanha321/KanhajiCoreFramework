package com.kanhaji.core.settings.data

import androidx.compose.ui.graphics.Color
import com.kanhaji.core.settings.domain.ThemeSettings
import com.kanhaji.core.theme.ThemeManager

class ThemeSettingsRepository(
    private val localDataSource: ThemeSettingsLocalDataSource = ThemeSettingsLocalDataSource()
) {
    fun load(): ThemeSettings = localDataSource.readThemeSettings()

    fun saveThemeType(themeType: ThemeManager.ThemeType) {
        localDataSource.writeThemeType(themeType)
    }

    fun saveAmoled(enabled: Boolean) {
        localDataSource.writeAmoled(enabled)
    }

    fun saveDynamicColor(enabled: Boolean) {
        localDataSource.writeDynamicColor(enabled)
    }

    fun saveCustomColor(color: Color) {
        localDataSource.writeCustomColor(color)
    }
}

