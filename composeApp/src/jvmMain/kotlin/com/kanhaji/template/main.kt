package com.kanhaji.template

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.kanhaji.core.storage.initKSafe

fun main() = application {
    initKSafe()
    Window(
        onCloseRequest = ::exitApplication,
        title = "Template",
    ) {
        App()
    }
}