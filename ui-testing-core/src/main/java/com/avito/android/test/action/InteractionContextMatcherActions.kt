package com.avito.android.test.action

import android.support.test.espresso.action.PrecisionDescriber
import android.support.test.espresso.action.SwipeDirection
import android.support.test.espresso.action.Swiper
import android.support.test.espresso.action.ViewActions
import android.support.v7.widget.RecyclerView
import android.view.View
import com.avito.android.test.InteractionContext
import com.avito.android.test.espresso.EspressoActions
import com.avito.android.test.espresso.action.TextViewReadAction
import com.avito.android.test.espresso.action.recycler.actionOnItem
import com.avito.android.test.espresso.action.recycler.scrollTo
import com.forkingcode.espresso.contrib.DescendantViewActions
import org.hamcrest.Matcher

class InteractionContextMatcherActions(
    private val interactionContext: InteractionContext,
    private val matcher: Matcher<View>,
    private val childMatcher: Matcher<View>
) : Actions {

    override fun scrollTo() {
        interactionContext.perform(
            scrollTo(
                itemViewMatcher = matcher,
                viewHolderType = RecyclerView.ViewHolder::class.java
            ).atPosition(0)
        )
    }

    override fun click() {
        interactionContext.perform(
            actionOnItem(
                itemViewMatcher = matcher,
                viewHolderType = RecyclerView.ViewHolder::class.java,
                viewAction = DescendantViewActions.performDescendantAction(
                    childMatcher,
                    ViewActions.click()
                )
            )
                .atPosition(0)
        )
    }

    override fun longClick() {
        interactionContext.perform(
            actionOnItem(
                itemViewMatcher = matcher,
                viewHolderType = RecyclerView.ViewHolder::class.java,
                viewAction = DescendantViewActions.performDescendantAction(
                    childMatcher,
                    ViewActions.longClick()
                )
            )
                .atPosition(0)
        )
    }

    override fun swipe(direction: SwipeDirection, speed: Swiper, precision: PrecisionDescriber) {
        interactionContext.perform(
            actionOnItem(
                itemViewMatcher = matcher,
                viewHolderType = RecyclerView.ViewHolder::class.java,
                viewAction = DescendantViewActions.performDescendantAction(
                    childMatcher,
                    EspressoActions.swipe(direction, speed, precision)
                )
            )
                .atPosition(0)
        )

        // FIXME
        Thread.sleep(1000)
    }

    override fun read(allowBlank: Boolean): String =
        TextViewReadAction.getResult(allowBlank) { action ->
            interactionContext.perform(
                actionOnItem(
                    itemViewMatcher = matcher,
                    viewHolderType = RecyclerView.ViewHolder::class.java,
                    viewAction = DescendantViewActions.performDescendantAction(childMatcher, action)
                )
                    .atPosition(0)
            )
        }
}
