package com.avito.android.test.espresso.action

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ReplaceTextAction
import android.support.test.espresso.action.TypeTextAction
import android.view.View
import java.lang.Character.UnicodeBlock
import org.hamcrest.Matcher
import org.hamcrest.Matchers

/**
 * Don't use directly, only via EspressoActions
 */
class SafeTypeTextAction(
    private val stringToBeTyped: String,
    tapBeforeInput: Boolean = true,
    private var doNotUseReplace: Boolean = false
) : ViewAction {

    private val typeAction by lazy { TypeTextAction(stringToBeTyped, tapBeforeInput) }
    private val replaceAction by lazy { ReplaceTextAction(stringToBeTyped) }

    override fun getDescription(): String = typeAction.description

    override fun getConstraints(): Matcher<View> = Matchers.allOf(typeAction.constraints)

    /**
     * If [doNotUseReplace] is not specified, and string to be typed seems not to be a phone number or a cyrillic text
     * (see AP-221), then fast [ReplaceTextAction] will be executed prior to [TypeTextAction]. Otherwise will be used
     * [TypeTextAction].
     */
    override fun perform(uiController: UiController?, view: View?) {
        doNotUseReplace = doNotUseReplace ||
            // phone numbers will not work properly with replace in most cases
            stringToBeTyped.isPhoneNumber() ||
            // AP-221: cyrillic input will not work correctly with replace in most cases
            stringToBeTyped.isCyrillic()
        try {
            if (doNotUseReplace) {
                typeAction.perform(uiController, view)
                return
            }
        } catch (e: RuntimeException) {
            // do nothing here - fallback is below
        }
        try {
            replaceAction.perform(uiController, view)
        } catch (e: RuntimeException) {
            throw ReplaceTextActionException(e)
        }
    }

    class ReplaceTextActionException(cause: Throwable) : RuntimeException(
        """
        |It seems, that text input with text replace action is not working correctly here.
        |Please, disable it in your page object explicitly, appending to the element something like
        |".apply { editText.doNotUseReplace = true }"""".trimMargin(), cause
    )

    private fun String.isCyrillic() =
        (0 until this.length).any { UnicodeBlock.of(this[it]) == UnicodeBlock.CYRILLIC }

    private fun String.isPhoneNumber() = this.matches(Regex("^\\+?[0-9. ()-]{1,25}$"))
}
