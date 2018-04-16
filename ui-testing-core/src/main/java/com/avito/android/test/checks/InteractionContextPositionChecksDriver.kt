package com.avito.android.test.checks

import android.support.test.espresso.ViewAssertion
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.v7.widget.RecyclerView
import android.view.View
import com.avito.android.test.InteractionContext
import com.forkingcode.espresso.contrib.DescendantViewActions
import org.hamcrest.Matcher


class InteractionContextPositionChecksDriver(
    private val interactionContext: InteractionContext,
    private val position: Int,
    private val childMatcher: Matcher<View>
) : ChecksDriver {

    override fun check(assertion: ViewAssertion) {
        interactionContext.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                DescendantViewActions.checkDescendantViewAction(childMatcher, assertion)
            )
        )
    }
}