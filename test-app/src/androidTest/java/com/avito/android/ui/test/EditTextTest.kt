package com.avito.android.ui.test

import com.avito.android.test.Device
import com.avito.android.ui.EditTextActivity
import org.junit.Rule
import org.junit.Test

class EditTextTest {

    @get:Rule
    val rule = screenRule<EditTextActivity>()

    @Test
    fun findsKeyboard_whenKeyboardIsOpenedByActivityManifest() = with(rule) {
        launchActivity(null)

        Device.keyboard.checks.isDisplayed(activity)
    }

    @Test
    fun writesToInputWithFormatting() = with(rule) {
        launchActivity(null)

        Screen.editTextScreen.phoneNumberText.write("9261418698")
        Screen.editTextScreen.phoneNumberText.checks.displayedWithText("(926) 141-8698")
    }

    @Test
    fun writesCyrillicText() = with(rule) {
        launchActivity(null)

        Screen.editTextScreen.editText1.write("Кирилл и Мефодий не изобретали кириллицу")
        Screen.editTextScreen.editText1.checks.displayedWithText("Кирилл и Мефодий не изобретали кириллицу")
    }

    @Test
    fun writesLongText() = with(rule) {
        launchActivity(null)

        val superLongText = StringBuilder()
            .apply {
                (0..100000)
                    .forEach { _ ->
                        append("a")
                    }
            }
            .toString()

        Screen.editTextScreen.editText.write(superLongText)
        Screen.editTextScreen.editText.checks.displayedWithText(superLongText)
    }

    @Test
    fun writesEmptyText() = with(rule) {
        launchActivity(null)

        Screen.editTextScreen.editText.write("")
        Screen.editTextScreen.editText.checks.displayedWithText("")
    }
}
