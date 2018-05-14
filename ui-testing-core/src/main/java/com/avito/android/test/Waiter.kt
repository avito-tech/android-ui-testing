package com.avito.android.test

import android.support.test.espresso.DataInteraction
import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.PerformException
import android.support.test.espresso.ViewAction
import android.support.test.espresso.ViewAssertion
import android.support.test.espresso.ViewInteraction
import java.util.concurrent.TimeUnit

/**
 * Changing this value will affect all subsequent actions/checks wait frequency
 */
var waiterFrequencyMs: Long = 50L

/**
 * Changing this value will affect all subsequent actions/checks wait timeout
 */
var waiterTimeoutMs: Long = TimeUnit.SECONDS.toMillis(2)

/**
 * Exceptions to be waited for; any unregistered exceptions will be propagated
 */
var waiterAllowedExceptions = setOf(
    PerformException::class.java,
    NoMatchingViewException::class.java,
    AssertionError::class.java,
    PerformException::class.java
)

/**
 * This method also exposed to the client, for hacking purposes
 */
fun waitFor(
    frequencyMs: Long = waiterFrequencyMs,
    timeoutMs: Long = waiterTimeoutMs,
    allowedExceptions: Set<Class<out Throwable>> = waiterAllowedExceptions,
    action: () -> Unit
) {
    var timer = 0L
    var caughtAllowedException: Throwable

    val startTime = System.currentTimeMillis()

    do {
        try {
            action.invoke()
            return
        } catch (e: Throwable) {
            val isExceptionAllowed =
                allowedExceptions.find { it.isAssignableFrom(e.javaClass) } != null
            when {
                isExceptionAllowed -> {
                    Thread.sleep(frequencyMs)
                    timer += frequencyMs
                    caughtAllowedException = e
                }
                else -> throw e
            }
        }
    } while (timer <= timeoutMs && System.currentTimeMillis() - startTime <= timeoutMs)

    throw caughtAllowedException
}

/**
 * Use this extension instead of ViewInteraction.check() all over the place
 * if you have problems with IdlingResources configuration
 */
fun ViewInteraction.waitForCheck(assertion: ViewAssertion) = waitFor { check(assertion) }

/**
 * Use this extension instead of ViewInteraction.perform() all over the place
 * if you have problems with IdlingResources configuration
 */
fun ViewInteraction.waitToPerform(vararg action: ViewAction) = waitFor { perform(*action) }

/**
 * Use this extension instead of DataInteraction.check() all over the place
 * if you have problems with IdlingResources configuration
 */
fun DataInteraction.waitForCheck(assertion: ViewAssertion) = waitFor { check(assertion) }

/**
 * Use this extension instead of DataInteraction.perform() all over the place
 * if you have problems with IdlingResources configuration
 */
fun DataInteraction.waitToPerform(vararg action: ViewAction) = waitFor { perform(*action) }