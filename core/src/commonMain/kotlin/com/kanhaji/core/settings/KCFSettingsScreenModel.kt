package com.kanhaji.core.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import cafe.adriel.voyager.core.model.ScreenModel
import com.kanhaji.core.storage.KSafeProvider
import com.kanhaji.core.storage.getDirect
import com.kanhaji.core.storage.putDirect
import com.kanhaji.core.theme.ThemeManager

class KCFSettingsScreenModel : ScreenModel {
    var showColorPicker by mutableStateOf(false)
    var showThemeDialog by mutableStateOf(false)

    init {
        val prefs = KSafeProvider.prefs
        val savedColor = getDirect(prefs, Keys.CUSTOM_COLOR, ThemeManager.customColor.value.toArgb())
        val savedAmoled = getDirect(prefs, Keys.IS_AMOLED, false)
        val savedDynamic = getDirect(prefs, Keys.IS_DYNAMIC_COLOR, false)
        val savedThemeType = getDirect(prefs, Keys.THEME_TYPE, ThemeManager.ThemeType.SYSTEM.name)

        ThemeManager.customColor.value = Color(savedColor)
        ThemeManager.isAmoled = savedAmoled
        ThemeManager.isDynamicColor = savedDynamic

        val parsedTheme = ThemeManager.ThemeType.entries.firstOrNull { it.name == savedThemeType }
            ?: ThemeManager.ThemeType.SYSTEM
        ThemeManager.setTheme(parsedTheme)
    }

    fun updateAmoled(enabled: Boolean) {
        ThemeManager.isAmoled = enabled
        putDirect(KSafeProvider.prefs, Keys.IS_AMOLED, enabled)
    }

    fun updateDynamicColor(enabled: Boolean) {
        ThemeManager.isDynamicColor = enabled
        putDirect(KSafeProvider.prefs, Keys.IS_DYNAMIC_COLOR, enabled)
    }

    fun updateTheme(type: ThemeManager.ThemeType) {
        ThemeManager.setTheme(type)
        putDirect(KSafeProvider.prefs, Keys.THEME_TYPE, type.name)
    }

    fun updateCustomColor(color: Color) {
        ThemeManager.customColor.value = color
        putDirect(KSafeProvider.prefs, Keys.CUSTOM_COLOR, color.toArgb())
    }

    private object Keys {
        const val CUSTOM_COLOR = "custom_color"
        const val IS_AMOLED = "is_amoled"
        const val IS_DYNAMIC_COLOR = "is_dynamic_color"
        const val THEME_TYPE = "theme_type"
    }
}


