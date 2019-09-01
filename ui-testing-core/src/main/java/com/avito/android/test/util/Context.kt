package com.avito.android.test.util

import android.content.Context
import androidx.test.InstrumentationRegistry
import java.util.Locale

fun getContextWithLocaleByLanguage(language: String): Context {
    val baseContext = InstrumentationRegistry.getTargetContext()
    val configuration = baseContext.resources.configuration
    configuration.setLocale(Locale(language))
    return baseContext.createConfigurationContext(configuration)
}
