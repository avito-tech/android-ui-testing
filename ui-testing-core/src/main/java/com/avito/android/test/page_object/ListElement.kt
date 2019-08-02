package com.avito.android.test.page_object

import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.CoordinatesProvider
import android.support.test.espresso.action.GeneralLocation.BOTTOM_CENTER
import android.support.test.espresso.action.GeneralLocation.CENTER
import android.support.test.espresso.action.GeneralLocation.CENTER_LEFT
import android.support.test.espresso.action.GeneralLocation.CENTER_RIGHT
import android.support.test.espresso.action.GeneralLocation.TOP_CENTER
import android.support.test.espresso.action.PrecisionDescriber
import android.support.test.espresso.action.Press
import android.support.test.espresso.action.Swipe
import android.support.test.espresso.action.SwipeDirection
import android.support.test.espresso.action.SwipeDirections.BOTTOM_TO_TOP
import android.support.test.espresso.action.SwipeDirections.LEFT_TO_RIGHT
import android.support.test.espresso.action.SwipeDirections.RIGHT_TO_LEFT
import android.support.test.espresso.action.SwipeDirections.TOP_TO_BOTTOM
import android.support.test.espresso.action.Swiper
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers.hasDescendant
import android.support.v7.widget.RecyclerView
import android.view.View
import com.avito.android.test.InteractionContext
import com.avito.android.test.RecyclerViewInteractionContext
import com.avito.android.test.SimpleInteractionContext
import com.avito.android.test.action.Actions
import com.avito.android.test.action.ActionsDriver
import com.avito.android.test.action.ActionsImpl
import com.avito.android.test.action.LibraryViewActions
import com.avito.android.test.action.InteractionContextMatcherActions
import com.avito.android.test.action.InteractionContextPositionActions
import com.avito.android.test.checks.Checks
import com.avito.android.test.checks.ChecksDriver
import com.avito.android.test.checks.ChecksImpl
import com.avito.android.test.checks.InteractionContextMatcherChecksDriver
import com.avito.android.test.checks.InteractionContextPositionChecksDriver
import com.avito.android.test.espresso.action.RecyclerSpanCountAction
import com.avito.android.test.espresso.action.RecyclerViewHorizontalOffsetAction
import com.avito.android.test.espresso.action.RecyclerViewItemsCountAction
import com.avito.android.test.espresso.action.RecyclerViewVerticalOffsetAction
import com.avito.android.test.espresso.action.ViewGetTranslationYAction
import com.avito.android.test.espresso.action.recycler.SmoothScrollToPositionViewAction
import com.avito.android.test.espresso.action.recycler.ViewActionOnItemAtPosition
import com.avito.android.test.espresso.action.recycler.actionOnHolderItem
import com.avito.android.test.espresso.action.recycler.scrollToElementInsideRecyclerViewItem
import com.avito.android.test.matcher.RecyclerViewMatcher
import com.avito.android.test.matcher.ViewGroupMatcher
import com.forkingcode.espresso.contrib.DescendantViewActions
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.greaterThan
import org.hamcrest.core.AnyOf.anyOf

open class ListElement(interactionContext: InteractionContext) :
    ViewElement(interactionContext) {

    constructor(matcher: Matcher<View>) : this(SimpleInteractionContext(matcher))

    @Suppress("LeakingThis") // no problem with leaking this here
    override val checks = CheckLibrary(interactionContext)

    override val actions = ListActions(interactionContext)

    /**
     * Interact with first matched element
     * Will scroll to element automatically
     */
    protected inline fun <T> typedItemByMatcher(
        itemMatcher: Matcher<View>,
        factory: (
            matcher: Matcher<View>,
            actions: Actions,
            checks: Checks,
            childFactory: (Matcher<View>) -> PageObjectElement
        ) -> T
    ): T {
        return factory(
            itemMatcher,
            InteractionContextMatcherActions(interactionContext, itemMatcher, itemMatcher),
            ChecksImpl(
                InteractionContextMatcherChecksDriver(
                    interactionContext,
                    itemMatcher,
                    itemMatcher
                )
            )
        ) { childMatcher ->
            ViewElement(
                childMatcher,
                actions = InteractionContextMatcherActions(
                    interactionContext,
                    itemMatcher,
                    childMatcher
                ),
                checks = ChecksImpl(
                    InteractionContextMatcherChecksDriver(
                        interactionContext,
                        itemMatcher,
                        childMatcher
                    )
                )
            )
        }
    }

    // todo make me lazy
    protected inline fun <reified T : PageObjectElement> typedItemByMatcher(
        matcher: Matcher<View>,
        position: Int = 0
    ): T =
        T::class.java.getConstructor(InteractionContext::class.java)
            .newInstance(
                RecyclerViewInteractionContext(
                    interactionContext = interactionContext,
                    cellMatcher = anyOf(hasDescendant(matcher), matcher),
                    childMatcher = matcher,
                    position = position
                )
            )

    protected inline fun <T> typedItemAtPosition(
        itemMatcher: Matcher<View>,
        position: Int,
        factory: (
            matcher: Matcher<View>,
            actions: Actions,
            checks: Checks,
            childFactory: (Matcher<View>) -> PageObjectElement
        ) -> T
    ): T {

        return factory(
            itemMatcher,
            InteractionContextPositionActions(interactionContext, position, itemMatcher),
            ChecksImpl(
                InteractionContextPositionChecksDriver(
                    interactionContext,
                    position,
                    itemMatcher
                )
            )
        ) { childMatcher ->
            ViewElement(
                childMatcher,
                actions = InteractionContextPositionActions(
                    interactionContext,
                    position,
                    childMatcher
                ),
                checks = ChecksImpl(
                    InteractionContextPositionChecksDriver(
                        interactionContext,
                        position,
                        childMatcher
                    )
                )
            )
        }
    }

    class ListActions private constructor(
        private val driver: ActionsDriver,
        private val actions: Actions
    ) :
        Actions by actions {

        constructor(driver: ActionsDriver) : this(driver, ActionsImpl(driver))

        fun scrollToPosition(position: Int) {
            driver.perform(
                com.avito.android.test.espresso.action.recycler.scrollToPosition(
                    position
                )
            )
        }

        fun smoothScrollToPosition(position: Int = 0) {
            driver.perform(
                SmoothScrollToPositionViewAction(
                    position
                )
            )
        }

        fun scrollToEnd() = scrollToPosition(items - 1)

        fun scrollToHolder(holder: Matcher<RecyclerView.ViewHolder>) {
            driver.perform(
                com.avito.android.test.espresso.action.recycler.scrollToHolder(
                    viewHolderMatcher = holder,
                    viewHolderType = RecyclerView.ViewHolder::class.java
                )
            )
        }

        fun clickOnHolder(holder: Matcher<RecyclerView.ViewHolder>, position: Int = 0) {
            driver.perform(
                actionOnHolderItem(
                    viewHolderMatcher = holder,
                    viewHolderType = RecyclerView.ViewHolder::class.java,
                    viewAction = LibraryViewActions.click()
                ).atPosition(position)
            )
        }

        fun scrollToChild(position: Int = 0, targetChildViewId: Int) {
            driver.perform(
                scrollToElementInsideRecyclerViewItem(
                    position = position,
                    childViewId = targetChildViewId
                )
            )
        }

        fun actionOnChild(
            position: Int = 0,
            targetChildViewId: Int,
            childMatcher: Matcher<View>,
            action: ViewAction
        ) {
            scrollToChild(position, targetChildViewId)
            driver.perform(
                ViewActionOnItemAtPosition<RecyclerView.ViewHolder>(
                    position,
                    DescendantViewActions.performDescendantAction(childMatcher, action)
                )
            )
        }

        val translationY
            get() = ViewGetTranslationYAction().also { driver.perform(it) }.translationY

        val items: Int
            get() = RecyclerViewItemsCountAction().also { driver.perform(it) }.result

        @Deprecated("Use getItems instead")
        fun countItems() = RecyclerViewItemsCountAction().also { driver.perform(it) }.result

        val verticalOffset: Int
            get() = RecyclerViewVerticalOffsetAction().also { driver.perform(it) }.result

        val horizontalOffset: Int
            get() = RecyclerViewHorizontalOffsetAction().also { driver.perform(it) }.result

        /**
         * Refreshes recycler view by pressing in it's center and pulling down. Note: it does not perform
         * [scrollToPosition] operation before - developer should do it himself!
         */
        fun pullToRefresh() = actions.swipe(object : SwipeDirection {
            override fun toCoordinateProvider(): Pair<CoordinatesProvider, CoordinatesProvider> {
                return CENTER to BOTTOM_CENTER
            }
        }, Swipe.SLOW, Press.FINGER)

        override fun swipe(
            direction: SwipeDirection,
            speed: Swiper,
            precision: PrecisionDescriber
        ) {
            actions.swipe(object : SwipeDirection {
                override fun toCoordinateProvider():
                    Pair<CoordinatesProvider, CoordinatesProvider> {
                    return when (direction) {
                        TOP_TO_BOTTOM -> CENTER to BOTTOM_CENTER
                        BOTTOM_TO_TOP -> CENTER to TOP_CENTER
                        LEFT_TO_RIGHT -> CENTER to CENTER_RIGHT
                        RIGHT_TO_LEFT -> CENTER to CENTER_LEFT
                        else -> throw IllegalArgumentException(
                            "Can't do \"swipe\". Argument $direction is not supported"
                        )
                    }
                }
            }, Swipe.FAST, Press.FINGER)
        }

        /**
         * Use only for recyclers with GridLayoutManager
         */
        fun getSpanCount() = RecyclerSpanCountAction().also { driver.perform(it) }.result
    }

    class CheckLibrary(private val driver: ChecksDriver) : Checks by ChecksImpl(driver) {

        override fun withChildCount(countMatcher: Matcher<Int>) {
            driver.check(ViewAssertions.matches(ViewGroupMatcher().hasChildren(countMatcher)))
        }

        override fun withChildCountEquals(count: Int) = withChildCount(`is`(count))

        fun withItemsCount(countMatcher: Matcher<Int>) {
            driver.check(ViewAssertions.matches(RecyclerViewMatcher().itemsInList(countMatcher)))
        }

        fun withItemsCount(count: Int) {
            withItemsCount(equalTo(count))
        }

        fun firstVisiblePosition(positionMatcher: Matcher<Int>) {
            driver.check(
                ViewAssertions.matches(
                    RecyclerViewMatcher().firstVisibleItemPosition(
                        positionMatcher
                    )
                )
            )
        }

        fun isNotEmpty() = withItemsCount(greaterThan(0))
    }
}
