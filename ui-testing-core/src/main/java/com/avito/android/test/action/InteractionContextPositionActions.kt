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

class InteractionContextPositionActions(
    private val interactionContext: InteractionContext,
    private val position: Int,
    private val childMatcher: Matcher<View>
) : Actions {

    override fun scrollTo() {
        interactionContext.perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                position
            )
        )
    }

    override fun click() {
        interactionContext.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                DescendantViewActions.performDescendantAction(childMatcher, ViewActions.click())
            )
        )
    }

    override fun longClick() {
        interactionContext.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                DescendantViewActions.performDescendantAction(childMatcher, ViewActions.longClick())
            )
        )
    }

    override fun swipe(direction: SwipeDirection, speed: Swiper, precision: PrecisionDescriber) {
        interactionContext.perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                DescendantViewActions.performDescendantAction(
                    childMatcher,
                    EspressoActions.swipe(direction, speed, precision)
                )
            )
        )

        //FIXME
        Thread.sleep(1000)
    }

    override fun read(allowBlank: Boolean): String =
        TextViewReadAction(allowBlank)
            .also {
                interactionContext.perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        position,
                        DescendantViewActions.performDescendantAction(childMatcher, it)
                    )
                )
            }.result
}