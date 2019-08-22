package com.avito.android.test.element.field.actions

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.avito.android.test.espresso.EspressoActions
import com.avito.android.test.util.executeMethod
import com.avito.android.test.util.getFieldByReflectionWithAnyField
import com.avito.android.test.waitFor
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.Assert
import java.util.concurrent.TimeUnit

/**
 * Type text action that is used for typing instead of [android.support.test.espresso.action.TypeTextAction]
 *
 * Differences:
 *  - We wait for focus on field
 *  - We use InputConnection API (for software keyboards) instead of injecting key events to
 *    (window or input managers)
 *
 * Why keyboard API is better?
 *  - We can apply any symbol as text instead of injecting low level key events. It's hard to apply,
 *    for example, cyrillic symbols using key events API
 *  - Real user most often uses software keyboards
 */
internal class TypeText(private val stringToBeTyped: String) : ViewAction {

    override fun getConstraints(): Matcher<View> = Matchers.allOf(
        ViewMatchers.isDisplayed(),
        ViewMatchers.isAssignableFrom(EditText::class.java)
    )

    override fun perform(uiController: UiController, view: View) {
        view as EditText

        tapForFocus(uiController = uiController, editText = view)

        val context = (
            InstrumentationRegistry
                .getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            )
            .getFieldByReflectionWithAnyField("mIInputContext")

        context.executeMethod("beginBatchEdit")
        context.executeMethod("finishComposingText")
        context.executeMethod("commitText", stringToBeTyped, 1)
        context.executeMethod("endBatchEdit")

        uiController.loopMainThreadUntilIdle()
    }

    private fun tapForFocus(uiController: UiController, editText: EditText) {
        EspressoActions.click().perform(uiController, editText)
        uiController.loopMainThreadUntilIdle()

        waitMainLoopFor(uiController) {
            Assert.assertThat(
                "View must have focus after tap before text typing",
                editText.hasFocus(),
                Matchers.`is`(true)
            )
        }
    }

    private fun waitMainLoopFor(uiController: UiController, action: () -> Unit) = waitFor(
        frequencyMs = 100,
        timeoutMs = TimeUnit.SECONDS.toMillis(5),
        allowedExceptions = setOf(Throwable::class.java),
        sleepAction = { delay -> uiController.loopMainThreadForAtLeast(delay) },
        action = action
    )

    override fun getDescription(): String = "type text $stringToBeTyped"
}