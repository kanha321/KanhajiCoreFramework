package com.kanhaji.core.settings.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.kanhaji.core.settings.ui.KCFSettingsScreenModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ColorPickerDialog(
    model: KCFSettingsScreenModel,
    currentColor: Color,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val originalColor = remember { currentColor }
    var selectedColor by remember { mutableStateOf(currentColor) }
    var hexCode by remember { mutableStateOf(colorToHex(currentColor)) }

    val pickerController = rememberColorPickerController()
    val scope = rememberCoroutineScope()
    var debounceJob by remember { mutableStateOf<Job?>(null) }

    fun scheduleColorUpdate(color: Color) {
        debounceJob?.cancel()
        debounceJob = scope.launch {
            delay(150)
            model.updateCustomColor(color)
        }
    }

    LaunchedEffect(selectedColor) {
        val newHex = colorToHex(selectedColor)
        if (newHex != hexCode) {
            hexCode = newHex
        }
    }

    AlertDialog(
        onDismissRequest = {
            model.updateCustomColor(originalColor)
            onDismiss()
        },
        title = { Text("Pick App Color") },
        text = {
            Column {
                HsvColorPicker(
                    initialColor = currentColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    controller = pickerController,
                    onColorChanged = { envelope ->
                        selectedColor = envelope.color
                        scheduleColorUpdate(envelope.color)
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = hexCode,
                    onValueChange = { input ->
                        val filtered = input
                            .filter { it.isDigit() || it in "abcdefABCDEF" }
                            .take(6)
                            .uppercase()
                        hexCode = filtered
                        if (filtered.length == 6) {
                            val parsedColor = hexToColor(filtered)
                            selectedColor = parsedColor
                            pickerController.selectByColor(parsedColor, fromUser = false)
                            scheduleColorUpdate(parsedColor)
                        }
                    },
                    singleLine = true,
                    label = { Text("HEX") },
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .background(selectedColor, CircleShape)
                                .border(width = 1.dp, color = Color.Gray, shape = CircleShape)
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        keyboardType = KeyboardType.Ascii
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    model.updateCustomColor(originalColor)
                    onDismiss()
                }
            ) {
                Text("Cancel")
            }
        }
    )
}

fun colorToHex(color: Color): String {
    return (color.toArgb() and 0x00FFFFFF).toString(16).padStart(6, '0').uppercase()
}

fun hexToColor(hex: String): Color {
    return try {
        val parsed = hex.toLong(16) or 0xFF000000
        Color(parsed)
    } catch (_: Throwable) {
        Color(0xFF6750A4)
    }
}
