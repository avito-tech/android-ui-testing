package com.avito.android.ui.test

import com.avito.android.test.Device
import com.avito.android.ui.EditTextActivity
import org.junit.Rule
import org.junit.Test


class EditTextTest {
    @Rule @JvmField
    val rule = screenRule<EditTextActivity>()

    @Test
    fun findsKeyboard_whenKeyboardIsOpenedByActivityManifest() = with(rule) {
        launchActivity(null)

        Device.keyboard.checks.isDisplayed(activity)
    }
}