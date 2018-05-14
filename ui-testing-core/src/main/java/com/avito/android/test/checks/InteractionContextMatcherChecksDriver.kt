package com.avito.android.test.checks

import android.support.test.espresso.ViewAssertion
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.v7.widget.RecyclerView
import android.view.View
import com.avito.android.test.interceptor.AssertionInterceptor
import com.avito.android.test.InteractionContext
import com.avito.android.test.UITestConfig
import com.forkingcode.espresso.contrib.DescendantViewActions
import org.hamcrest.Matcher

class InteractionContextMatcherChecksDriver(
    private val interactionContext: InteractionContext,
    private val matcher: Matcher<View>,
    private val childMatcher: Matcher<View>
) : ChecksDriver {

    override fun check(assertion: ViewAssertion) {
        val interceptedAssertion =
            AssertionInterceptor.Proxy(assertion, UITestConfig.assertionInterceptors)

        interactionContext.perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                matcher,
                DescendantViewActions.checkDescendantViewAction(childMatcher, interceptedAssertion)
            ).atPosition(0)
        )
    }
}