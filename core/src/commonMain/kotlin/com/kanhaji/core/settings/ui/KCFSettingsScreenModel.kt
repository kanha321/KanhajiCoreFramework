package com.kanhaji.core.settings.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.model.ScreenModel
import com.kanhaji.core.settings.data.ThemeSettingsRepository
import com.kanhaji.core.settings.domain.ThemeSettingsApplier
import com.kanhaji.core.settings.domain.usecase.LoadThemeSettingsUseCase
import com.kanhaji.core.settings.domain.usecase.UpdateAmoledUseCase
import com.kanhaji.core.settings.domain.usecase.UpdateCustomColorUseCase
import com.kanhaji.core.settings.domain.usecase.UpdateDynamicColorUseCase
import com.kanhaji.core.settings.domain.usecase.UpdateThemeTypeUseCase
import com.kanhaji.core.settings.ui.SettingsUiState
import com.kanhaji.core.settings.ui.toUiState
import com.kanhaji.core.theme.ThemeManager

class KCFSettingsScreenModel : ScreenModel {
    private val repository = ThemeSettingsRepository()
    private val applier = ThemeSettingsApplier()

    private val loadThemeSettings = LoadThemeSettingsUseCase(repository, applier)
    private val updateThemeType = UpdateThemeTypeUseCase(repository, applier)
    private val updateAmoledSetting = UpdateAmoledUseCase(repository, applier)
    private val updateDynamicSetting = UpdateDynamicColorUseCase(repository, applier)
    private val updateCustomColorSetting = UpdateCustomColorUseCase(repository, applier)

    var uiState by mutableStateOf(SettingsUiState())
        private set

    var showColorPicker: Boolean
        get() = uiState.showColorPicker
        set(value) {
            uiState = uiState.copy(showColorPicker = value)
        }

    var showThemeDialog: Boolean
        get() = uiState.showThemeDialog
        set(value) {
            uiState = uiState.copy(showThemeDialog = value)
        }

    init {
        val loaded = loadThemeSettings()
        uiState = uiState.copy(theme = loaded.toUiState())
    }

    fun updateAmoled(enabled: Boolean) {
        val updated = updateAmoledSetting(enabled)
        uiState = uiState.copy(theme = updated.toUiState())
    }

    fun updateDynamicColor(enabled: Boolean) {
        val updated = updateDynamicSetting(enabled)
        uiState = uiState.copy(theme = updated.toUiState())
    }

    fun updateTheme(type: ThemeManager.ThemeType) {
        val updated = updateThemeType(type)
        uiState = uiState.copy(theme = updated.toUiState())
    }

    fun updateCustomColor(color: Color) {
        val updated = updateCustomColorSetting(color)
        uiState = uiState.copy(theme = updated.toUiState())
    }
}

