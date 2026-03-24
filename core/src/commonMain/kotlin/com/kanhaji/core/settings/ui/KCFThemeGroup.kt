package com.kanhaji.core.settings.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Brightness4
import androidx.compose.material.icons.outlined.Brightness5
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.Colorize
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kanhaji.core.theme.ThemeManager
import com.kanhaji.core.theme.isDynamicColorSupported

@Suppress("ComposableNaming")
@Composable
fun KCFThemeGroup(model: KCFSettingsScreenModel): Group<SettingItems> {
    val themeState = model.uiState.theme
    val isDarkTheme = when (themeState.themeType) {
        ThemeManager.ThemeType.DARK -> true
        ThemeManager.ThemeType.LIGHT -> false
        ThemeManager.ThemeType.SYSTEM -> isSystemInDarkTheme()
    }
    val items = mutableListOf<SettingItems>()

    items += SettingItems(
        id = "app_theme",
        title = "App Theme",
        description = "Select your preferred app theme",
        icon = if (isDarkTheme) Icons.Outlined.Brightness4 else Icons.Outlined.Brightness5,
        widget = {
            TextButton(onClick = { model.showThemeDialog = true }) {
                Text(
                    text = themeState.themeLabel,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.widthIn(max = 84.dp)
                )
            }
        },
        onClick = { model.showThemeDialog = true }
    )

    if (isDarkTheme) {
        items += SettingItems(
            id = "pitch_black",
            title = "Pitch Black",
            description = "Use pure black backgrounds in dark mode",
            icon = Icons.Outlined.DarkMode,
            widget = {
                Switch(
                    checked = themeState.isAmoled,
                    onCheckedChange = model::updateAmoled
                )
            },
            onClick = { model.updateAmoled(!themeState.isAmoled) }
        )
    }

    if (isDynamicColorSupported()) {
        items += SettingItems(
            id = "dynamic_color",
            title = "Dynamic Color",
            description = "Use device-driven dynamic colors",
            icon = Icons.Outlined.ColorLens,
            widget = {
                Switch(
                    checked = themeState.isDynamicColor,
                    onCheckedChange = model::updateDynamicColor
                )
            },
            onClick = { model.updateDynamicColor(!themeState.isDynamicColor) }
        )
    }

    if (!themeState.isDynamicColor) {
        items += SettingItems(
            id = "app_color",
            title = "App Color",
            description = "Select a custom app color",
            icon = Icons.Outlined.Colorize,
            onClick = { model.showColorPicker = true }
        )
    }

    return Group(header = "Theme", items = items)
}
