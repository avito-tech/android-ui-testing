package com.avito.android.test.checks

import android.support.test.espresso.Espresso
import android.support.test.espresso.ViewAssertion
import android.support.test.espresso.ViewInteraction
import android.view.View
import com.avito.android.test.waitForCheck
import org.hamcrest.Matcher

@Deprecated("todo use InteractionContext")
class OnViewChecksDriver(private val matcher: Matcher<View>) : ChecksDriver {

    private val interaction: ViewInteraction
        get() = Espresso.onView(matcher)

    override fun check(assertion: ViewAssertion) {
        interaction.waitForCheck(assertion)
    }
}