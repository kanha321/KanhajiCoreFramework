package com.kanhaji.template

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.kanhaji.core.settings.KCFSettingsScreen
import com.kanhaji.core.theme.KCF

@Composable
@Preview
fun App() {
    KCF {
        Navigator(KCFSettingsScreen(groups = emptyList())) { navigator ->
            SlideTransition(navigator)
        }
    }
}