package me.sujanpoudel.kmp.config.sample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Kotlin Multiplatform Configuration",
    ) {
        App()
    }
}
