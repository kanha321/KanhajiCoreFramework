package com.kanhaji.template

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.kanhaji.core.shell.ui.BindShellState
import com.kanhaji.core.shell.ui.FabState
import com.kanhaji.core.shell.ui.LocalShellController
import com.kanhaji.core.shell.ui.TopBarState
import com.kanhaji.core.settings.ui.KCFSettingsScreen

object Screen1 : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val shellController = LocalShellController.current

        BindShellState(
            topBar = TopBarState(
                title = "Screen 1",
                showBack = false,
                actions = {
                    IconButton(onClick = { navigator.push(KCFSettingsScreen(groups = emptyList())) }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Open settings"
                        )
                    }
                }
            ),
            fab = FabState(
                isVisible = true,
                icon = Icons.Outlined.Add,
                contentDescription = "Show snackbar",
                onClick = { 
                    shellController.showSnackbar("FAB clicked on Screen 1") 
                }
            )
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = { navigator.push(Screen2) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = null
                )
                Text("Go to Screen 2")
            }
        }
    }
}




