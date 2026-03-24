package com.kanhaji.core.shell.ui

import android.content.res.Configuration
import android.content.res.Resources

actual fun hasHardwareKeyboardConnected(): Boolean {
    val configuration = Resources.getSystem().configuration
    return configuration.keyboard != Configuration.KEYBOARD_NOKEYS
}
