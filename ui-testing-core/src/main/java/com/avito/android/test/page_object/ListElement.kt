package com.avito.android.test.page_object

import androidx.test.espresso.action.SwipeDirection
import androidx.test.espresso.action.SwipeDirections.BOTTOM_TO_TOP
import androidx.test.espresso.action.SwipeDirections.LEFT_TO_RIGHT
import androidx.test.espresso.action.SwipeDirections.RIGHT_TO_LEFT
import androidx.test.espresso.action.SwipeDirections.TOP_TO_BOTTOM
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.CoordinatesProvider
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.PrecisionDescriber
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Swipe
import androidx.test.espresso.action.Swiper
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import com.avito.android.test.InteractionContext
import com.avito.android.test.RecyclerViewInteractionContext
import com.avito.android.test.SimpleInteractionContext
import com.avito.android.test.action.Actions
import com.avito.android.test.action.ActionsDriver
import com.avito.android.test.action.ActionsImpl
import com.avito.android.test.action.InteractionContextMatcherActions
import com.avito.android.test.action.InteractionContextPositionActions
import com.avito.android.test.checks.Checks
import com.avito.android.test.checks.ChecksDriver
import com.avito.android.test.checks.ChecksImpl
import com.avito.android.test.checks.InteractionContextMatcherChecksDriver
import com.avito.android.test.checks.InteractionContextPositionChecksDriver
import com.avito.android.test.espresso.EspressoActions
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

open class ListElement(interactionContext: InteractionContext) : ViewElement(interactionContext) {

    constructor(matcher: Matcher<View>) : this(SimpleInteractionContext(matcher))

    @Suppress("LeakingThis") // no problem with leaking this here
    override val checks = CheckLibrary(interactionContext)

    override val actions = ListActions(interactionContext)

    protected inline fun <reified T : PageObjectElement> listElement(
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

    @Deprecated("Use listElement(matcher: Matcher<View>, position: Int = 0) instead")
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

    @Deprecated("Use listElement(matcher: Matcher<View>, position: Int = 0) instead")
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

    /**
     * Interact with first matched element
     * Will scroll to element automatically
     */
    @Deprecated("Use listElement(matcher: Matcher<View>, position: Int = 0) instead")
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

    /**
     * Hide parent method [PageObject.element]
     */
    protected inline fun <reified T : PageObjectElement> element(matcher: Matcher<View>): T =
        throw RuntimeException("Use listElement(Matcher<View>) instead of element(Matcher<View>)")

    /**
     * Hide parent method [PageObject.element]
     */
    protected inline fun <reified T : PageObjectElement> element(): T =
        throw RuntimeException("Use listElement() instead of element()")

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
                    viewAction = EspressoActions.click()
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
                return GeneralLocation.CENTER to GeneralLocation.BOTTOM_CENTER
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
                        TOP_TO_BOTTOM -> GeneralLocation.CENTER to GeneralLocation.BOTTOM_CENTER
                        BOTTOM_TO_TOP -> GeneralLocation.CENTER to GeneralLocation.TOP_CENTER
                        LEFT_TO_RIGHT -> GeneralLocation.CENTER to GeneralLocation.CENTER_RIGHT
                        RIGHT_TO_LEFT -> GeneralLocation.CENTER to GeneralLocation.CENTER_LEFT
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
