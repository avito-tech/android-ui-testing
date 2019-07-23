package com.avito.android.test

import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.PerformException
import com.avito.android.test.interceptor.ActionInterceptor
import com.avito.android.test.interceptor.AssertionInterceptor
import java.util.concurrent.TimeUnit

object UITestConfig {

    /**
     * global registry for [ActionInterceptor]'s
     */
    val actionInterceptors = mutableListOf<ActionInterceptor>()

    /**
     * global registry for [AssertionInterceptor]'s
     */
    val assertionInterceptors = mutableListOf<AssertionInterceptor>()

    /**
     * Changing this value will affect all subsequent actions/checks wait frequency
     */
    var waiterFrequencyMs: Long = 50L

    /**
     * Changing this value will affect all subsequent actions/checks wait timeout
     */
    var waiterTimeoutMs: Long = TimeUnit.SECONDS.toMillis(2)

    var activityLaunchTimeoutMilliseconds: Long = TimeUnit.SECONDS.toMillis(10)

    var openNotificationTimeoutMilliseconds: Long = TimeUnit.SECONDS.toMillis(30)

    /**
     * Exceptions to be waited for; any unregistered exceptions will be propagated
     */
    var waiterAllowedExceptions = setOf(
        PerformException::class.java,
        NoMatchingViewException::class.java,
        AssertionError::class.java,
        PerformException::class.java
    )
}
