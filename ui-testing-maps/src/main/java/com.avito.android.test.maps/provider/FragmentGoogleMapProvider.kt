package com.avito.android.test.maps.provider

import android.support.annotation.IdRes
import android.support.test.internal.runner.junit4.statement.UiThreadStatement
import android.support.v7.app.AppCompatActivity
import com.avito.android.test.maps.GoogleMapWaitingException
import com.avito.android.test.util.getCurrentActivity
import com.avito.android.test.waitFor
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.SupportMapFragment

class FragmentGoogleMapProvider(
    @IdRes private val id: Int,
    private val timeoutMs: Long
) : GoogleMapProvider {

    override fun provide(): GoogleMap {
        val activity = getCurrentActivity() as? AppCompatActivity
                ?: throw RuntimeException(
                    "You should have app compat activity in foreground for communicating with map"
                )

        val mapFragment: MapFragment? =
            activity.supportFragmentManager.findFragmentById(id) as? MapFragment?
                    ?: activity.fragmentManager.findFragmentById(id) as? MapFragment?

        val mapSupportFragment: SupportMapFragment? =
            activity.supportFragmentManager.findFragmentById(id) as? SupportMapFragment?
                    ?: activity.fragmentManager.findFragmentById(id) as? SupportMapFragment?

        if (mapFragment == null && mapSupportFragment == null) {
            throw RuntimeException(
                "MapFragment should be on foreground activity for communication with map"
            )
        }

        var googleMap: GoogleMap? = null
        UiThreadStatement.runOnUiThread {
            mapFragment?.getMapAsync {
                googleMap = it
            }
            mapSupportFragment?.getMapAsync {
                googleMap = it
            }
        }

        waitFor(
            timeoutMs = timeoutMs,
            allowedExceptions = setOf(GoogleMapWaitingException::class.java)
        ) {
            if (googleMap == null) {
                throw GoogleMapWaitingException(
                    "Error during getting GoogleMap instance from MapFragment"
                )
            }
        }

        return googleMap!!
    }
}