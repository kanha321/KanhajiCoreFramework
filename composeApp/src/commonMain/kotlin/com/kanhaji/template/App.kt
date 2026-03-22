package com.kanhaji.template

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.kanhaji.core.shell.ui.ShellScaffold
import com.kanhaji.core.theme.KCF

@Composable
@Preview
fun App() {
    KCF {
        Navigator(Screen1) { navigator ->
            ShellScaffold(
                canPop = navigator.canPop,
                onBack = { navigator.pop() }
            ) {
                SlideTransition(navigator)
            }
        }
    }
}