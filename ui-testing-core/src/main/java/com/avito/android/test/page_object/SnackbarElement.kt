package com.avito.android.test.page_object

import android.support.design.widget.Snackbar
import android.support.test.espresso.action.SwipeDirections.RIGHT_TO_LEFT
import android.support.test.espresso.matcher.ViewMatchers
import com.avito.android.test.espresso.EspressoActions
import org.hamcrest.Matcher
import org.hamcrest.Matchers

/**
 * Snackbar https://developer.android.com/reference/android/support/design/widget/Snackbar.html
 *
 * @param textMatcher optional matcher to handle 2+ snackbars on screen conflicts
 * (use part of message to work with specific snackbar)
 *
 * @warning hidden snackbar stays in hierarchy longer than you might expect;
 * if you encounter weird "duplicate snackbars" it's known issue
 */
class SnackbarElement(textMatcher: Matcher<String>? = null) : ViewElement(
    if (textMatcher != null) {
        Matchers.allOf(
            ViewMatchers.isAssignableFrom(Snackbar.SnackbarLayout::class.java),
            ViewMatchers.hasDescendant(ViewMatchers.withText(textMatcher))
        )
    } else {
        ViewMatchers.isAssignableFrom(Snackbar.SnackbarLayout::class.java)
    }
) {

    val message = ViewElement(
        Matchers.allOf(
            ViewMatchers.withId(android.support.design.R.id.snackbar_text),
            ViewMatchers.isDescendantOfA(matcher)
        )
    )

    val button = ViewElement(
        Matchers.allOf(
            ViewMatchers.withId(android.support.design.R.id.snackbar_action),
            ViewMatchers.isDescendantOfA(matcher)
        )
    )

    fun swipeOut() = EspressoActions.swipe(RIGHT_TO_LEFT)
}
