package com.avito.android.ui.test

import com.avito.android.ui.RecyclerAsLayoutActivity
import org.junit.Rule
import org.junit.Test

class RecyclerAsLayoutTest {

    @get:Rule
    val rule = screenRule<RecyclerAsLayoutActivity>()

    @Test
    fun label_isAccessible() {
        rule.launchActivity(
            RecyclerAsLayoutActivity.intent(
                arrayListOf(
                    "input",
                    "editText",
                    "label"
                )
            )
        )

        Screen.recyclerAsLayout.label.checks.withText("label")
    }

    @Test
    fun editText_isAccessible() {
        rule.launchActivity(
            RecyclerAsLayoutActivity.intent(
                arrayListOf(
                    "input",
                    "editText",
                    "label"
                )
            )
        )

        Screen.recyclerAsLayout.editText.checks.withHintText("editText")
    }

    @Test
    fun textInput_isAccessible() {
        rule.launchActivity(
            RecyclerAsLayoutActivity.intent(
                arrayListOf(
                    "input",
                    "editText",
                    "label"
                )
            )
        )

        Screen.recyclerAsLayout.inputField.checks.withHintText("input")
    }
}
