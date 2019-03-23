package com.avito.android.test.checks

import android.support.test.espresso.ViewAssertion
import android.support.v7.widget.RecyclerView
import android.view.View
import com.avito.android.test.InteractionContext
import com.avito.android.test.UITestConfig
import com.avito.android.test.espresso.action.recycler.actionOnItemAtPosition
import com.avito.android.test.interceptor.AssertionInterceptor
import com.forkingcode.espresso.contrib.DescendantViewActions
import org.hamcrest.Matcher

class InteractionContextPositionChecksDriver(
    private val interactionContext: InteractionContext,
    private val position: Int,
    private val childMatcher: Matcher<View>
) : ChecksDriver {

    override fun check(assertion: ViewAssertion) {
        val interceptedAssertion =
            AssertionInterceptor.Proxy(assertion, UITestConfig.assertionInterceptors)

        interactionContext.perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                DescendantViewActions.checkDescendantViewAction(childMatcher, interceptedAssertion)
            )
        )
    }
}
