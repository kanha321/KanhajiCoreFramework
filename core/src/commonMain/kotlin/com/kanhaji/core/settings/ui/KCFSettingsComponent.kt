package com.kanhaji.core.settings.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kanhaji.core.settings.ui.components.ColorPickerDialog
import com.kanhaji.core.settings.ui.components.ThemeSelectionDialog
import kotlinx.coroutines.delay

data class Group<T>(
    val header: String,
    val items: List<T>
)

@Composable
fun KCFSettingsComponent(
    groups: List<Group<SettingItems>>,
    model: KCFSettingsScreenModel
) {
    val allGroups = listOf(
        KCFThemeGroup(model),
        KCFNavigationGroup()
    ) + groups
    val uiState = model.uiState
    val listState = rememberLazyListState()

    GroupedLazyColumn(
        groups = allGroups,
        keySelector = { item -> item.id },
        contentPadding = PaddingValues(0.dp),
        listState = listState,
        onItemClick = { _, _, item -> item.onClick() },
        itemContent = { _, _, item ->
            SettingsCard(item = item)
        }
    )

    if (uiState.showThemeDialog) {
        ThemeSelectionDialog(
            model = model,
            onDismiss = { model.showThemeDialog = false },
            onConfirm = { model.showThemeDialog = false }
        )
    }

    if (uiState.showColorPicker) {
        ColorPickerDialog(
            model = model,
            currentColor = uiState.theme.customColor,
            onDismiss = { model.showColorPicker = false },
            onConfirm = { model.showColorPicker = false }
        )
    }
}

private const val SettingsItemAnimDurationMs = 220
private const val SettingsItemEnterDelayMs = 16L

@Composable
fun <T> GroupedLazyColumn(
    groups: List<Group<T>>,
    keySelector: (T) -> Any,
    contentPadding: PaddingValues,
    listState: LazyListState,
    onItemClick: (groupIndex: Int, itemIndex: Int, item: T) -> Unit,
    itemContent: @Composable (groupIndex: Int, itemIndex: Int, item: T) -> Unit
) {
    var displayGroups by remember { mutableStateOf(groups) }
    val itemVisibility = remember { mutableStateMapOf<String, Boolean>() }
    var isInitialPassDone by remember { mutableStateOf(false) }

    // Keep removed items temporarily for exit animation and animate newly added items on entry.
    LaunchedEffect(groups) {
        val liveKeys = groups.flatMapIndexed { groupIndex, group ->
            group.items.map { stableItemKey(groupIndex, keySelector(it)) }
        }.toSet()

        if (!isInitialPassDone) {
            // First composition: show existing items immediately; animate only subsequent insertions.
            displayGroups = groups
            liveKeys.forEach { key -> itemVisibility[key] = true }
            isInitialPassDone = true
            return@LaunchedEffect
        }

        displayGroups = mergeGroupsForAnimation(
            previous = displayGroups,
            current = groups,
            keySelector = keySelector,
            keepRemoved = true
        )

        val displayKeys = displayGroups.flatMapIndexed { groupIndex, group ->
            group.items.map { stableItemKey(groupIndex, keySelector(it)) }
        }.toSet()

        // Mark removed items invisible to play exit animation.
        itemVisibility.keys.filter { it !in liveKeys }.forEach { removedKey ->
            itemVisibility[removedKey] = false
        }

        // Initialize new display entries hidden so they can animate in.
        displayKeys.forEach { displayKey ->
            if (displayKey !in itemVisibility) {
                itemVisibility[displayKey] = false
            }
        }

        // Trigger enter animation for items currently present.
        delay(SettingsItemEnterDelayMs)
        liveKeys.forEach { liveKey ->
            itemVisibility[liveKey] = true
        }

        // After exit animation, remove stale entries from list and visibility map.
        delay(SettingsItemAnimDurationMs.toLong())
        displayGroups = mergeGroupsForAnimation(
            previous = displayGroups,
            current = groups,
            keySelector = keySelector,
            keepRemoved = false
        )
        itemVisibility.keys.filter { it !in liveKeys }.forEach { staleKey ->
            itemVisibility.remove(staleKey)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        contentPadding = contentPadding,
        state = listState
    ) {
        displayGroups.forEachIndexed { groupIndex, group ->

            if (group.header.isNotBlank() && group.items.isNotEmpty()) {
                item(key = "header-$groupIndex-${group.header}") {
                    Text(
                        text = group.header,
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                    HorizontalDivider()
                }
            }

            itemsIndexed(
                items = group.items,
                key = { _, item -> stableItemKey(groupIndex, keySelector(item)) }
            ) { itemIndex, item ->
                val itemKey = stableItemKey(groupIndex, keySelector(item))
                val isVisible = itemVisibility[itemKey] ?: !isInitialPassDone

                AnimatedVisibility(
                    visible = isVisible,
                    enter = slideInVertically(
                        initialOffsetY = { -it },
                        animationSpec = tween(SettingsItemAnimDurationMs)
                    ) + expandVertically(
                        expandFrom = Alignment.Top,
                        animationSpec = tween(SettingsItemAnimDurationMs)
                    ) +
                        fadeIn(animationSpec = tween(SettingsItemAnimDurationMs)),
                    exit = slideOutVertically(
                        targetOffsetY = { -it / 2 },
                        animationSpec = tween(SettingsItemAnimDurationMs)
                    ) + shrinkVertically(animationSpec = tween(SettingsItemAnimDurationMs)) +
                        fadeOut(animationSpec = tween(SettingsItemAnimDurationMs))
                ) {
                    Surface(
                        onClick = { if (isVisible) onItemClick(groupIndex, itemIndex, item) },
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
}

private fun stableItemKey(groupIndex: Int, itemKey: Any): String =
    "item-$groupIndex-$itemKey"

private fun <T> mergeGroupsForAnimation(
    previous: List<Group<T>>,
    current: List<Group<T>>,
    keySelector: (T) -> Any,
    keepRemoved: Boolean
): List<Group<T>> {
    return current.mapIndexed { index, liveGroup ->
        val previousGroup = previous.getOrNull(index)
        if (previousGroup == null || !keepRemoved) {
            return@mapIndexed liveGroup
        }

        val previousByKey = previousGroup.items.associateBy(keySelector)
        val liveByKey = liveGroup.items.associateBy(keySelector)

        val previousKeys = previousGroup.items.map(keySelector)
        val liveKeys = liveGroup.items.map(keySelector)
        val removedKeys = previousKeys.filter { it !in liveByKey }

        // Keep live order for insertions, then temporarily reinsert removed items at prior slots.
        val mergedKeys = liveKeys.toMutableList()
        var insertedOffset = 0
        removedKeys.forEach { removedKey ->
            val previousIndex = previousKeys.indexOf(removedKey)
            val targetIndex = (previousIndex + insertedOffset).coerceIn(0, mergedKeys.size)
            mergedKeys.add(targetIndex, removedKey)
            insertedOffset += 1
        }

        val mergedItems = mergedKeys.mapNotNull { key -> liveByKey[key] ?: previousByKey[key] }
        Group(header = liveGroup.header, items = mergedItems)
    }
}
