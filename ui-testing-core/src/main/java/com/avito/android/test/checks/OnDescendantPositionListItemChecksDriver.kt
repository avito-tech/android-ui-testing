package com.avito.android.test.checks

import android.support.test.espresso.Espresso
import android.support.test.espresso.ViewAssertion
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.v7.widget.RecyclerView
import android.view.View
import com.avito.android.test.interceptor.AssertionInterceptor
import com.avito.android.test.UITestConfig
import com.avito.android.test.waitToPerform
import com.forkingcode.espresso.contrib.DescendantViewActions
import org.hamcrest.Matcher

@Deprecated("todo use InteractionContextPositionChecksDriver")
class OnDescendantPositionListItemChecksDriver(
    private val listMatcher: Matcher<View>,
    private val position: Int,
    private val childMatcher: Matcher<View>
) : ChecksDriver {

    private val interaction: ViewInteraction
        get() = Espresso.onView(listMatcher)

    override fun check(assertion: ViewAssertion) {
        val interceptedAssertion =
            AssertionInterceptor.Proxy(assertion, UITestConfig.assertionInterceptors)

        interaction.waitToPerform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                DescendantViewActions.checkDescendantViewAction(childMatcher, interceptedAssertion)
            )
        )
    }
}