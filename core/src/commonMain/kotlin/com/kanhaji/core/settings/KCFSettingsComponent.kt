package com.kanhaji.core.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.kanhaji.core.settings.components.ColorPickerDialog
import com.kanhaji.core.settings.components.ThemeSelectionDialog
import com.kanhaji.core.theme.ThemeManager
import androidx.compose.foundation.shape.RoundedCornerShape

data class Group<T>(
    val header: String,
    val items: List<T>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KCFSettingsComponent(
    groups: List<Group<SettingItems>>,
    model: KCFSettingsScreenModel
) {
    val navigator = LocalNavigator.currentOrThrow
    val allGroups = listOf(KCFThemeGroup(model)) + groups

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    if (navigator.canPop) {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        val listState = rememberLazyListState()
        GroupedLazyColumn(
            groups = allGroups,
            keySelector = { item -> item.id },
            contentPadding = innerPadding,
            listState = listState,
            onItemClick = { _, _, item -> item.onClick() },
            itemContent = { _, _, item ->
                SettingsCard(item = item)
            }
        )

        if (model.showThemeDialog) {
            ThemeSelectionDialog(
                model = model,
                onDismiss = { model.showThemeDialog = false },
                onConfirm = { model.showThemeDialog = false }
            )
        }

        if (model.showColorPicker) {
            ColorPickerDialog(
                model = model,
                currentColor = ThemeManager.customColor.value,
                onDismiss = { model.showColorPicker = false },
                onConfirm = { model.showColorPicker = false }
            )
        }
    }
}

@Composable
fun <T> GroupedLazyColumn(
    groups: List<Group<T>>,
    keySelector: (T) -> Any,
    contentPadding: PaddingValues,
    listState: LazyListState,
    onItemClick: (groupIndex: Int, itemIndex: Int, item: T) -> Unit,
    itemContent: @Composable (groupIndex: Int, itemIndex: Int, item: T) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        contentPadding = contentPadding,
        state = listState
    ) {
        groups.forEachIndexed { groupIndex, group ->
            item(key = "header-$groupIndex-${group.header}") {
                if (group.header.isNotBlank()) {
                    Text(
                        text = group.header,
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                    HorizontalDivider()
                }
            }

            itemsIndexed(
                items = group.items,
                key = { itemIndex, item -> "item-$groupIndex-$itemIndex-${keySelector(item)}" }
            ) { itemIndex, item ->
                Surface(
                    onClick = { onItemClick(groupIndex, itemIndex, item) },
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    itemContent(groupIndex, itemIndex, item)
                }
            }
        }
    }
}
