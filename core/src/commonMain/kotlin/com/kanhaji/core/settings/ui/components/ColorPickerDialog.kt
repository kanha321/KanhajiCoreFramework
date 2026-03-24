package com.kanhaji.core.settings.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    var hexCode by remember { mutableStateOf(currentColor.toHex()) }

    val controller = rememberColorPickerController()
    val scope = rememberCoroutineScope()
    var debounceJob by remember { mutableStateOf<Job?>(null) }

    // Keep HEX text in sync when color changes from picker.
    LaunchedEffect(selectedColor) {
        val newHex = selectedColor.toHex()
        if (newHex != hexCode) {
            hexCode = newHex
        }
    }

    // Keep picker in sync when a full HEX value is typed.
    LaunchedEffect(hexCode) {
        if (hexCode.length == 6) {
            val newColor = hexCode.hexToColor(fallback = selectedColor)
            if (newColor != selectedColor) {
                selectedColor = newColor
                controller.selectByColor(newColor, fromUser = false)

                debounceJob?.cancel()
                debounceJob = scope.launch {
                    delay(150L)
                    model.updateCustomColor(newColor)
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        controller.selectByColor(currentColor, fromUser = false)
    }

    AlertDialog(
        onDismissRequest = {
            model.updateCustomColor(originalColor)
            onDismiss()
        },
        title = { Text("Pick a Color") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                HsvColorPicker(
                    initialColor = currentColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(10.dp),
                    controller = controller,
                    onColorChanged = { envelope ->
                        selectedColor = envelope.color

                        debounceJob?.cancel()
                        debounceJob = scope.launch {
                            delay(150L)
                            model.updateCustomColor(selectedColor)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = hexCode,
                    onValueChange = { newHex ->
                        val filteredHex = newHex.filter { it.isDigit() || it in "abcdefABCDEF" }
                        if (filteredHex.length <= 6) {
                            hexCode = filteredHex.uppercase()
                        }
                    },
                    label = { Text("Hex Code") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        keyboardType = KeyboardType.Ascii
                    ),
                    leadingIcon = {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(selectedColor, shape = CircleShape)
                                .border(1.dp, Color.Gray, CircleShape)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                model.updateCustomColor(originalColor)
                onDismiss()
            }) {
                Text("Cancel")
            }
        }
    )
}
