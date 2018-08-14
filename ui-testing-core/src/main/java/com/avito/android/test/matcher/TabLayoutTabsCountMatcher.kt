package com.avito.android.test.matcher

import android.support.design.widget.TabLayout
import android.support.test.espresso.matcher.BoundedMatcher
import android.view.View
import org.hamcrest.Description

internal class TabLayoutTabsCountMatcher(private val count: Int) :
    BoundedMatcher<View, TabLayout>(TabLayout::class.java) {

    override fun describeTo(description: Description) {
        description.appendText("with tab layout tabs count: $count")
    }

    override fun matchesSafely(tabLayout: TabLayout): Boolean {
        return tabLayout.tabCount == count
    }
}
