package com.kanhaji.core.shell.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Stable
data class TopBarState(
    val title: String = "",
    val showBack: Boolean = false,
    val actions: @Composable androidx.compose.foundation.layout.RowScope.() -> Unit = {}
)

@Stable
data class FabState(
    val isVisible: Boolean = false,
    val icon: ImageVector? = null,
    val contentDescription: String = "Action",
    val onClick: () -> Unit = {}
)

class ShellController internal constructor(
    private val snackbarHostState: SnackbarHostState,
    private val coroutineScope: CoroutineScope
) {
    internal val topBarState = mutableStateOf(TopBarState())
    internal val fabState = mutableStateOf(FabState())

    fun setTopBar(state: TopBarState) {
        topBarState.value = state
    }

    fun clearTopBar() {
        topBarState.value = TopBarState()
    }

    fun setFab(state: FabState) {
        fabState.value = state
    }

    fun clearFab() {
        fabState.value = FabState()
    }

    fun reset() {
        clearTopBar()
        clearFab()
    }

    fun showSnackbar(message: String) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }
}

val LocalShellController = staticCompositionLocalOf<ShellController> {
    error("ShellController is not available. Wrap content with ShellScaffold.")
}

@Composable
fun rememberShellController(
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
): ShellController {
    return remember(snackbarHostState, coroutineScope) {
        ShellController(
            snackbarHostState = snackbarHostState,
            coroutineScope = coroutineScope
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShellScaffold(
    canPop: Boolean,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val shellController = rememberShellController(snackbarHostState, coroutineScope)

    val topBarState = shellController.topBarState.value
    val fabState = shellController.fabState.value

    CompositionLocalProvider(LocalShellController provides shellController) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(topBarState.title) },
                    navigationIcon = {
                        if (topBarState.showBack && canPop) {
                            IconButton(onClick = onBack) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    },
                    actions = topBarState.actions
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            floatingActionButton = {
                if (fabState.isVisible && fabState.icon != null) {
                    FloatingActionButton(onClick = fabState.onClick) {
                        Icon(
                            imageVector = fabState.icon,
                            contentDescription = fabState.contentDescription
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 12.dp)
            ) {
                content()
            }
        }
    }
}

