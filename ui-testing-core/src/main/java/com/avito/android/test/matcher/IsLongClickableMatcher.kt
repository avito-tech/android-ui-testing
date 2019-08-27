package com.avito.android.test.matcher

import android.view.View
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class IsLongClickableMatcher : TypeSafeMatcher<View>() {

    override fun describeTo(description: Description) {
        description.appendText("is long-clickable")
    }

    override fun matchesSafely(view: View) = view.isLongClickable
}