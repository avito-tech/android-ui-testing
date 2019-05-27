package com.avito.android.test.espresso.action.recycler

import android.support.test.espresso.util.HumanReadables
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import java.util.ArrayList
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * Finds positions of items in [RecyclerView] which is matching given viewHolderMatcher.
 *
 * @param recyclerView recycler view which is hosting items.
 * @param viewHolderMatcher a
 * [`Matcher`](http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matcher.html) that matches
 * an item view in [RecyclerView]
 * @param viewHolderType as type token for checking view holder type get from view hierarchy
 * @return list of MatchedItem which contains position and description of items in recyclerView.
 * @throws RuntimeException if more than one item or item could not be found. </VH>
 */
internal fun <VH : RecyclerView.ViewHolder> itemsMatching(
    recyclerView: RecyclerView,
    viewHolderType: Class<VH>,
    viewHolderMatcher: Matcher<VH>,
    max: Int
): List<MatchedItem> {
    val adapter = recyclerView.adapter

    val viewHolderCache = SparseArray<VH>()
    val shownViewHolders = SparseArray<VH>()

    val matchedItems = ArrayList<MatchedItem>()

    // We can avoid fake binding for already shown elements because we can get bound view holder
    // from hierarchy.
    for (position in 0 until adapter.itemCount) {
        val viewHolderForPosition: RecyclerView.ViewHolder? =
            recyclerView.findViewHolderForAdapterPosition(position)

        if (viewHolderForPosition != null && viewHolderType.isInstance(viewHolderForPosition)) {
            shownViewHolders.put(position, viewHolderType.cast(viewHolderForPosition))
        }
    }

    for (position in 0 until adapter.itemCount) {
        var viewHolder: VH? = shownViewHolders.get(position)

        if (viewHolder == null) {
            val itemType = adapter.getItemViewType(position)

            viewHolder = viewHolderCache.get(itemType)

            if (viewHolder == null) {

                @Suppress("UNCHECKED_CAST")
                viewHolder = adapter.createViewHolder(recyclerView, itemType) as VH

                /**
                 * It allows production code to understand, that bindViewHolder has called by
                 * fake rendering process (for find element in recycler view before it will be shown on
                 * real user screen).
                 */
                (viewHolder as RecyclerView.ViewHolder).itemView.setTag(
                    FAKE_RENDERING_VIEW_HOLDER_TAG_KEY,
                    FAKE_RENDERING_VIEW_HOLDER_TAG_VALUE
                )

                viewHolderCache.put(itemType, viewHolder)
            }
            // Bind data to ViewHolder and apply matcher to view descendants.
            @Suppress("UNCHECKED_CAST")
            adapter.bindViewHolder(viewHolder, position)
        }

        if (viewHolderMatcher.matches(viewHolder)) {
            matchedItems.add(
                MatchedItem(
                    position = position,
                    description = HumanReadables.getViewHierarchyErrorMessage(
                        viewHolder.itemView, null,
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

internal class MatchedItem(val position: Int, val description: String) {

    override fun toString(): String = description
}

private const val FAKE_RENDERING_VIEW_HOLDER_TAG_KEY = Integer.MAX_VALUE - 228
private const val FAKE_RENDERING_VIEW_HOLDER_TAG_VALUE = "RENDERED_FOR_FAKE_RUN"