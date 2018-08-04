package com.avito.android.test.espresso.action

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

class ViewDoesntExistsInRecyclerCheckHack<VH : RecyclerView.ViewHolder> constructor(
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
        return ViewDoesntExistsInRecyclerCheckHack(viewHolderMatcher, viewAction, position)
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
            val matchedItems = itemsMatching(recyclerView, viewHolderMatcher, max)
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

// TODO REWRITE

/**
 * [ViewAction] which scrolls [RecyclerView] to the view matched by itemViewMatcher.
 * See [RecyclerViewActions.scrollTo] for more details.
 */
class ScrollToViewAction<VH : RecyclerView.ViewHolder>(
    private val viewHolderMatcher: Matcher<VH>,
    private val atPosition: Int = NO_POSITION
) : RecyclerViewActions.PositionableRecyclerViewAction {

    override fun atPosition(position: Int): RecyclerViewActions.PositionableRecyclerViewAction {
        checkArgument(position >= 0, "%d is used as an index - must be >= 0", position)
        return ScrollToViewAction(viewHolderMatcher, position)
    }

    override fun getConstraints(): Matcher<View> {
        return allOf<View>(isAssignableFrom(RecyclerView::class.java), isDisplayed())
    }

    override fun getDescription(): String {
        return if (atPosition == NO_POSITION) {
            "scroll RecyclerView to: $viewHolderMatcher"
        } else {
            String.format(
                "scroll RecyclerView to the: %dth matching %s.", atPosition,
                viewHolderMatcher
            )
        }
    }

    override fun perform(uiController: UiController, view: View) {
        val recyclerView = view as RecyclerView
        try {
            val maxMatches = if (atPosition == NO_POSITION) 2 else atPosition + 1
            val selectIndex = if (atPosition == NO_POSITION) 0 else atPosition
            val matchedItems = itemsMatching(recyclerView, viewHolderMatcher, maxMatches)

            if (selectIndex >= matchedItems.size) {
                throw RuntimeException(
                    String.format(
                        "Found %d items matching %s, but position %d was requested.",
                        matchedItems.size,
                        viewHolderMatcher.toString(),
                        atPosition
                    )
                )
            }
            if (atPosition == NO_POSITION && matchedItems.size == 2) {
                val ambiguousViewError = StringBuilder()
                ambiguousViewError.append(
                    String.format("Found more than one sub-view matching %s", viewHolderMatcher)
                )
                for (item in matchedItems) {
                    ambiguousViewError.append(item.toString() + "\n")
                }
                throw RuntimeException(ambiguousViewError.toString())
            }
            recyclerView.scrollToPosition(matchedItems[selectIndex].position)
            uiController.loopMainThreadUntilIdle()
        } catch (e: RuntimeException) {
            throw PerformException.Builder().withActionDescription(this.description)
                .withViewDescription(HumanReadables.describe(view)).withCause(e).build()
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
private fun <T : VH, VH : RecyclerView.ViewHolder> itemsMatching(
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
        if (null == cachedViewHolder) {
            @Suppress("UNCHECKED_CAST")
            cachedViewHolder = adapter.createViewHolder(recyclerView, itemType) as VH
            viewHolderCache.put(itemType, cachedViewHolder)
        }
        // Bind data to ViewHolder and apply matcher to view descendants.
        @Suppress("UNCHECKED_CAST")
        adapter.bindViewHolder(cachedViewHolder as T, position)
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

/**
 * Wrapper for matched items in recycler view which contains position and description of matched
 * view.
 */
private class MatchedItem(val position: Int, val description: String) {

    override fun toString(): String = description
}

/**
 * Creates matcher for view holder with given item view matcher.
 *
 * @param itemViewMatcher a item view matcher which is used to match item.
 * @return a matcher which matches a view holder containing item matching itemViewMatcher.
 */
private fun <VH : RecyclerView.ViewHolder> viewHolderMatcher(itemViewMatcher: Matcher<View>) =
    object : TypeSafeMatcher<VH>() {
        override fun matchesSafely(viewHolder: VH): Boolean {
            return itemViewMatcher.matches(viewHolder.itemView)
        }

        override fun describeTo(description: Description) {
            description.appendText("holder with view: ")
            itemViewMatcher.describeTo(description)
        }
    }

/**
 * Performs a [ViewAction] on a view matched by viewHolderMatcher.
 *
 *
 *  1. Scroll Recycler View to the view matched by itemViewMatcher
 *  1. Perform an action on the matched view
 *
 *
 * @param itemViewMatcher a
 * [`Matcher`](http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matcher.html)
 * that matches an item view in [RecyclerView]
 * @param viewAction the action that is performed on the view matched by itemViewMatcher
 * @throws PerformException if there are more than one items matching given viewHolderMatcher.
 */
fun <VH : RecyclerView.ViewHolder> actionOnItem(
    itemViewMatcher: Matcher<View>,
    viewAction: ViewAction
): RecyclerViewActions.PositionableRecyclerViewAction {
    val viewHolderMatcher = viewHolderMatcher<VH>(itemViewMatcher)
    return ViewDoesntExistsInRecyclerCheckHack(viewHolderMatcher, viewAction)
}

/**
 * Performs a [ViewAction] on a view at position.
 *
 *
 *  1. Scroll Recycler View to position
 *  1. Perform an action on the view at position
 *
 *
 * @param position position of a view in [RecyclerView]
 * @param viewAction the action that is performed on the view matched by itemViewMatcher
 */
fun <VH : RecyclerView.ViewHolder> actionOnItemAtPosition(
    position: Int,
    viewAction: ViewAction
): ViewAction {
    return ActionOnItemAtPositionViewAction<VH>(position, viewAction)
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

        ScrollToPositionViewAction(position).perform(uiController, view)
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

/**
 * [ViewAction] which scrolls [RecyclerView] to a given position. See
 * [RecyclerViewActions.scrollToPosition] for more details.
 */
private class ScrollToPositionViewAction(private val position: Int) : ViewAction {

    override fun getConstraints(): Matcher<View> {
        return allOf(isAssignableFrom(RecyclerView::class.java), isDisplayed())
    }

    override fun getDescription(): String = "scroll RecyclerView to position: $position"

    override fun perform(uiController: UiController, view: View) {
        val recyclerView = view as RecyclerView
        recyclerView.scrollToPosition(position)
        uiController.loopMainThreadUntilIdle()
    }
}
