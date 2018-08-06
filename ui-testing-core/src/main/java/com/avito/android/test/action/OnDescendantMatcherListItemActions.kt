package com.avito.android.test.action

import android.support.test.espresso.Espresso
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.action.PrecisionDescriber
import android.support.test.espresso.action.SwipeDirection
import android.support.test.espresso.action.Swiper
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.v7.widget.RecyclerView
import android.view.View
import com.avito.android.test.espresso.EspressoActions
import com.avito.android.test.espresso.action.TextViewReadAction
import com.avito.android.test.waitToPerform
import com.forkingcode.espresso.contrib.DescendantViewActions
import org.hamcrest.Matcher

@Deprecated("use InteractionContextMatcherActions")
class OnDescendantMatcherListItemActions(
    private val listMatcher: Matcher<View>,
    private val matcher: Matcher<View>,
    private val childMatcher: Matcher<View>
) : Actions {

    private val interaction: ViewInteraction
        get() = Espresso.onView(listMatcher)

    override fun scrollTo() {
        interaction.waitToPerform(
            RecyclerViewActions.scrollTo<RecyclerView.ViewHolder>(matcher).atPosition(
                0
            )
        )
    }

    override fun click() {
        interaction.waitToPerform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                matcher,
                DescendantViewActions.performDescendantAction(childMatcher, ViewActions.click())
            )
                .atPosition(0)
        )
    }

    override fun longClick() {
        interaction.waitToPerform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                matcher,
                DescendantViewActions.performDescendantAction(childMatcher, ViewActions.longClick())
            )
                .atPosition(0)
        )
    }

    override fun swipe(direction: SwipeDirection, speed: Swiper, precision: PrecisionDescriber) {
        interaction.waitToPerform(
            RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                matcher,
                DescendantViewActions.performDescendantAction(
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
        TextViewReadAction(allowBlank)
            .also { action ->
                interaction.waitToPerform(
                    RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                        matcher,
                        DescendantViewActions.performDescendantAction(childMatcher, action)
                    )
                        .atPosition(0)
                )
            }.result
}
