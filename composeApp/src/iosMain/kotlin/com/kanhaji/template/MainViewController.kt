package com.kanhaji.template

import androidx.compose.ui.window.ComposeUIViewController
import com.kanhaji.core.storage.initKSafe

fun MainViewController() = ComposeUIViewController {
	initKSafe()
	App()
}
