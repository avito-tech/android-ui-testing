package com.avito.android.test.action

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers
import android.view.View
import org.hamcrest.Matcher
import org.hamcrest.Matchers

class ActionOnEnabledElement(
    private val action: ViewAction
) : ViewAction {

    override fun getDescription(): String = "Action: ${action.description} on enabled element"

    override fun getConstraints(): Matcher<View> = Matchers.allOf(
        ViewMatchers.isEnabled(),
        action.constraints
    )

    override fun perform(uiController: UiController, view: View) {
        action.perform(uiController, view)
    }
}