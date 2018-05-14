package com.avito.android.ui.test

import android.app.Activity
import android.support.test.espresso.contrib.ActivityResultMatchers
import com.avito.android.test.Device
import com.avito.android.ui.StartForResultActivity
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class RotationFailuresTest {

    @get:Rule
    val rule = screenRule<StartForResultActivity>(launchActivity = true)

    @Test
    fun activity_is_not_finishing_on_finish_call_after_rotation() {
        Device.rotate()

        Screen.startForResultScreen.view.click()

        Assert.assertFalse(rule.activity.isFinishing)
    }

    @Test(expected = IllegalStateException::class)
    fun impossible_to_get_result_after_rotation() {
        Device.rotate()

        Screen.startForResultScreen.view.click()

        Assert.assertThat(
            rule.activityResult,
            ActivityResultMatchers.hasResultCode(Activity.RESULT_OK)
        )
    }
}