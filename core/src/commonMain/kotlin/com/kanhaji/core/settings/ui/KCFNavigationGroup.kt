package com.kanhaji.core.settings.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import com.kanhaji.core.shell.ui.hasHardwareKeyboardConnected
import com.kanhaji.core.shell.ui.LocalShellController

@Suppress("ComposableNaming")
@Composable
fun KCFNavigationGroup(): Group<SettingItems> {
	val shellController = LocalShellController.current
	val escapeAsBackEnabled = shellController.escapeAsBackEnabled
	val hasHardwareKeyboard = hasHardwareKeyboardConnected()

	if (!hasHardwareKeyboard) {
		return Group(header = "Navigation", items = emptyList())
	}

	return Group(
		header = "Navigation",
		items = listOf(
			SettingItems(
				id = "escape_as_back",
				title = "Escape as Back",
				description = "Use Escape key as back action",
				icon = Icons.AutoMirrored.Outlined.ArrowBack,
				widget = {
					Switch(
						checked = escapeAsBackEnabled,
						onCheckedChange = shellController::setEscapeAsBack
					)
				},
				onClick = { shellController.setEscapeAsBack(!escapeAsBackEnabled) }
			)
		)
	)
}
