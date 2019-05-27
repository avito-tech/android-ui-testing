package com.avito.android.test.util

import android.annotation.TargetApi
import android.content.ContentResolver
import android.content.Context
import android.os.Build
import android.provider.Settings
import junit.framework.Assert.assertTrue

fun verifyAnimationsDisabled(context: Context) {
    val resolver = context.contentResolver

    try {
        val isTransitionAnimationDisabled = isZero(getTransitionAnimationScale(resolver))
        val isWindowAnimationDisabled = isZero(getWindowAnimationScale(resolver))
        val isAnimatorDisabled = isZero(getAnimatorDurationScale(resolver))

        assertTrue(isTransitionAnimationDisabled)
        assertTrue(isWindowAnimationDisabled)
        assertTrue(isAnimatorDisabled)
    } catch (t: Throwable) {
        throw RuntimeException(
            "You can't run instrumentation tests on device with enabled animations." +
                "You should disable it in settings (Settings -> Developer options)."
        )
    }
}

private fun isZero(value: Float): Boolean {
    return java.lang.Float.compare(Math.abs(value), 0.0f) == 0
}

@Suppress("DEPRECATION")
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
private fun getTransitionAnimationScale(resolver: ContentResolver): Float {
    return getSetting(
        resolver,
        Settings.Global.TRANSITION_ANIMATION_SCALE,
        Settings.System.TRANSITION_ANIMATION_SCALE
    )
}

@Suppress("DEPRECATION")
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
private fun getWindowAnimationScale(resolver: ContentResolver): Float {
    return getSetting(
        resolver,
        Settings.Global.WINDOW_ANIMATION_SCALE,
        Settings.System.WINDOW_ANIMATION_SCALE
    )
}

@Suppress("DEPRECATION")
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
private fun getAnimatorDurationScale(resolver: ContentResolver): Float {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        getSetting(
            resolver,
            Settings.Global.ANIMATOR_DURATION_SCALE,
            Settings.System.ANIMATOR_DURATION_SCALE
        )
    } else 0f
}

private fun getSetting(resolver: ContentResolver, current: String, deprecated: String): Float {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        Settings.Global.getFloat(resolver, current)
    } else {
        Settings.System.getFloat(resolver, deprecated)
    }
}
