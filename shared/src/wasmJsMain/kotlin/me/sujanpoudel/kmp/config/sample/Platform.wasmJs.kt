package me.sujanpoudel.kmp.config.sample

import me.sujanpoudel.kmp.config.sample.Platform

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()
