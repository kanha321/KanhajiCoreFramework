package com.kanhaji.core.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Brightness4
import androidx.compose.material.icons.outlined.Brightness5
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.Colorize
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.kanhaji.core.theme.ThemeManager
import com.kanhaji.core.theme.isDynamicColorSupported

@Composable
fun KCFThemeGroup(model: KCFSettingsScreenModel): Group<SettingItems> {
    val items = mutableListOf<SettingItems>()

    items += SettingItems(
        id = "app_theme",
        title = "App Theme",
        description = "Select your preferred app theme",
        icon = if (ThemeManager.isDarkTheme) Icons.Outlined.Brightness4 else Icons.Outlined.Brightness5,
        widget = {
            TextButton(onClick = { model.showThemeDialog = true }) {
                Text(ThemeManager.currentThemeType.name.lowercase().replaceFirstChar { it.uppercase() })
            }
        },
        onClick = { model.showThemeDialog = true }
    )

    if (ThemeManager.isDarkTheme) {
        items += SettingItems(
            id = "pitch_black",
            title = "Pitch Black",
            description = "Use pure black backgrounds in dark mode",
            icon = Icons.Outlined.DarkMode,
            widget = {
                Switch(
                    checked = ThemeManager.isAmoled,
                    onCheckedChange = model::updateAmoled
                )
            },
            onClick = { model.updateAmoled(!ThemeManager.isAmoled) }
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
                    checked = ThemeManager.isDynamicColor,
                    onCheckedChange = model::updateDynamicColor
                )
            },
            onClick = { model.updateDynamicColor(!ThemeManager.isDynamicColor) }
        )
    }

    if (!ThemeManager.isDynamicColor) {
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




