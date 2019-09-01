package com.avito.android.ui.test

import androidx.test.espresso.PerformException
import com.avito.android.ui.ButtonsActivity
import org.junit.Rule
import org.junit.Test

class ButtonsTest {

    @get:Rule
    val rule = screenRule<ButtonsActivity>(launchActivity = true)

    @Test
    fun clickOnEnabledButton_performed() {
        Screen.buttons.enabledButton.click()

        Screen.buttons.enabledButtonClickIndicatorView.checks.isDisplayed()
        Screen.buttons.enabledButtonLongClickIndicatorView.checks.isNotDisplayed()
    }

    @Test
    fun longClickOnEnabledButton_performed() {
        Screen.buttons.enabledButton.longClick()

        Screen.buttons.enabledButtonClickIndicatorView.checks.isNotDisplayed()
        Screen.buttons.enabledButtonLongClickIndicatorView.checks.isDisplayed()
    }

    @Test(expected = PerformException::class)
    fun clickOnDisabledButton_mustThrowsPerformException() {
        Screen.buttons.disabledButton.click()
    }

    @Test(expected = PerformException::class)
    fun longClickOnDisabledButton_mustThrowsPerformException() {
        Screen.buttons.disabledButton.longClick()
    }
}