package com.kanhaji.core.settings.data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.kanhaji.core.settings.domain.ThemeSettings
import com.kanhaji.core.storage.KSafeProvider
import com.kanhaji.core.storage.getDirect
import com.kanhaji.core.storage.putDirect
import com.kanhaji.core.theme.ThemeManager
import com.kanhaji.core.theme.defaultThemeTypeForPlatform
import com.kanhaji.core.theme.normalizeThemeTypeForPlatform

class ThemeSettingsLocalDataSource {
    fun readThemeSettings(): ThemeSettings {
        val prefs = KSafeProvider.prefs
        val savedColor = getDirect(prefs, Keys.CUSTOM_COLOR, Color(0xFF6750A4).toArgb())
        val savedAmoled = getDirect(prefs, Keys.IS_AMOLED, false)
        val savedDynamic = getDirect(prefs, Keys.IS_DYNAMIC_COLOR, false)
        val defaultThemeType = defaultThemeTypeForPlatform()
        val savedThemeType = getDirect(prefs, Keys.THEME_TYPE, defaultThemeType.name)

        val parsedTheme = ThemeManager.ThemeType.entries.firstOrNull { it.name == savedThemeType }
            ?: defaultThemeType
        val normalizedTheme = normalizeThemeTypeForPlatform(parsedTheme)

        return ThemeSettings(
            themeType = normalizedTheme,
            isAmoled = savedAmoled,
            isDynamicColor = savedDynamic,
            customColor = Color(savedColor)
        )
    }

    fun writeThemeType(themeType: ThemeManager.ThemeType) {
        putDirect(KSafeProvider.prefs, Keys.THEME_TYPE, normalizeThemeTypeForPlatform(themeType).name)
    }

    fun writeAmoled(enabled: Boolean) {
        putDirect(KSafeProvider.prefs, Keys.IS_AMOLED, enabled)
    }

    fun writeDynamicColor(enabled: Boolean) {
        putDirect(KSafeProvider.prefs, Keys.IS_DYNAMIC_COLOR, enabled)
    }

    fun writeCustomColor(color: Color) {
        putDirect(KSafeProvider.prefs, Keys.CUSTOM_COLOR, color.toArgb())
    }

    private object Keys {
        const val CUSTOM_COLOR = "custom_color"
        const val IS_AMOLED = "is_amoled"
        const val IS_DYNAMIC_COLOR = "is_dynamic_color"
        const val THEME_TYPE = "theme_type"
    }
}

