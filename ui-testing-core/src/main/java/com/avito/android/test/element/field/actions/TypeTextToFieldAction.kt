package com.avito.android.test.element.field.actions

import android.support.test.espresso.InjectEventSecurityException
import android.support.test.espresso.PerformException
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.TypeTextAction
import android.support.test.espresso.util.HumanReadables
import android.util.Log
import android.view.View
import com.avito.android.test.espresso.EspressoActions
import org.hamcrest.Matcher

/**
 * Full copy of [TypeTextAction] that
 * uses our version of clicks for putting field into focused state.
 */
internal class TypeTextToFieldAction(
    private val stringToBeTyped: String,
    private val tapToFocus: Boolean = true
) : ViewAction {

    private val originalAction: TypeTextAction = TypeTextAction(stringToBeTyped, tapToFocus)

    override fun getConstraints(): Matcher<View> = originalAction.constraints

    override fun perform(uiController: UiController, view: View) {
        if (stringToBeTyped.isEmpty()) {
            Log.w(TAG, "Supplied string is empty resulting in no-op (nothing is typed).")
            return
        }

        if (tapToFocus) {
            EspressoActions.click().perform(uiController, view)
            uiController.loopMainThreadUntilIdle()
        }

        try {
            if (!uiController.injectString(stringToBeTyped)) {
                Log.e(TAG, "Failed to type text: $stringToBeTyped")
                throw PerformException.Builder()
                    .withActionDescription(this.description)
                    .withViewDescription(HumanReadables.describe(view))
                    .withCause(RuntimeException("Failed to type text: $stringToBeTyped"))
                    .build()
            }
        } catch (e: InjectEventSecurityException) {
            Log.e(TAG, "Failed to type text: $stringToBeTyped")
            throw PerformException.Builder()
                .withActionDescription(this.description)
                .withViewDescription(HumanReadables.describe(view))
                .withCause(e)
                .build()
        }

    }

    override fun getDescription(): String = originalAction.description

    companion object {
        private val TAG = TypeTextToFieldAction::class.java.simpleName
    }
}