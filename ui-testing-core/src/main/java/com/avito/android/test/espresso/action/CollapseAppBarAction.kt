package com.avito.android.test.espresso.action

import android.support.design.widget.AppBarLayout
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers
import android.view.View
import org.hamcrest.Matcher

class CollapseAppBarAction : ViewAction {

    override fun getDescription() = "collapsing AppBar"

    override fun getConstraints(): Matcher<View> =
        ViewMatchers.isAssignableFrom(AppBarLayout::class.java)

    override fun perform(uiController: UiController, view: View) {
        val appBarLayout = view as AppBarLayout
        appBarLayout.setExpanded(false)
        uiController.loopMainThreadUntilIdle()
    }
}