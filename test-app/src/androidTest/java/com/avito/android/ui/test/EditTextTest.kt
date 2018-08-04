package com.avito.android.ui.test

import android.widget.EditText
import com.avito.android.test.Device
import com.avito.android.ui.EditTextActivity
import com.avito.android.ui.R
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class EditTextTest {

    @get:Rule
    val rule = screenRule<EditTextActivity>()

    @Ignore("Not working in firebase")
    @Test
    fun findsKeyboard_whenKeyboardIsOpenedByActivityManifest() = with(rule) {
        launchActivity(null)

        Device.keyboard.checks.isDisplayed(activity)
    }

    @Test
    fun writesLongText() = with(rule) {
        launchActivity(null)

        rule.runOnUiThread {
            activity.findViewById<EditText>(R.id.edit_text).setText("text")
        }

        Screen.editTextScreen.editText.write("1000000000000000000000000")
        Screen.editTextScreen.editText.checks.displayedWithText("1000000000000000000000000")
    }
}
