package com.avito.android.test

import android.support.test.espresso.Espresso
import android.support.test.espresso.ViewAction
import android.support.test.espresso.ViewAssertion
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.assertion.isDoesntExistAssertion
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItem
import android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA
import android.support.v7.widget.RecyclerView
import android.view.View
import com.avito.android.test.action.ActionsDriver
import com.avito.android.test.checks.ChecksDriver
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
        interaction.waitToPerform(*actions)
    }

    override fun check(assertion: ViewAssertion) {
        interaction.waitForCheck(assertion)
    }

    override fun provideChildContext(matcher: Matcher<View>): InteractionContext {
        return SimpleInteractionContext(allOf(isDescendantOfA(this.matcher), matcher))
    }
}

class RecyclerViewInteractionContext(
    private val interactionContext: InteractionContext,
    private val cellMatcher: Matcher<View>,
    private val childMatcher: Matcher<View>,
    private val position: Int
) : InteractionContext {

    override fun perform(vararg actions: ViewAction) {
        actions.forEach {
            interactionContext.perform(
                actionOnItem<RecyclerView.ViewHolder>(
                    cellMatcher,
                    performDescendantAction(childMatcher, it)
                )
                    .atPosition(position)
            )
        }
    }

    override fun check(assertion: ViewAssertion) {
        if (assertion.isDoesntExistAssertion()) {
            interactionContext.perform(
                com.avito.android.test.espresso.action.actionOnItem<RecyclerView.ViewHolder>(
                    cellMatcher,
                    checkDescendantViewAction(childMatcher, assertion)
                )
                    .atPosition(position)
            )
        } else {
            interactionContext.perform(
                actionOnItem<RecyclerView.ViewHolder>(
                    cellMatcher,
                    checkDescendantViewAction(childMatcher, assertion)
                )
                    .atPosition(position)
            )
        }
    }

    override fun provideChildContext(matcher: Matcher<View>): InteractionContext =
        RecyclerViewInteractionContext(
            interactionContext,
            cellMatcher,
            allOf(isDescendantOfA(childMatcher), matcher),
            position
        )
}