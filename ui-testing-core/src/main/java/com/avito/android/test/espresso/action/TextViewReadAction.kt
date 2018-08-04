package com.avito.android.test.espresso.action

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers
import android.view.View
import android.widget.TextView
import com.avito.android.test.waitFor
import junit.framework.Assert
import org.hamcrest.Matcher

class TextViewReadAction(private val allowBlank: Boolean) : ViewAction {

    lateinit var result: String
        private set

    override fun getConstraints(): Matcher<View> =
        ViewMatchers.isAssignableFrom(TextView::class.java)

    override fun getDescription(): String = "getting text from a TextView"

    override fun perform(uiController: UiController, view: View) {
        if (allowBlank) {
            result = view.readText()
        } else {
            waitFor {
                result = view.readText()
                Assert.assertTrue(
                    "read() waited, but view.text still has empty string value; " +
                            "use read(allowBlank=true) if you really need it",
                    result.isNotBlank()
                )
            }
        }
    }

    private fun View.readText(): String = (this as TextView).text.toString()
}
