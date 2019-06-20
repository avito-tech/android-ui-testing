package com.avito.android.ui.test.matcher

import android.app.Instrumentation
import android.content.Intent

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object ActivityResultMatchers {

    fun hasResultData(intentMatcher: Matcher<Intent>): Matcher<in Instrumentation.ActivityResult> {
        return object : TypeSafeMatcher<Instrumentation.ActivityResult>(Instrumentation.ActivityResult::class.java) {

            override fun describeTo(description: Description) {
                description.appendDescriptionOf(intentMatcher)
            }

            override fun matchesSafely(item: Instrumentation.ActivityResult): Boolean {
                return intentMatcher.matches(item.resultData)
            }

            override fun describeMismatchSafely(item: Instrumentation.ActivityResult, mismatchDescription: Description) {
                intentMatcher.describeMismatch(item.resultData, mismatchDescription)
            }
        }
    }

    fun hasResultCode(resultCode: Int): Matcher<in Instrumentation.ActivityResult> {
        return object : TypeSafeMatcher<Instrumentation.ActivityResult>(Instrumentation.ActivityResult::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has result code $resultCode")
            }

            override fun matchesSafely(activityResult: Instrumentation.ActivityResult): Boolean {
                return activityResult.resultCode == resultCode
            }
        }
    }
}