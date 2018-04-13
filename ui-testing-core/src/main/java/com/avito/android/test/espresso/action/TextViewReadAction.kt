package com.avito.android.test.espresso.action

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers
import android.view.View
import android.widget.TextView
import org.hamcrest.Matcher

class TextViewReadAction : ViewAction {

    lateinit var result: String
        private set

    override fun getConstraints(): Matcher<View> = ViewMatchers.isAssignableFrom(TextView::class.java)

    override fun getDescription(): String = "getting text from a TextView"

    override fun perform(uiController: UiController, view: View) {
        result = (view as TextView).text.toString()
    }
}