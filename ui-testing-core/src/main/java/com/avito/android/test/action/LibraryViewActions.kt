package com.avito.android.test.action

import android.support.test.espresso.PerformException
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions
import android.view.View
import com.avito.android.test.UITestConfig
import org.hamcrest.Matcher
import org.hamcrest.core.IsAnything

internal object LibraryViewActions {

    fun click(policy: UITestConfig.ClickRollbackPolicy = UITestConfig.clickRollbackPolicy): ViewAction = when (policy) {
        is UITestConfig.ClickRollbackPolicy.DoNothing -> ActionOnEnabledElement(
            ViewActions.click()
        )

        is UITestConfig.ClickRollbackPolicy.TryOneMoreClick -> ActionOnEnabledElement(
            ViewActions.click(
                ActionOnEnabledElement(ViewActions.click())
            )
        )

        is UITestConfig.ClickRollbackPolicy.Fail -> ActionOnEnabledElement(
            ViewActions.click(
                object : ViewAction {
                    override fun getDescription(): String = "fake fail action after click interpreted as long click"

                    override fun getConstraints(): Matcher<View> = IsAnything()

                    override fun perform(uiController: UiController?, view: View?) {
                        throw PerformException.Builder()
                            .withActionDescription("click interpreted as long click")
                            .withViewDescription("view")
                            .build()
                    }

                }
            )
        )
    }

    fun longClick(): ViewAction = ActionOnEnabledElement(ViewActions.longClick())
}