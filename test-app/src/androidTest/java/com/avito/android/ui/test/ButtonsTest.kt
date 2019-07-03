package com.avito.android.ui.test

import android.support.test.espresso.PerformException
import com.avito.android.ui.ButtonsActivity
import org.junit.Rule
import org.junit.Test

class ButtonsTest {

    @get:Rule
    val rule = screenRule<ButtonsActivity>(launchActivity = true)

    @Test(expected = PerformException::class)
    fun clickOnDisabledButton_mustThrowsPerformException() {
        Screen.buttons.disabledButton.click()
    }

    @Test(expected = PerformException::class)
    fun longClickOnDisabledButton_mustThrowsPerformException() {
        Screen.buttons.disabledButton.longClick()
    }
}