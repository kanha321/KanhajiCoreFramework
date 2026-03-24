package com.kanhaji.template

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.kanhaji.core.shell.ui.FabState
import com.kanhaji.core.shell.ui.LocalShellController
import com.kanhaji.core.shell.ui.TopBarState
import com.kanhaji.core.settings.ui.KCFSettingsScreen
import com.kanhaji.core.theme.ThemeManager

object Screen2 : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val shellController = LocalShellController.current
        val shellOwner = remember { Any() }

        SideEffect {
            shellController.setTopBar(
                owner = shellOwner,
                state = TopBarState(
                    title = "Screen 2",
                    showBack = true,
                    actions = {
                        IconButton(onClick = { navigator.push(KCFSettingsScreen(groups = emptyList())) }) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = "Open settings"
                            )
                        }
                    }
                )
            )
            shellController.setFab(
                owner = shellOwner,
                state = FabState(
                    isVisible = true,
                    icon = Icons.Outlined.Image,
                    contentDescription = "Show snackbar",
                    onClick = { shellController.showSnackbar("FAB clicked on Screen 2") }
                )
            )
        }

        DisposableEffect(shellController, shellOwner) {
            onDispose {
                shellController.release(shellOwner)
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { navigator.pop() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = null
                )
                Text("Back to Screen 1")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(onClick = { shellController.showSnackbar("Snackbar from Screen 2") }) {
                Text("Show Snackbar")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                colorToHex(ThemeManager.customColor.value)
            )
        }
    }
}

private fun colorToHex(color: Color): String =
    (color.toArgb() and 0x00FFFFFF)
        .toString(16)
        .padStart(6, '0')
        .uppercase()






