package com.kanhaji.core.storage

import android.content.Context
import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.KSafeMemoryPolicy

fun initKSafe(context: Context) {
    KSafeProvider.init(
        prefs = KSafe(context, memoryPolicy = KSafeMemoryPolicy.PLAIN_TEXT) as KSafeWrapper,
        secure = KSafe(
            context,
            fileName = "secure",
            memoryPolicy = KSafeMemoryPolicy.ENCRYPTED
        ) as KSafeWrapper
    )
}

