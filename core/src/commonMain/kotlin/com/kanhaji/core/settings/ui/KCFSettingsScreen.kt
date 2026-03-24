package com.kanhaji.core.settings.ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import com.kanhaji.core.shell.ui.BindShellState
import com.kanhaji.core.shell.ui.FabState
import com.kanhaji.core.shell.ui.TopBarState

data class KCFSettingsScreen(
    val groups: List<Group<SettingItems>> = emptyList()
) : Screen {
    @Composable
    override fun Content() {
        val model = rememberScreenModel { KCFSettingsScreenModel() }

        BindShellState(
            topBar = TopBarState(
                title = "Settings",
                showBack = true
            ),
            fab = FabState(isVisible = false)
        )

        KCFSettingsComponent(
            groups = groups,
            model = model
        )
    }
}