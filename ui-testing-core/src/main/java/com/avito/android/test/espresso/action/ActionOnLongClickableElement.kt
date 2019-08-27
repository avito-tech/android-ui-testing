package com.avito.android.test.espresso.action

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.view.View
import com.avito.android.test.matcher.IsLongClickableMatcher
import org.hamcrest.Matcher
import org.hamcrest.Matchers

class ActionOnLongClickableElement(
    private val action: ViewAction
) : ViewAction {

    override fun getDescription(): String = "${action.description} on long-clickable element"

    override fun getConstraints(): Matcher<View> = Matchers.allOf(
        IsLongClickableMatcher(),
        action.constraints
    )

    override fun perform(uiController: UiController, view: View) {
        action.perform(uiController, view)
    }
}