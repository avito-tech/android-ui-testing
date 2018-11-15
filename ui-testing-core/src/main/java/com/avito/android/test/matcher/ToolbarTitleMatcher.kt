package com.avito.android.test.matcher

import android.support.test.espresso.matcher.BoundedMatcher
import android.support.v7.widget.Toolbar
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher

/**
 * from http://blog.sqisland.com/2015/05/espresso-match-toolbar-title.html
 */
internal class ToolbarTitleMatcher(private val textMatcher: Matcher<out CharSequence>) :
        BoundedMatcher<View, Toolbar>(Toolbar::class.java) {

    private var actualText: CharSequence? = null

    override fun describeTo(description: Description) {
        description.appendText("with toolbar title: ")
        textMatcher.describeTo(description)
        when {
            actualText != null -> description.appendText(" actualText = $actualText")
        }
    }

    override fun matchesSafely(toolbar: Toolbar): Boolean {
        actualText = toolbar.title
        return textMatcher.matches(actualText)
    }
}
