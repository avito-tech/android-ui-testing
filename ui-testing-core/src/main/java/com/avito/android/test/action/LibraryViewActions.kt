package com.avito.android.test.action

import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions
import com.avito.android.test.UITestConfig

internal object LibraryViewActions {

    fun click(): ViewAction = when (UITestConfig.clickRollbackPolicy) {
        is UITestConfig.ClickRollbackPolicy.DoNothing -> ActionOnEnabledElement(
            ViewActions.click()
        )

        is UITestConfig.ClickRollbackPolicy.TryOneMoreClick -> ActionOnEnabledElement(
            ViewActions.click(
                ActionOnEnabledElement(ViewActions.click())
            )
        )
    }

    fun longClick(): ViewAction = ActionOnEnabledElement(ViewActions.longClick())
}