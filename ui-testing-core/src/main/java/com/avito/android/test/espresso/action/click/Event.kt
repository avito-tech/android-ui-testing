package com.avito.android.test.espresso.action.click

import android.os.SystemClock
import android.view.InputDevice
import android.view.MotionEvent

internal fun obtainEvent(coordinates: FloatArray, precision: FloatArray, event: Int) =
    MotionEvent.obtain(
        SystemClock.uptimeMillis(),
        SystemClock.uptimeMillis(),
        event,
        coordinates[0],
        coordinates[1],
        NORMAL_PRESSURE,
        NORMAL_SIZE,
        WITHOUT_MODIFIERS_META_STATE,
        precision[0],
        precision[1],
        InputDevice.SOURCE_UNKNOWN,
        0
    )

private const val NORMAL_PRESSURE = 1F
private const val NORMAL_SIZE = 1F
private const val WITHOUT_MODIFIERS_META_STATE = 0
