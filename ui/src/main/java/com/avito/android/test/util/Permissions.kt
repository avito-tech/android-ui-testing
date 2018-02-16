package com.avito.android.test.util

import android.os.Build
import android.support.test.InstrumentationRegistry

fun grantPermission(permissionName: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val command = StringBuilder()
            .append("pm grant ")
            .append(InstrumentationRegistry.getTargetContext().packageName)
            .append(" ")
            .append(permissionName)
            .toString()

        InstrumentationRegistry.getInstrumentation().uiAutomation.executeShellCommand(command)
    }
}