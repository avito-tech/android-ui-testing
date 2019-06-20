package com.avito.android.test.espresso.action.recycler

import android.support.test.espresso.PerformException
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.intent.Checks.checkArgument
import android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.util.HumanReadables
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.NO_POSITION
import android.view.View
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf

interface PositionableRecyclerViewAction : ViewAction {

    /**
     * Returns a new ViewAction which will cause the ViewAction to operate upon the position-th
     * element which the matcher has selected.
     *
     * @param position a 0-based index into the list of matching elements within the RecyclerView.
     * @return PositionableRecyclerViewAction a new ViewAction focused on a particular position.
     * @throws IllegalArgumentException if position < 0.
     */
    fun atPosition(position: Int): PositionableRecyclerViewAction
}

private class ViewDoesNotExistsInRecyclerCheckHack<VH : RecyclerView.ViewHolder> constructor(
    private val viewHolderMatcher: Matcher<VH>,
    private val viewHolderType: Class<VH>,
    private val viewAction: ViewAction,
    private val atPosition: Int = NO_POSITION
) : PositionableRecyclerViewAction {

    override fun getConstraints(): Matcher<View> =
        allOf<View>(isAssignableFrom(RecyclerView::class.java), isDisplayed())

    override fun atPosition(position: Int): PositionableRecyclerViewAction {
        checkArgument(position >= 0, "%d is used as an index - must be >= 0", position)
        return ViewDoesNotExistsInRecyclerCheckHack(
            viewHolderType = viewHolderType,
            viewHolderMatcher = viewHolderMatcher,
            viewAction = viewAction,
            atPosition = position
        )
    }

    override fun getDescription(): String = if (atPosition == NO_POSITION) {
        String.format(
            "performing ViewAction: %s on item matching: %s",
            viewAction.description,
            viewHolderMatcher
        )
    } else {
        String.format(
            "performing ViewAction: %s on %d-th item matching: %s",
            viewAction.description,
            atPosition,
            viewHolderMatcher
        )
    }

    override fun perform(uiController: UiController, root: View) {
        val recyclerView = root as RecyclerView
        try {
            val max = if (atPosition == NO_POSITION) 2 else atPosition + 1
            val selectIndex = if (atPosition == NO_POSITION) 0 else atPosition
            val matchedItems = itemsMatching(
                recyclerView = recyclerView,
                viewHolderType = viewHolderType,
                viewHolderMatcher = viewHolderMatcher,
                max = max
            )
            if (matchedItems.isNotEmpty()) {
                val position = matchedItems[selectIndex].position
                @Suppress("UNCHECKED_CAST")
                val viewHolderForPosition =
                    recyclerView.findViewHolderForAdapterPosition(position) as VH?
                val viewAtPosition = viewHolderForPosition?.itemView

                assertThat<Boolean>(
                    "View is present in the hierarchy: " +
                        HumanReadables.describe(viewAtPosition), true, `is`(false)
                )
            }
            uiController.loopMainThreadUntilIdle()
        } catch (t: Throwable) {
            throw PerformException.Builder().withActionDescription(this.description)
                .withViewDescription(HumanReadables.describe(root)).withCause(t).build()
        }
    }
}

fun <VH : RecyclerView.ViewHolder> itemDoesNotExists(
    itemViewMatcher: Matcher<View>,
    viewHolderType: Class<VH>,
    viewAction: ViewAction
): PositionableRecyclerViewAction {
    val viewHolderMatcher = viewHolderMatcher<VH>(itemViewMatcher)

    return ViewDoesNotExistsInRecyclerCheckHack(
        viewHolderMatcher = viewHolderMatcher,
        viewHolderType = viewHolderType,
        viewAction = viewAction
    )
}

fun <VH : RecyclerView.ViewHolder> actionOnItemAtPosition(
    position: Int,
    viewAction: ViewAction
): ViewAction {
    return ActionOnItemAtPositionViewAction<VH>(
        position = position,
        viewAction = viewAction
    )
}

private class ActionOnItemAtPositionViewAction<VH : RecyclerView.ViewHolder>(
    private val position: Int,
    private val viewAction: ViewAction
) : ViewAction {

    override fun getConstraints(): Matcher<View> =
        allOf(isAssignableFrom(RecyclerView::class.java), isDisplayed())

    override fun getDescription(): String =
        ("actionOnItemAtPosition performing ViewAction: " + viewAction.description +
            " on item at position: " + position)

    override fun perform(uiController: UiController, view: View) {
        val recyclerView = view as RecyclerView

        scrollToPosition(position).perform(uiController, view)
        uiController.loopMainThreadUntilIdle()

        @Suppress("UNCHECKED_CAST")
        val viewHolderForPosition = recyclerView.findViewHolderForAdapterPosition(position) as VH?
            ?: throw PerformException.Builder().withActionDescription(this.toString())
                .withViewDescription(HumanReadables.describe(view))
                .withCause(IllegalStateException("No view holder at position: $position"))
                .build()

        val viewAtPosition = viewHolderForPosition.itemView

        viewAction.perform(uiController, viewAtPosition)
    }
}

private class ActionOnItemViewAction<VH : RecyclerView.ViewHolder>(
    private val viewHolderMatcher: Matcher<VH>,
    private val viewHolderType: Class<VH>,
    private val viewAction: ViewAction,
    private val atPosition: Int = NO_POSITION
) : PositionableRecyclerViewAction {

    override fun getConstraints(): Matcher<View> {
        return allOf(isAssignableFrom(RecyclerView::class.java), isDisplayed())
    }

    override fun atPosition(position: Int): PositionableRecyclerViewAction {
        checkArgument(position >= 0, "%d is used as an index - must be >= 0", position)
        return ActionOnItemViewAction(
            viewHolderMatcher = viewHolderMatcher,
            viewHolderType = viewHolderType,
            viewAction = viewAction,
            atPosition = position
        )
    }

    override fun getDescription(): String {
        return if (atPosition == NO_POSITION) {
            String.format(
                "performing ViewAction: %s on item matching: %s",
                viewAction.description, viewHolderMatcher
            )
        } else {
            String.format(
                "performing ViewAction: %s on %d-th item matching: %s",
                viewAction.description, atPosition, viewHolderMatcher
            )
        }
    }

    override fun perform(uiController: UiController, root: View) {
        val recyclerView = root as RecyclerView
        try {
            val max = if (atPosition == NO_POSITION) 2 else atPosition + 1
            val selectIndex = if (atPosition == NO_POSITION) 0 else atPosition
            val matchedItems = itemsMatching(
                recyclerView = recyclerView,
                viewHolderType = viewHolderType,
                viewHolderMatcher = viewHolderMatcher,
                max = max
            )
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                matchedItems[selectIndex].position,
                viewAction
            ).perform(
                uiController, root
            )
            uiController.loopMainThreadUntilIdle()
        } catch (t: Throwable) {
            throw PerformException.Builder().withActionDescription(this.description)
                .withViewDescription(HumanReadables.describe(root)).withCause(t).build()
        }
    }
}

fun <VH : RecyclerView.ViewHolder> actionOnHolderItem(
    viewHolderMatcher: Matcher<VH>,
    viewHolderType: Class<VH>,
    viewAction: ViewAction
): PositionableRecyclerViewAction =
    ActionOnItemViewAction(
        viewHolderMatcher = viewHolderMatcher,
        viewHolderType = viewHolderType,
        viewAction = viewAction
    )

fun <VH : RecyclerView.ViewHolder> actionOnItem(
    itemViewMatcher: Matcher<View>,
    viewHolderType: Class<VH>,
    viewAction: ViewAction
): PositionableRecyclerViewAction {
    val viewHolderMatcher = viewHolderMatcher<VH>(itemViewMatcher)

    return ActionOnItemViewAction(
        viewHolderMatcher = viewHolderMatcher,
        viewHolderType = viewHolderType,
        viewAction = viewAction
    )
}

class ViewActionOnItemAtPosition<VH : RecyclerView.ViewHolder>(
    private val position: Int,
    private val viewAction: ViewAction
) : ViewAction {

    override fun getConstraints(): Matcher<View> =
        allOf(isAssignableFrom(RecyclerView::class.java), isDisplayed())

    override fun getDescription(): String =
        ("performing ViewAction: " + viewAction.description + " on item at position: " + position)

    override fun perform(uiController: UiController, view: View) {
        val recyclerView = view as RecyclerView

        @Suppress("UNCHECKED_CAST")
        val viewHolderForPosition: RecyclerView.ViewHolder? =
            recyclerView.findViewHolderForLayoutPosition(position) as VH?

        val viewAtPosition: View = viewHolderForPosition?.itemView
            ?: throw PerformException.Builder().withActionDescription(this.toString())
                .withViewDescription("null")
                .withCause(IllegalStateException("No view at position: $position")).build()

        viewAction.perform(uiController, viewAtPosition)
    }
}
