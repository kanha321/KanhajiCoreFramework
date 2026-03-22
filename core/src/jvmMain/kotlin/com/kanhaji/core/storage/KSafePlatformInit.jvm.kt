package com.kanhaji.core.storage

import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.KSafeMemoryPolicy

fun initKSafe() {
    KSafeProvider.init(
        prefs = KSafe(memoryPolicy = KSafeMemoryPolicy.PLAIN_TEXT) as KSafeWrapper,
        secure = KSafe(fileName = "secure", memoryPolicy = KSafeMemoryPolicy.ENCRYPTED) as KSafeWrapper
    )
}


