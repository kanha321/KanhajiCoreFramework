package com.kanhaji.core.settings.domain.usecase

import androidx.compose.ui.graphics.Color
import com.kanhaji.core.settings.data.ThemeSettingsRepository
import com.kanhaji.core.settings.domain.ThemeSettings
import com.kanhaji.core.settings.domain.ThemeSettingsApplier
import com.kanhaji.core.theme.ThemeManager

class LoadThemeSettingsUseCase(
    private val repository: ThemeSettingsRepository,
    private val applier: ThemeSettingsApplier
) {
    operator fun invoke(): ThemeSettings {
        val settings = repository.load()
        applier.apply(settings)
        return settings
    }
}

class UpdateThemeTypeUseCase(
    private val repository: ThemeSettingsRepository,
    private val applier: ThemeSettingsApplier
) {
    operator fun invoke(themeType: ThemeManager.ThemeType): ThemeSettings {
        repository.saveThemeType(themeType)
        val updated = repository.load().copy(themeType = themeType)
        applier.apply(updated)
        return updated
    }
}

class UpdateAmoledUseCase(
    private val repository: ThemeSettingsRepository,
    private val applier: ThemeSettingsApplier
) {
    operator fun invoke(enabled: Boolean): ThemeSettings {
        repository.saveAmoled(enabled)
        val updated = repository.load().copy(isAmoled = enabled)
        applier.apply(updated)
        return updated
    }
}

class UpdateDynamicColorUseCase(
    private val repository: ThemeSettingsRepository,
    private val applier: ThemeSettingsApplier
) {
    operator fun invoke(enabled: Boolean): ThemeSettings {
        repository.saveDynamicColor(enabled)
        val updated = repository.load().copy(isDynamicColor = enabled)
        applier.apply(updated)
        return updated
    }
}

class UpdateCustomColorUseCase(
    private val repository: ThemeSettingsRepository,
    private val applier: ThemeSettingsApplier
) {
    operator fun invoke(color: Color): ThemeSettings {
        repository.saveCustomColor(color)
        val updated = repository.load().copy(customColor = color)
        applier.apply(updated)
        return updated
    }
}

