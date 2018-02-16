package com.avito.android.test.maps

import android.support.annotation.IdRes
import com.avito.android.test.maps.provider.FragmentGoogleMapProvider
import com.avito.android.test.maps.provider.GoogleMapProvider
import com.avito.android.test.page_object.PageObject
import java.util.concurrent.TimeUnit

class GoogleMapFragmentElement(
    @IdRes private val id: Int,
    private val mapProvider: GoogleMapProvider = FragmentGoogleMapProvider(id = id, timeoutMs = CHECKS_TIMEOUT_MS)
) : PageObject(), GoogleMapActions by GoogleMapActionsImpl(timeoutMs = CHECKS_TIMEOUT_MS, mapProvider = mapProvider) {

    val checks: GoogleMapChecks = GoogleMapChecksImpl(timeoutMs = CHECKS_TIMEOUT_MS, mapProvider = mapProvider)
}

private val CHECKS_TIMEOUT_MS = TimeUnit.SECONDS.toMillis(10)