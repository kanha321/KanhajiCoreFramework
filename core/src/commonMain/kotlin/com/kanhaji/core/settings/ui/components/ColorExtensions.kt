package com.kanhaji.core.settings.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

fun Color.toHex(): String =
    (toArgb() and 0x00FFFFFF)
        .toString(16)
        .padStart(6, '0')
        .uppercase()

fun String.hexToColor(fallback: Color): Color = try {
    Color((toLong(16) or 0xFF000000L).toInt())
} catch (_: Throwable) {
    fallback
}

