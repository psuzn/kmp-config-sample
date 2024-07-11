package me.sujanpoudel.kmp.config.sample

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
