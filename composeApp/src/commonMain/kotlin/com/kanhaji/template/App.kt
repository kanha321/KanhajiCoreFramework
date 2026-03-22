package com.kanhaji.template

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.kanhaji.core.theme.KCF
import com.kanhaji.core.shell.ui.ShellScaffold

@Composable
fun App() {
    KCF {
        Navigator(SplashScreen) { navigator ->
            ShellScaffold(
                canPop = navigator.canPop,
                onBack = { navigator.pop() },
                content = { SlideTransition(navigator) }
            )
        }
    }
}