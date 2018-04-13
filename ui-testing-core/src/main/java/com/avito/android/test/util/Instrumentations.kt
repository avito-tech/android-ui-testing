package com.avito.android.test.util

import android.app.Activity
import android.app.Instrumentation
import android.os.Looper
import android.support.test.InstrumentationRegistry
import android.support.test.internal.runner.junit4.statement.UiThreadStatement
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import android.support.test.runner.lifecycle.Stage
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.Assert

fun getCurrentActivity(): Activity {
    var currentActivity: Activity? = null
    val findResumedActivity = {
        val resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
        if (resumedActivities.iterator().hasNext()) {
            currentActivity = resumedActivities.iterator().next()
        }
    }
    if (isMainThread()) {
        findResumedActivity()
    } else {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(findResumedActivity)
    }
    return currentActivity ?: throw IllegalStateException("Resumed activity not found")
}

inline fun <reified T : Activity> assertCurrentActivity() {
    UiThreadStatement.runOnUiThread {
        Assert.assertThat(getCurrentActivity(), instanceOf(T::class.java))
    }
}

/**
 * Call it to pass message: "Test running failed: Instrumentation run failed due to '$message'"
 * TODO not working with TestOrchestrator
 */
fun Instrumentation.failInstrumentation(message: String) {
    error(message)
}

fun isMainThread() = Looper.myLooper() == Looper.getMainLooper()