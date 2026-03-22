package com.kanhaji.core.shell.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * Configuration for CustomSnackbar appearance and behavior.
 *
 * @param containerColor Background color of the snackbar
 * @param contentColor Text and icon color
 * @param containerPadding Padding inside the snackbar container
 * @param autoDismissDuration Duration in milliseconds before auto-dismissing (0 to disable)
 * @param shape Shape of the snackbar container
 */
data class SnackbarConfig(
    val containerColor: Color = Color.Unspecified,
    val contentColor: Color = Color.Unspecified,
    val containerPadding: SnackbarPadding = SnackbarPadding(),
    val autoDismissDuration: Long = 3000L, // 3 seconds default
    val shape: androidx.compose.foundation.shape.RoundedCornerShape? = null
)

/**
 * Customizable padding for snackbar container.
 */
data class SnackbarPadding(
    val start: androidx.compose.ui.unit.Dp = 16.dp,
    val end: androidx.compose.ui.unit.Dp = 4.dp,
    val top: androidx.compose.ui.unit.Dp = 8.dp,
    val bottom: androidx.compose.ui.unit.Dp = 8.dp
)

/**
 * Custom, fully customizable snackbar component.
 *
 * @param message Text to display in the snackbar
 * @param onDismiss Callback when dismiss button is clicked or auto-dismiss occurs
 * @param isVisible Whether the snackbar is visible
 * @param config Configuration for appearance and auto-dismiss behavior
 * @param modifier Modifier for the container
 */
@Composable
fun CustomSnackbar(
    message: String,
    onDismiss: () -> Unit,
    isVisible: Boolean,
    config: SnackbarConfig = SnackbarConfig(),
    modifier: Modifier = Modifier
) {
    // Determine actual colors - use material defaults if not specified
    val actualContainerColor = if (config.containerColor != Color.Unspecified) {
        config.containerColor
    } else {
        MaterialTheme.colorScheme.inverseSurface
    }

    val actualContentColor = if (config.contentColor != Color.Unspecified) {
        config.contentColor
    } else {
        MaterialTheme.colorScheme.inverseOnSurface
    }

    // Determine shape - use material defaults if not specified
    val actualShape = config.shape ?: MaterialTheme.shapes.large

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = modifier
    ) {
        Surface(
            shape = actualShape,
            color = actualContainerColor,
            contentColor = actualContentColor,
            tonalElevation = 6.dp,
            shadowElevation = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = config.containerPadding.start,
                        end = config.containerPadding.end,
                        top = config.containerPadding.top,
                        bottom = config.containerPadding.bottom
                    )
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Dismiss"
                    )
                }
            }
        }
    }
}

