package com.kanhaji.core.settings.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Brightness4
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kanhaji.core.settings.KCFSettingsScreenModel
import com.kanhaji.core.theme.ThemeManager

data class RadioItem(
    val label: String,
    val onClick: () -> Unit
)

@Composable
fun RadioSelectionDialog(
    title: String,
    options: List<RadioItem>,
    initialSelection: Int?,
    icon: ImageVector,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    var selectedIndex by rememberSaveable { mutableStateOf(initialSelection) }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(imageVector = icon, contentDescription = null) },
        title = { Text(text = title) },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(options) { index, item ->
                    Surface(
                        onClick = {
                            selectedIndex = index
                            item.onClick()
                        },
                        shape = RoundedCornerShape(16.dp),
                        color = Color.Transparent,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedIndex == index,
                                onClick = {
                                    selectedIndex = index
                                    item.onClick()
                                }
                            )
                            Text(text = item.label)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Confirm") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun ThemeSelectionDialog(
    model: KCFSettingsScreenModel,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val originalTheme = remember { ThemeManager.currentThemeType }

    RadioSelectionDialog(
        title = "Select App Theme",
        options = listOf(
            RadioItem(label = "Light") { model.updateTheme(ThemeManager.ThemeType.LIGHT) },
            RadioItem(label = "Dark") { model.updateTheme(ThemeManager.ThemeType.DARK) },
            RadioItem(label = "System") { model.updateTheme(ThemeManager.ThemeType.SYSTEM) }
        ),
        initialSelection = ThemeManager.currentThemeType.ordinal,
        icon = Icons.Outlined.Brightness4,
        onConfirm = onConfirm,
        onDismiss = {
            model.updateTheme(originalTheme)
            onDismiss()
        }
    )
}
