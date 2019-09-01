package com.avito.android.test.action

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.PrecisionDescriber
import androidx.test.espresso.action.SwipeDirection
import androidx.test.espresso.action.Swiper
import com.avito.android.test.espresso.EspressoActions
import com.avito.android.test.espresso.action.TextViewReadAction
import com.avito.android.test.espresso.action.recycler.actionOnItemAtPosition
import com.avito.android.test.espresso.action.recycler.scrollToPosition
import com.avito.android.test.waitToPerform
import com.forkingcode.espresso.contrib.DescendantViewActions
import org.hamcrest.Matcher

@Deprecated("use InteractionContextPositionActions")
class OnDescendantPositionListItemActions(
    private val listMatcher: Matcher<View>,
    private val position: Int,
    private val childMatcher: Matcher<View>
) : Actions {

    private val interaction: ViewInteraction
        get() = Espresso.onView(listMatcher)

    override fun scrollTo() {
        interaction.waitToPerform(
            scrollToPosition(
                position
            )
        )
    }

    override fun click() {
        interaction.waitToPerform(
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
        interaction.waitToPerform(
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
        interaction.waitToPerform(
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
            interaction.waitToPerform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    position,
                    DescendantViewActions.performDescendantAction(childMatcher, action)
                )
            )
        }
}
