package com.avito.android.test.checks

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.ViewAssertion
import com.avito.android.test.InteractionContext
import com.avito.android.test.UITestConfig
import com.avito.android.test.espresso.action.recycler.actionOnItem
import com.avito.android.test.interceptor.AssertionInterceptor
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
            actionOnItem(
                itemViewMatcher = matcher,
                viewHolderType = RecyclerView.ViewHolder::class.java,
                viewAction = DescendantViewActions.checkDescendantViewAction(childMatcher, interceptedAssertion)
            ).atPosition(0)
        )
    }
}
