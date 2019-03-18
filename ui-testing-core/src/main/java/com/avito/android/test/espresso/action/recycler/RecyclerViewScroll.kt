package com.avito.android.test.espresso.action.recycler

import android.support.test.espresso.PerformException
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.intent.Checks
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.util.HumanReadables
import android.support.v7.widget.RecyclerView
import android.view.View
import com.avito.android.test.espresso.action.scroll.collapseAllAppBarsInParent
import com.avito.android.test.espresso.action.scroll.scrollToScrollableParentCenterPosition
import org.hamcrest.Matcher
import org.hamcrest.Matchers

private class ScrollToPositionViewAction(private val position: Int) : ViewAction {

    override fun getConstraints(): Matcher<View> {
        return Matchers.allOf(
            ViewMatchers.isAssignableFrom(RecyclerView::class.java),
            ViewMatchers.isDisplayed()
        )
    }

    override fun getDescription(): String = "scroll RecyclerView to position: $position"

    override fun perform(uiController: UiController, view: View) {
        val recyclerView = view as RecyclerView

        recyclerView.scrollItemAtPositionToCenter(
            uiController = uiController,
            position = position
        )
    }
}

class SmoothScrollToPositionViewAction constructor(private val position: Int) : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return Matchers.allOf(
            ViewMatchers.isAssignableFrom(RecyclerView::class.java),
            ViewMatchers.isDisplayed()
        )
    }

    override fun getDescription(): String {
        return "smooth scroll RecyclerView to position: $position"
    }

    override fun perform(uiController: UiController, view: View) {
        val recyclerView = view as RecyclerView
        recyclerView.smoothScrollToPosition(position)
    }
}

fun scrollToPosition(position: Int): ViewAction {
    return ScrollToPositionViewAction(position)
}

private class ScrollToElementInsideRecyclerViewItem(
    private val position: Int,
    private val targetViewId: Int
) : ViewAction {

    override fun getDescription(): String = "scroll to element in item"

    override fun getConstraints(): Matcher<View> =
        Matchers.allOf(
            ViewMatchers.isAssignableFrom(RecyclerView::class.java),
            ViewMatchers.isDisplayed()
        )

    override fun perform(uiController: UiController, view: View) {
        val recyclerView = view as RecyclerView

        recyclerView.scrollToViewInsideItemAtPositionToCenter(
            uiController = uiController,
            position = position,
            childId = targetViewId
        )
    }
}

fun scrollToElementInsideRecyclerViewItem(
    position: Int,
    childViewId: Int
): ViewAction = ScrollToElementInsideRecyclerViewItem(
    position = position,
    targetViewId = childViewId
)

internal class ScrollToViewAction<VH : RecyclerView.ViewHolder>(
    private val viewHolderMatcher: Matcher<VH>,
    private val atPosition: Int = RecyclerView.NO_POSITION
) : RecyclerViewActions.PositionableRecyclerViewAction {

    override fun atPosition(position: Int): RecyclerViewActions.PositionableRecyclerViewAction {
        Checks.checkArgument(position >= 0, "%d is used as an index - must be >= 0", position)
        return ScrollToViewAction(
            viewHolderMatcher,
            position
        )
    }

    override fun getConstraints(): Matcher<View> {
        return Matchers.allOf<View>(
            ViewMatchers.isAssignableFrom(RecyclerView::class.java),
            ViewMatchers.isDisplayed()
        )
    }

    override fun getDescription(): String {
        return if (atPosition == RecyclerView.NO_POSITION) {
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
            val maxMatches = if (atPosition == RecyclerView.NO_POSITION) 2 else atPosition + 1
            val selectIndex = if (atPosition == RecyclerView.NO_POSITION) 0 else atPosition
            val matchedItems = itemsMatching(
                recyclerView,
                viewHolderMatcher,
                maxMatches
            )

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
            if (atPosition == RecyclerView.NO_POSITION && matchedItems.size == 2) {
                val ambiguousViewError = StringBuilder()
                ambiguousViewError.append(
                    String.format("Found more than one sub-view matching %s", viewHolderMatcher)
                )
                for (item in matchedItems) {
                    ambiguousViewError.append(item.toString() + "\n")
                }
                throw RuntimeException(ambiguousViewError.toString())
            }
            ScrollToPositionViewAction(matchedItems[selectIndex].position).perform(
                uiController,
                recyclerView
            )
            uiController.loopMainThreadUntilIdle()
        } catch (e: RuntimeException) {
            throw PerformException.Builder().withActionDescription(this.description)
                .withViewDescription(HumanReadables.describe(view)).withCause(e).build()
        }
    }
}

fun <VH : RecyclerView.ViewHolder> scrollToHolder(
    viewHolderMatcher: Matcher<VH>
): RecyclerViewActions.PositionableRecyclerViewAction =
    ScrollToViewAction(viewHolderMatcher)

fun <VH : RecyclerView.ViewHolder> scrollToHolder(
    viewHolderMatcher: Matcher<VH>,
    position: Int
): RecyclerViewActions.PositionableRecyclerViewAction {
    return ScrollToViewAction(viewHolderMatcher, position)
}

fun <VH : RecyclerView.ViewHolder> scrollTo(
    itemViewMatcher: Matcher<View>
): RecyclerViewActions.PositionableRecyclerViewAction {
    val viewHolderMatcher = viewHolderMatcher<VH>(itemViewMatcher)
    return ScrollToViewAction(viewHolderMatcher)
}

private fun RecyclerView.scrollItemAtPositionToCenter(
    uiController: UiController,
    position: Int
) {
    try {
        collapseAllAppBarsInParent()
        uiController.loopMainThreadUntilIdle()
    } catch (t: Throwable) {
        // collapseAllAppBarsInParent contains hard logic to find app bar in parent,
        // so we're just trying to collapse it. This action is optional
    }

    if (!viewForItemAtPositionExists(position)) {
        scrollToPosition(position)
        uiController.loopMainThreadUntilIdle()
    }

    try {
        layoutManager.findViewByPosition(position)
            .scrollToScrollableParentCenterPosition()
    } catch (t: Throwable) {
        // scrollToScrollableParentCenterPosition contains hard logic to find scrollable container,
        // so we're just trying to scroll to center of scrollable parent. This action is optional
    }

    uiController.loopMainThreadUntilIdle()
}

private fun RecyclerView.scrollToViewInsideItemAtPositionToCenter(
    uiController: UiController,
    position: Int,
    childId: Int
) {
    try {
        collapseAllAppBarsInParent()
        uiController.loopMainThreadUntilIdle()
    } catch (t: Throwable) {
        // collapseAllAppBarsInParent contains hard logic to find app bar in parent,
        // so we're just trying to collapse it. This action is optional
    }

    if (!viewForItemAtPositionExists(position)) {
        scrollToPosition(position)
        uiController.loopMainThreadUntilIdle()
    }

    try {
        layoutManager.findViewByPosition(position)
            .findViewById<View>(childId)
            .scrollToScrollableParentCenterPosition()
    } catch (t: Throwable) {
        // scrollToScrollableParentCenterPosition contains hard logic to find scrollable container,
        // so we're just trying to scroll to center of scrollable parent. This action is optional
    }

    uiController.loopMainThreadUntilIdle()
}

private fun RecyclerView.viewForItemAtPositionExists(position: Int): Boolean =
    findViewHolderForAdapterPosition(position) != null
