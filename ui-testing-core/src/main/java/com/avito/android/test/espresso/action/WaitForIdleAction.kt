package com.avito.android.test.espresso.action

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.view.View
import org.hamcrest.Matcher
import org.hamcrest.Matchers.any

class WaitForIdleAction : ViewAction {

    override fun getDescription() = "Loops the main thread until the application goes idle."

    override fun getConstraints(): Matcher<View> = any(View::class.java)

    override fun perform(uiController: UiController, view: View) {
        uiController.loopMainThreadUntilIdle()
    }
}