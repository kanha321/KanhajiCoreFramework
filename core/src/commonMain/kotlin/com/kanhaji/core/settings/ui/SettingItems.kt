package com.kanhaji.core.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class SettingItems(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val widget: @Composable () -> Unit = {},
    val onClick: () -> Unit = {}
)

