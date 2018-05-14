package com.avito.android.test

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.OngoingStubbing
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.espresso.intent.matcher.UriMatchers
import org.hamcrest.Matcher
import org.hamcrest.Matchers

object Intents {

    fun resultOK(resultData: Intent? = null) =
        Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)

    fun resultCanceled(resultData: Intent? = null) =
        Instrumentation.ActivityResult(Activity.RESULT_CANCELED, resultData)

    inline fun <reified T : Activity> whenIntended(): OngoingStubbing =
        Intents.intending(IntentMatchers.hasComponent(T::class.qualifiedName))

    fun stubEverything() =
        Intents.intending(IntentMatchers.anyIntent()).respondWith(resultCanceled())

    val checks = Checks()

    class Checks {

        fun actionIntended(action: String, vararg additionalMatchers: Matcher<Intent>) {
            Intents.intended(Matchers.allOf(IntentMatchers.hasAction(action), *additionalMatchers))
        }

        fun actionIntended(action: String, key: String, value: String) {
            actionIntended(action, IntentMatchers.hasExtra(key, value))
        }

        fun actionIntendedWithUriParam(action: String, param: String, value: String) {
            actionIntended(
                action,
                IntentMatchers.hasData(UriMatchers.hasParamWithValue(param, value))
            )
        }

        fun activityIntended(className: String) {
            Intents.intended(IntentMatchers.hasComponent(className))
        }

        inline fun <reified T : Activity> activityIntendedWithUriParam(
            param: String,
            value: String
        ) {
            activityIntended<T>(IntentMatchers.hasData(UriMatchers.hasParamWithValue(param, value)))
        }

        inline fun <reified T : Activity> activityIntended(vararg additionalMatchers: Matcher<Intent>) {
            Intents.intended(
                Matchers.allOf(
                    IntentMatchers.hasComponent(T::class.java.name),
                    *additionalMatchers
                )
            )
        }

        inline fun <reified T : Activity> activityIntendedWithoutExtraParam(param: String) {
            activityIntended<T>(Matchers.not(IntentMatchers.hasExtraWithKey(param)))
        }

        inline fun <reified T : Activity> activityIntendedWithExtraParam(
            param: String,
            value: String
        ) {
            activityIntended<T>(IntentMatchers.hasExtra(param, value))
        }
    }
}