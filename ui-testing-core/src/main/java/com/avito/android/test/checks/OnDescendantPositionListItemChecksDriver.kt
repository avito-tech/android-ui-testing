package com.avito.android.test.checks

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import com.avito.android.test.UITestConfig
import com.avito.android.test.espresso.action.recycler.actionOnItemAtPosition
import com.avito.android.test.interceptor.AssertionInterceptor
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
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                DescendantViewActions.checkDescendantViewAction(childMatcher, interceptedAssertion)
            )
        )
    }
}
