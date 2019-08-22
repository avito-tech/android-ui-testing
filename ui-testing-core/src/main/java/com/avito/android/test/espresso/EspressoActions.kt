package com.avito.android.test.espresso

import android.support.test.espresso.PerformException
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.GeneralSwipeAction
import android.support.test.espresso.action.PrecisionDescriber
import android.support.test.espresso.action.Press
import android.support.test.espresso.action.Swipe
import android.support.test.espresso.action.SwipeDirection
import android.support.test.espresso.action.Swiper
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.actionWithAssertions
import android.view.View
import com.avito.android.test.UITestConfig
import com.avito.android.test.element.field.actions.TypeText
import com.avito.android.test.espresso.action.ActionOnEnabledElement
import com.avito.android.test.espresso.action.WaitForIdleAction
import com.avito.android.test.espresso.action.click.inProcessClickAction
import com.avito.android.test.espresso.action.click.inProcessLongClickAction
import com.avito.android.test.espresso.action.scroll.ScrollToIfPossibleAction
import org.hamcrest.Matcher
import org.hamcrest.core.IsAnything

object EspressoActions {

    fun typeText(stringToBeTyped: String): ViewAction = actionWithAssertions(TypeText(stringToBeTyped))

    /**
     * Enables scrolling to the given view.
     * Less strict version of ScrollToAction
     * If view not a descendant of a ScrollView - nothing happens
     */
    fun scrollIfPossible(): ViewAction = actionWithAssertions(ScrollToIfPossibleAction())

    /**
     * Hack: waits for main thread to be idle
     */
    fun waitForIdle(): ViewAction = actionWithAssertions(WaitForIdleAction())

    /**
     * Expose espresso API
     */
    fun swipe(
        direction: SwipeDirection,
        speed: Swiper = Swipe.FAST,
        precision: PrecisionDescriber = Press.FINGER
    ): ViewAction {
        val providers = direction.toCoordinateProvider()
        return actionWithAssertions(
            GeneralSwipeAction(
                speed,
                providers.first,
                providers.second,
                precision
            )
        )
    }

    fun click(type: UITestConfig.ClickType = UITestConfig.clicksType): ViewAction = ActionOnEnabledElement(
        when (type) {
            is UITestConfig.ClickType.EspressoClick -> when (type.rollbackPolicy) {

                is UITestConfig.ClickType.EspressoClick.ClickRollbackPolicy.DoNothing -> ViewActions.click()

                is UITestConfig.ClickType.EspressoClick.ClickRollbackPolicy.TryOneMoreClick -> ViewActions.click(
                    ActionOnEnabledElement(ViewActions.click())
                )

                is UITestConfig.ClickType.EspressoClick.ClickRollbackPolicy.Fail -> ViewActions.click(
                    object : ViewAction {
                        override fun getDescription(): String = "fake fail action after click interpreted as long click"

                        override fun getConstraints(): Matcher<View> = IsAnything()

                        override fun perform(uiController: UiController?, view: View?) {
                            throw PerformException.Builder()
                                .withActionDescription("click interpreted as long click")
                                .withViewDescription("view")
                                .build()
                        }
                    }
                )
            }

            is UITestConfig.ClickType.InProcessClick -> inProcessClickAction()
        }
    )

    fun longClick(type: UITestConfig.ClickType = UITestConfig.clicksType): ViewAction = ActionOnEnabledElement(
        when (type) {
            is UITestConfig.ClickType.EspressoClick -> ViewActions.longClick()
            is UITestConfig.ClickType.InProcessClick -> inProcessLongClickAction()
        }
    )
}
