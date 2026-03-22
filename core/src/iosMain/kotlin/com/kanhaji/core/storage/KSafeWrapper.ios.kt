package com.kanhaji.core.storage

import eu.anifantakis.lib.ksafe.KSafe

actual typealias KSafeWrapper = KSafe

actual fun getDirect(ksafe: KSafeWrapper, key: String, defaultValue: Int): Int =
    ksafe.getDirect(key, defaultValue)

actual fun getDirect(ksafe: KSafeWrapper, key: String, defaultValue: Boolean): Boolean =
    ksafe.getDirect(key, defaultValue)

actual fun getDirect(ksafe: KSafeWrapper, key: String, defaultValue: String): String =
    ksafe.getDirect(key, defaultValue)

actual fun putDirect(ksafe: KSafeWrapper, key: String, value: Int) {
    ksafe.putDirect(key, value)
}

actual fun putDirect(ksafe: KSafeWrapper, key: String, value: Boolean) {
    ksafe.putDirect(key, value)
}

actual fun putDirect(ksafe: KSafeWrapper, key: String, value: String) {
    ksafe.putDirect(key, value)
}
