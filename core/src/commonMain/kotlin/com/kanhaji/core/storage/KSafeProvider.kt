package com.kanhaji.core.storage

expect class KSafeWrapper

object KSafeProvider {
    private const val NOT_INITIALIZED_MESSAGE =
        "KSafeProvider not initialized. Call initKSafe() at app entry point."

    private var prefsInstance: KSafeWrapper? = null
    private var secureInstance: KSafeWrapper? = null

    val prefs: KSafeWrapper
        get() = prefsInstance ?: error(NOT_INITIALIZED_MESSAGE)

    val secure: KSafeWrapper
        get() = secureInstance ?: error(NOT_INITIALIZED_MESSAGE)

    fun init(prefs: KSafeWrapper, secure: KSafeWrapper) {
        prefsInstance = prefs
        secureInstance = secure
    }
}

expect fun getDirect(ksafe: KSafeWrapper, key: String, defaultValue: Int): Int
expect fun getDirect(ksafe: KSafeWrapper, key: String, defaultValue: Boolean): Boolean
expect fun getDirect(ksafe: KSafeWrapper, key: String, defaultValue: String): String

expect fun putDirect(ksafe: KSafeWrapper, key: String, value: Int)
expect fun putDirect(ksafe: KSafeWrapper, key: String, value: Boolean)
expect fun putDirect(ksafe: KSafeWrapper, key: String, value: String)

fun initKSafe(prefs: KSafeWrapper, secure: KSafeWrapper) {
    KSafeProvider.init(prefs = prefs, secure = secure)
}


