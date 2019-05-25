package com.avito.android.test.espresso.action.recycler

import android.support.test.espresso.PerformException
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.Checks.checkArgument
import android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.util.HumanReadables
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.NO_POSITION
import android.util.SparseArray
import android.view.View
import java.util.ArrayList
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher

private class ViewDoesntExistsInRecyclerCheckHack<VH : RecyclerView.ViewHolder> constructor(
    viewHolderMatcher: Matcher<VH>,
    viewAction: ViewAction,
    private val atPosition: Int = NO_POSITION
) : RecyclerViewActions.PositionableRecyclerViewAction {

    private val viewHolderMatcher: Matcher<VH> = checkNotNull(viewHolderMatcher)
    private val viewAction: ViewAction = checkNotNull(viewAction)

    override fun getConstraints(): Matcher<View> =
        allOf<View>(isAssignableFrom(RecyclerView::class.java), isDisplayed())

    override fun atPosition(position: Int): RecyclerViewActions.PositionableRecyclerViewAction {
        checkArgument(position >= 0, "%d is used as an index - must be >= 0", position)
        return ViewDoesntExistsInRecyclerCheckHack(
            viewHolderMatcher,
            viewAction,
            position
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
            // the above scroller has checked bounds, dupes (maybe) and brought the element into screen.
            val max = if (atPosition == NO_POSITION) 2 else atPosition + 1
            val selectIndex = if (atPosition == NO_POSITION) 0 else atPosition
            val matchedItems = itemsMatching(
                recyclerView,
                viewHolderMatcher,
                max
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
        } catch (e: RuntimeException) {
            throw PerformException.Builder().withActionDescription(this.description)
                .withViewDescription(HumanReadables.describe(root)).withCause(e).build()
        }
    }
}

/**
 * Finds positions of items in [RecyclerView] which is matching given viewHolderMatcher.
 * This is similar to positionMatching(RecyclerView, Matcher<VH>), except that it returns list of
 * multiple positions if there are, rather than throwing Ambiguous view error exception.
 *
 * @param recyclerView recycler view which is hosting items.
 * @param viewHolderMatcher a
 * [`Matcher`](http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matcher.html) that matches
 * an item view in [RecyclerView]
 * @return list of MatchedItem which contains position and description of items in recyclerView.
 * @throws RuntimeException if more than one item or item could not be found. </VH>
 */
internal fun <VH : RecyclerView.ViewHolder> itemsMatching(
    recyclerView: RecyclerView,
    viewHolderMatcher: Matcher<VH>,
    max: Int
): List<MatchedItem> {
    val adapter = recyclerView.adapter

    val viewHolderCache = SparseArray<VH>()
    val matchedItems = ArrayList<MatchedItem>()

    for (position in 0 until adapter.itemCount) {
        val itemType = adapter.getItemViewType(position)
        var cachedViewHolder: VH? = viewHolderCache.get(itemType)
        // Create a view holder per type if not exists
        if (cachedViewHolder == null) {
            @Suppress("UNCHECKED_CAST")

            cachedViewHolder = (adapter.createViewHolder(recyclerView, itemType) as VH)

            /**
             * It allows production code to understand, that bindViewHolder has called by
             * fake rendering process (for find element in recycler view before it will be shown on
             * real user screen).
             */
            (cachedViewHolder as RecyclerView.ViewHolder).itemView.setTag(
                FAKE_RENDERING_VIEW_HOLDER_TAG_KEY,
                FAKE_RENDERING_VIEW_HOLDER_TAG_VALUE
            )

            viewHolderCache.put(itemType, cachedViewHolder)
        }
        // Bind data to ViewHolder and apply matcher to view descendants.
        @Suppress("UNCHECKED_CAST")
        adapter.bindViewHolder(cachedViewHolder, position)

        if (viewHolderMatcher.matches(cachedViewHolder)) {
            matchedItems.add(
                MatchedItem(
                    position,
                    HumanReadables.getViewHierarchyErrorMessage(
                        cachedViewHolder.itemView, null,
                        "\n\n*** Matched ViewHolder item at position: $position ***", null
                    )
                )
            )
            if (matchedItems.size == max) {
                break
            }
        }
    }
    return matchedItems
}

internal class MatchedItem(val position: Int, val description: String) {

    override fun toString(): String = description
}

fun <VH : RecyclerView.ViewHolder> viewHolderMatcher(itemViewMatcher: Matcher<View>) =
    object : TypeSafeMatcher<VH>() {
        override fun matchesSafely(viewHolder: VH): Boolean {
            return itemViewMatcher.matches(viewHolder.itemView)
        }

        override fun describeTo(description: Description) {
            description.appendText("holder with view: ")
            itemViewMatcher.describeTo(description)
        }
    }

fun <VH : RecyclerView.ViewHolder> itemDoesntExists(
    itemViewMatcher: Matcher<View>,
    viewAction: ViewAction
): RecyclerViewActions.PositionableRecyclerViewAction {
    val viewHolderMatcher =
        viewHolderMatcher<VH>(itemViewMatcher)
    return ViewDoesntExistsInRecyclerCheckHack(
        viewHolderMatcher,
        viewAction
    )
}

fun <VH : RecyclerView.ViewHolder> actionOnItemAtPosition(
    position: Int,
    viewAction: ViewAction
): ViewAction {
    return ActionOnItemAtPositionViewAction<VH>(
        position,
        viewAction
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

        val viewAtPosition =
            viewHolderForPosition.itemView
                ?: throw PerformException.Builder().withActionDescription(this.toString())
                    .withCause(IllegalStateException("No view at position: $position")).build()

        viewAction.perform(uiController, viewAtPosition)
    }
}

private class ActionOnItemViewAction<VH : RecyclerView.ViewHolder>(
    private val viewHolderMatcher: Matcher<VH>,
    private val viewAction: ViewAction,
    private val atPosition: Int = NO_POSITION
) : RecyclerViewActions.PositionableRecyclerViewAction {

    override fun getConstraints(): Matcher<View> {
        return allOf(isAssignableFrom(RecyclerView::class.java), isDisplayed())
    }

    override fun atPosition(position: Int): RecyclerViewActions.PositionableRecyclerViewAction {
        checkArgument(position >= 0, "%d is used as an index - must be >= 0", position)
        return ActionOnItemViewAction(
            viewHolderMatcher,
            viewAction,
            position
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
                recyclerView,
                viewHolderMatcher,
                max
            )
            actionOnItemAtPosition<RecyclerView.ViewHolder>(
                matchedItems[selectIndex].position,
                viewAction
            ).perform(
                uiController, root
            )
            uiController.loopMainThreadUntilIdle()
        } catch (e: RuntimeException) {
            throw PerformException.Builder().withActionDescription(this.getDescription())
                .withViewDescription(HumanReadables.describe(root)).withCause(e).build()
        }
    }
}

fun <VH : RecyclerView.ViewHolder> actionOnHolderItem(
    viewHolderMatcher: Matcher<VH>,
    viewAction: ViewAction
): RecyclerViewActions.PositionableRecyclerViewAction =
    ActionOnItemViewAction(viewHolderMatcher, viewAction)

fun <VH : RecyclerView.ViewHolder> actionOnItem(
    itemViewMatcher: Matcher<View>,
    viewAction: ViewAction
): RecyclerViewActions.PositionableRecyclerViewAction {
    val viewHolderMatcher = viewHolderMatcher<VH>(itemViewMatcher)
    return ActionOnItemViewAction(viewHolderMatcher, viewAction)
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

private const val FAKE_RENDERING_VIEW_HOLDER_TAG_KEY = Integer.MAX_VALUE - 228
private const val FAKE_RENDERING_VIEW_HOLDER_TAG_VALUE = "RENDERED_FOR_FAKE_RUN"
