package com.avito.android.test

import android.support.test.espresso.DataInteraction
import android.support.test.espresso.ViewAction
import android.support.test.espresso.ViewAssertion
import android.support.test.espresso.ViewInteraction

/**
 * This method also exposed to the client, for hacking purposes
 */
fun <T> waitFor(
    frequencyMs: Long = UITestConfig.waiterFrequencyMs,
    timeoutMs: Long = UITestConfig.waiterTimeoutMs,
    allowedExceptions: Set<Class<out Throwable>> = UITestConfig.waiterAllowedExceptions,
    sleepAction: (frequencyMs: Long) -> Unit = { Thread.sleep(it) },
    action: () -> T
): T {
    var timer = 0L
    var caughtAllowedException: Throwable

    val startTime = System.currentTimeMillis()

    do {
        try {
            return action.invoke()
        } catch (e: Throwable) {
            val isExceptionAllowed =
                allowedExceptions.find { it.isAssignableFrom(e.javaClass) } != null
            when {
                isExceptionAllowed -> {
                    sleepAction(frequencyMs)
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
 * Use this extension instead of ViewInteraction.perform() all over the place
 * if you have problems with IdlingResources configuration
 */
fun ViewInteraction.waitToPerform(actions: List<ViewAction>) =
    waitFor { perform(*actions.toTypedArray()) }

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
