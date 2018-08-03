package com.avito.android.test.action

import android.support.test.espresso.action.PrecisionDescriber
import android.support.test.espresso.action.SwipeDirection
import android.support.test.espresso.action.Swiper
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.v7.widget.RecyclerView
import android.view.View
import com.avito.android.test.InteractionContext
import com.avito.android.test.espresso.EspressoActions
import com.avito.android.test.espresso.action.TextViewReadAction
import com.forkingcode.espresso.contrib.DescendantViewActions
import org.hamcrest.Matcher

class InteractionContextMatcherActions(
    private val interactionContext: InteractionContext,
    private val matcher: Matcher<View>,
    private val childMatcher: Matcher<View>
) : Actions {

    override fun scrollTo() {
        interactionContext.perform(
            RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(matcher).atPosition(
                0
            )
        )
    }

    override fun click() {
        interactionContext.perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                matcher,
                DescendantViewActions.performDescendantAction(childMatcher, ViewActions.click())
            )
                .atPosition(0)
        )
    }

    override fun longClick() {
        interactionContext.perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                matcher,
                DescendantViewActions.performDescendantAction(childMatcher, ViewActions.longClick())
            )
                .atPosition(0)
        )
    }

    override fun swipe(direction: SwipeDirection, speed: Swiper, precision: PrecisionDescriber) {
        interactionContext.perform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                matcher,
                DescendantViewActions.performDescendantAction(
                    childMatcher,
                    EspressoActions.swipe(direction, speed, precision)
                )
            )
                .atPosition(0)
        )

        //FIXME
        Thread.sleep(1000)
    }

    override fun read(allowBlank: Boolean): String =
        TextViewReadAction(allowBlank)
            .also {
                interactionContext.perform(
                    RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                        matcher,
                        DescendantViewActions.performDescendantAction(childMatcher, it)
                    )
                        .atPosition(0)
                )
            }.result
}