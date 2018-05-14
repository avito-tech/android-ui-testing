package com.avito.android.test.util

import android.content.Context
import android.support.test.InstrumentationRegistry
import java.util.*

fun getContextWithLocaleByLanguage(language: String): Context {
    val baseContext = InstrumentationRegistry.getTargetContext()
    val configuration = baseContext.resources.configuration
    configuration.setLocale(Locale(language))
    return baseContext.createConfigurationContext(configuration)
}
