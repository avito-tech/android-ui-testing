package com.avito.android.test.matcher

import android.support.test.espresso.matcher.BoundedMatcher
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher

class IsRefreshingMatcher(val matcher: Matcher<Boolean>) : BoundedMatcher<View, SwipeRefreshLayout>(SwipeRefreshLayout::class.java) {

    override fun describeTo(description: Description) {
        description.appendText(" SwipeRefreshLayout refreshing state ").appendDescriptionOf(matcher)
    }

    override fun matchesSafely(layout: SwipeRefreshLayout): Boolean {
        return matcher.matches(layout.isRefreshing)
    }

}