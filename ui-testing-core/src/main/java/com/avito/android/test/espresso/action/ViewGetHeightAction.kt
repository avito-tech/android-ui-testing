package com.avito.android.test.espresso.action

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.view.View
import org.hamcrest.Matcher

class ViewGetHeightAction : ViewAction {

    var height: Int = Int.MIN_VALUE
        get() {
            if (field == Int.MIN_VALUE) {
                throw UninitializedPropertyAccessException()
            }
            return field
        }
        private set

    override fun getDescription() = "getting view height"

    override fun getConstraints(): Matcher<View> = isAssignableFrom(View::class.java)

    override fun perform(uiController: UiController, view: View) {
        height = view.height
    }
}