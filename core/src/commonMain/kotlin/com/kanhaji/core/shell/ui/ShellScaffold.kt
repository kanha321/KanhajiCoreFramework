package com.kanhaji.core.shell.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Stable
data class TopBarState(
    val isVisible: Boolean = true,
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

data class ShellSnackbarMessage(
    val id: Long,
    val message: String
)

class ShellController internal constructor(
    private val coroutineScope: CoroutineScope
) {
    internal val topBarState = mutableStateOf(TopBarState())
    internal val fabState = mutableStateOf(FabState())
    internal var snackbarConfig = SnackbarConfig()
    private val topBarStatesByOwner = LinkedHashMap<Any, TopBarState>()
    private val fabStatesByOwner = LinkedHashMap<Any, FabState>()
    private var activeTopBarOwner: Any? = null
    private var activeFabOwner: Any? = null
    internal val snackbarMessage = mutableStateOf<ShellSnackbarMessage?>(null)
    private var snackbarJob: Job? = null
    private var snackbarId = 0L

    fun setTopBar(state: TopBarState) {
        topBarState.value = state
    }

    fun setTopBar(owner: Any, state: TopBarState) {
        topBarStatesByOwner[owner] = state
        activeTopBarOwner = owner
        topBarState.value = state
    }

    fun clearTopBar() {
        topBarState.value = TopBarState()
    }

    fun clearTopBar(owner: Any) {
        topBarStatesByOwner.remove(owner)

        if (activeTopBarOwner == owner) {
            val previousOwner = topBarStatesByOwner.keys.lastOrNull()
            if (previousOwner != null) {
                activeTopBarOwner = previousOwner
                topBarState.value = topBarStatesByOwner.getValue(previousOwner)
            } else {
                // Keep the last rendered top bar until another screen provides its config.
                activeTopBarOwner = null
            }
        }
    }

    fun setFab(state: FabState) {
        fabState.value = state
    }

    fun setFab(owner: Any, state: FabState) {
        fabStatesByOwner[owner] = state
        activeFabOwner = owner
        fabState.value = state
    }

    fun clearFab() {
        fabState.value = FabState()
    }

    fun clearFab(owner: Any) {
        fabStatesByOwner.remove(owner)

        if (activeFabOwner == owner) {
            val previousOwner = fabStatesByOwner.keys.lastOrNull()
            if (previousOwner != null) {
                activeFabOwner = previousOwner
                fabState.value = fabStatesByOwner.getValue(previousOwner)
            } else {
                // Keep the last rendered FAB until another screen provides its config.
                activeFabOwner = null
            }
        }
    }

    fun reset() {
        clearTopBar()
        clearFab()
    }

    fun release(owner: Any) {
        clearTopBar(owner)
        clearFab(owner)
    }

    /**
     * Set snackbar configuration for customizing appearance and auto-dismiss duration.
     */
    fun setSnackbarConfig(config: SnackbarConfig) {
        snackbarConfig = config
    }

    fun showSnackbar(message: String) {
        snackbarJob?.cancel()
        snackbarJob = coroutineScope.launch {
            // Option B with timer reset: if same message is already visible,
            // increment the ID to trigger a fresh timer restart
            if (snackbarMessage.value?.message == message) {
                snackbarId += 1
                snackbarMessage.value = snackbarMessage.value?.copy(id = snackbarId)
                return@launch
            }

            if (snackbarMessage.value != null) {
                snackbarMessage.value = null
                delay(120)
            }

            snackbarId += 1
            snackbarMessage.value = ShellSnackbarMessage(
                id = snackbarId,
                message = message
            )
        }
    }

    fun dismissSnackbar() {
        snackbarJob?.cancel()
        snackbarMessage.value = null
    }
}

val LocalShellController = staticCompositionLocalOf<ShellController> {
    error("ShellController is not available. Wrap content with ShellScaffold.")
}

/**
 * Binds the current screen to shell UI slots with automatic cleanup.
 * Call this once near the top of a Screen.Content composable.
 */
@Composable
fun BindShellState(
    topBar: TopBarState,
    fab: FabState = FabState(),
    controller: ShellController = LocalShellController.current
) {
    val owner = remember { Any() }

    SideEffect {
        controller.setTopBar(owner = owner, state = topBar)
        controller.setFab(owner = owner, state = fab)
    }

    DisposableEffect(controller, owner) {
        onDispose {
            controller.release(owner)
        }
    }
}

@Composable
fun rememberShellController(
    coroutineScope: CoroutineScope
): ShellController {
    return remember(coroutineScope) {
        ShellController(
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
    val coroutineScope = rememberCoroutineScope()
    val shellController = rememberShellController(coroutineScope)

    val topBarState = shellController.topBarState.value
    val fabState = shellController.fabState.value
    val snackbarMessage = shellController.snackbarMessage.value
    val focusRequester = remember { FocusRequester() }
    val hasFab = fabState.isVisible && fabState.icon != null
    val density = LocalDensity.current
    val navigationBarsBottomPadding = with(density) {
        WindowInsets.navigationBars.getBottom(this).toDp()
    }
    val snackbarBottomPadding = if (hasFab) {
        // FAB baseline: nav inset + 16.dp margin + 56.dp size + gap above FAB.
        navigationBarsBottomPadding + 84.dp
    } else {
        // When no FAB, keep snackbar near the bottom with a standard margin.
        navigationBarsBottomPadding + 16.dp
    }

    val handleBackKey: (androidx.compose.ui.input.key.KeyEvent) -> Boolean = { keyEvent ->
        if (
            keyEvent.type == KeyEventType.KeyUp &&
            (keyEvent.key == Key.Escape || keyEvent.key == Key.Back) &&
            canPop
        ) {
            onBack()
            true
        } else {
            false
        }
    }

    LaunchedEffect(canPop, topBarState.isVisible, fabState.isVisible) {
        // Focus requests can race with navigation transition/layout passes,
        // so we retry briefly to make Escape/back handling stable.
        repeat(4) {
            delay(16)
            focusRequester.requestFocus()
        }
    }

    val backKeyModifier = Modifier
        .onPreviewKeyEvent(handleBackKey)
        .onKeyEvent(handleBackKey)

    CompositionLocalProvider(LocalShellController provides shellController) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .focusRequester(focusRequester)
                .focusable()
                .then(backKeyModifier),
            topBar = {
                if (topBarState.isVisible) {
                    TopAppBar(
                        title = { Text(topBarState.title) },
                        navigationIcon = {
                            if (topBarState.showBack && canPop) {
                                IconButton(onClick = onBack) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back"
                                    )
                                }
                            }
                        },
                        actions = topBarState.actions
                    )
                }
            },
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
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp)
                        .then(backKeyModifier)
                ) {
                    content()
                }

                CustomSnackbar(
                    message = snackbarMessage?.message.orEmpty(),
                    onDismiss = shellController::dismissSnackbar,
                    isVisible = snackbarMessage != null,
                    config = shellController.snackbarConfig,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp,
                            bottom = snackbarBottomPadding
                        )
                )

                // Auto-dismiss snackbar based on config duration
                if (snackbarMessage != null && shellController.snackbarConfig.autoDismissDuration > 0) {
                    LaunchedEffect(snackbarMessage.id) {
                        delay(shellController.snackbarConfig.autoDismissDuration)
                        shellController.dismissSnackbar()
                    }
                }
            }
        }
    }
}

