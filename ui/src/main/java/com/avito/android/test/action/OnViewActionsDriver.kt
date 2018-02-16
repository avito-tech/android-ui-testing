package com.avito.android.test.action

import android.support.test.espresso.Espresso
import android.support.test.espresso.ViewAction
import android.support.test.espresso.ViewInteraction
import android.view.View
import com.avito.android.test.waitToPerform
import org.hamcrest.Matcher

@Deprecated("use interaction context")
class OnViewActionsDriver(private val matcher: Matcher<View>) : ActionsDriver {

    private val interaction: ViewInteraction
        get() = Espresso.onView(matcher)

    override fun perform(vararg actions: ViewAction) {
        interaction.waitToPerform(*actions)
    }
}