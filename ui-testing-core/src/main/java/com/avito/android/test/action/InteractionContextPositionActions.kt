package com.avito.android.test.action

import android.support.test.espresso.action.PrecisionDescriber
import android.support.test.espresso.action.SwipeDirection
import android.support.test.espresso.action.Swiper
import android.support.v7.widget.RecyclerView
import android.view.View
import com.avito.android.test.InteractionContext
import com.avito.android.test.espresso.EspressoActions
import com.avito.android.test.espresso.action.TextViewReadAction
import com.avito.android.test.espresso.action.recycler.actionOnItemAtPosition
import com.avito.android.test.espresso.action.recycler.scrollToPosition
import com.forkingcode.espresso.contrib.DescendantViewActions
import org.hamcrest.Matcher

class InteractionContextPositionActions(
    private val interactionContext: InteractionContext,
    private val position: Int,
    private val childMatcher: Matcher<View>
) : Actions {

    override fun scrollTo() {
        interactionContext.perform(
            scrollToPosition(
                position
            )
        )
    }

    override fun click() {
        interactionContext.perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                DescendantViewActions.performDescendantAction(
                    childMatcher,
                    EspressoActions.click()
                )
            )
        )
    }

    override fun longClick() {
        interactionContext.perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                DescendantViewActions.performDescendantAction(
                    childMatcher,
                    EspressoActions.longClick()
                )
            )
        )
    }

    override fun swipe(direction: SwipeDirection, speed: Swiper, precision: PrecisionDescriber) {
        interactionContext.perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                position,
                DescendantViewActions.performDescendantAction(
                    childMatcher,
                    EspressoActions.swipe(direction, speed, precision)
                )
            )
        )

        // FIXME
        Thread.sleep(1000)
    }

    override fun read(allowBlank: Boolean): String =
        TextViewReadAction.getResult(allowBlank) { action ->
            interactionContext.perform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    position,
                    DescendantViewActions.performDescendantAction(childMatcher, action)
                )
            )
        }
}
