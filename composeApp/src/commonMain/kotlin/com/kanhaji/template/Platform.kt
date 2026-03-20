package com.kanhaji.template

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform