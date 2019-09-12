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

    @Test(expected = PerformException::class)
    fun clickOnNonClickableButton_mustThrowsPerformException() {
        Screen.buttons.nonClickableButton.click()
    }

    @Test
    fun clickInsideClickableContainer_performs() {
        Screen.buttons.clickableContainerInnerButton.checks.isNotClickable()
        Screen.buttons.clickableContainerInnerButton.click()

        Screen.buttons.clickableContainerIndicator.checks.isVisible()
    }

    @Test
    fun clickOnNonLongClickableButton_performed() {
        Screen.buttons.nonLongClickableButton.checks.isClickable()
        Screen.buttons.nonLongClickableButton.click()

        Screen.buttons.nonLongClickableButtonIndicator.checks.isDisplayed()
    }

    @Test(expected = PerformException::class)
    fun longClickOnNonLongClickableButton_mustThrowsPerformException() {
        Screen.buttons.nonLongClickableButton.longClick()
    }

    @Test
    fun longClickInsideLongClickableContainer_performs() {
        Screen.buttons.longClickableContainerInnerButton.longClick()

        Screen.buttons.longClickableContainerIndicator.checks.isVisible()
    }
}