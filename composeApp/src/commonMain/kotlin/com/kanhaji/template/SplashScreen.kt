package com.kanhaji.template

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.kanhaji.core.settings.data.ThemeSettingsRepository
import com.kanhaji.core.settings.domain.ThemeSettingsApplier
import com.kanhaji.core.settings.domain.usecase.LoadThemeSettingsUseCase
import com.kanhaji.core.shell.ui.BindShellState
import com.kanhaji.core.shell.ui.FabState
import com.kanhaji.core.shell.ui.TopBarState
import kotlinx.coroutines.delay

object SplashScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val loadThemeSettings = remember {
            LoadThemeSettingsUseCase(
                repository = ThemeSettingsRepository(),
                applier = ThemeSettingsApplier()
            )
        }

        BindShellState(
            topBar = TopBarState(
                isVisible = false
            ),
            fab = FabState(isVisible = false)
        )

        LaunchedEffect(Unit) {
            // Apply persisted theme flags and seed color before moving to the app screens.
            loadThemeSettings()
            delay(1350)
            navigator.replace(Screen1)
        }

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Loading...")
            }
        }
    }
}


