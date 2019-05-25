package com.avito.android.test

import android.support.test.espresso.Espresso
import android.support.test.espresso.ViewAction
import android.support.test.espresso.ViewAssertion
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.assertion.isDoesntExistAssertion
import android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA
import android.support.v7.widget.RecyclerView
import android.view.View
import com.avito.android.test.action.ActionsDriver
import com.avito.android.test.checks.ChecksDriver
import com.avito.android.test.espresso.action.GroupedViewAction
import com.avito.android.test.espresso.action.recycler.actionOnItem
import com.avito.android.test.espresso.action.recycler.itemDoesntExists
import com.avito.android.test.interceptor.ActionInterceptor
import com.avito.android.test.interceptor.AssertionInterceptor
import com.forkingcode.espresso.contrib.DescendantViewActions.checkDescendantViewAction
import com.forkingcode.espresso.contrib.DescendantViewActions.performDescendantAction
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf

interface InteractionContext : ActionsDriver, ChecksDriver {

    fun provideChildContext(matcher: Matcher<View>): InteractionContext
}

class SimpleInteractionContext(private val matcher: Matcher<View>) : InteractionContext {

    private val interaction: ViewInteraction
        get() = Espresso.onView(matcher)

    override fun perform(vararg actions: ViewAction) {
        interaction.waitToPerform(actions.map { action ->
            ActionInterceptor.Proxy(
                action,
                UITestConfig.actionInterceptors
            )
        })
    }

    override fun check(assertion: ViewAssertion) {
        interaction.waitForCheck(
            AssertionInterceptor.Proxy(assertion, UITestConfig.assertionInterceptors)
        )
    }

    override fun provideChildContext(matcher: Matcher<View>): InteractionContext =
        SimpleInteractionContext(allOf(isDescendantOfA(this.matcher), matcher))
}

class RecyclerViewInteractionContext(
    private val interactionContext: InteractionContext,
    private val cellMatcher: Matcher<View>,
    private val childMatcher: Matcher<View>,
    private val position: Int
) : InteractionContext {

    override fun perform(vararg actions: ViewAction) {
        val groupedAction = GroupedViewAction(actions.toList())

        val actionOnItem = actionOnItem<RecyclerView.ViewHolder>(
            itemViewMatcher = cellMatcher,
            viewAction = performDescendantAction(childMatcher, groupedAction)
        ).atPosition(position)

        ActionInterceptor.Proxy(
            source = actionOnItem,
            interceptors = UITestConfig.actionInterceptors
        )

        interactionContext.perform(actionOnItem)
    }

    override fun check(assertion: ViewAssertion) {
        val intercepted = AssertionInterceptor.Proxy(assertion, UITestConfig.assertionInterceptors)

        if (assertion.isDoesntExistAssertion()) {
            interactionContext.perform(
                itemDoesntExists<RecyclerView.ViewHolder>(
                    itemViewMatcher = cellMatcher,
                    viewAction = checkDescendantViewAction(childMatcher, intercepted)
                )
                    .atPosition(position)
            )
        } else {
            interactionContext.perform(
                actionOnItem<RecyclerView.ViewHolder>(
                    itemViewMatcher = cellMatcher,
                    viewAction = checkDescendantViewAction(childMatcher, intercepted)
                )
                    .atPosition(position)
            )
        }
    }

    override fun provideChildContext(matcher: Matcher<View>): InteractionContext =
        RecyclerViewInteractionContext(
            interactionContext = interactionContext,
            cellMatcher = cellMatcher,
            childMatcher = allOf(isDescendantOfA(childMatcher), matcher),
            position = position
        )
}
