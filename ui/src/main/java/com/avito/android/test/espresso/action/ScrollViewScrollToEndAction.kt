package com.avito.android.test.espresso.action

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers
import android.view.View
import android.widget.ScrollView
import org.hamcrest.Matcher

class ScrollViewScrollToEndAction : ViewAction {

    override fun getDescription() = "scroll to end of ScrollView"

    override fun getConstraints(): Matcher<View> = ViewMatchers.isAssignableFrom(ScrollView::class.java)

    override fun perform(uiController: UiController, view: View) {
        val scrollView = view as ScrollView
        scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        uiController.loopMainThreadUntilIdle()
    }

}