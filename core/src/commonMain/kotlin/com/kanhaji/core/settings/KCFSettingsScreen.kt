package com.kanhaji.core.settings

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen

data class KCFSettingsScreen(
    val groups: List<Group<SettingItems>>
) : Screen {
    @Composable
    override fun Content() {
        val model = rememberScreenModel { KCFSettingsScreenModel() }
        KCFSettingsComponent(
            groups = groups,
            model = model
        )
    }
}
