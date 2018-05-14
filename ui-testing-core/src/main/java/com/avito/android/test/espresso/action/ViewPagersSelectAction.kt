package com.avito.android.test.espresso.action

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers
import android.support.v4.view.ViewPager
import android.view.View
import org.hamcrest.Matcher

class ViewPagersSelectAction(private val tabPosition: Int) : ViewAction {

    override fun getDescription() = "selecting ViewPager"

    override fun getConstraints(): Matcher<View> =
        ViewMatchers.isAssignableFrom(ViewPager::class.java)

    override fun perform(uiController: UiController, view: View) {
        val viewPager = view as ViewPager
        viewPager.currentItem = tabPosition
        uiController.loopMainThreadUntilIdle()
    }
}