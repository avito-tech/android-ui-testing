package com.avito.android.test.espresso

import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.GeneralSwipeAction
import android.support.test.espresso.action.PrecisionDescriber
import android.support.test.espresso.action.Press
import android.support.test.espresso.action.Swipe
import android.support.test.espresso.action.SwipeDirection
import android.support.test.espresso.action.Swiper
import android.support.test.espresso.action.ViewActions.actionWithAssertions
import com.avito.android.test.espresso.action.SafeTypeTextAction
import com.avito.android.test.espresso.action.ScrollToIfPossibleAction
import com.avito.android.test.espresso.action.WaitForIdleAction

/**
 * Avito in-house ViewActions
 */
object EspressoActions {

    /**
     * Devices support only latin symbols by default
     * We could possibly enable cyrillic keyboard on all emulators / firebase devices / developer devices/emulators,
     * but better approach is to fallback to replaceText method if IME does not support cyrillic input.
     * This way we can enable cyrillic natively on devices to make checks more "real", but write cyrillic input tests without problems
     */
    fun safeTypeText(stringToBeTyped: String, tapBeforeInput: Boolean = true): ViewAction {
        return actionWithAssertions(SafeTypeTextAction(stringToBeTyped, tapBeforeInput))
    }

    /**
     * Enables scrolling to the given view.
     * Less strict version of ScrollToAction
     * If view not a descendant of a ScrollView - nothing happens
     */
    fun scrollIfPossible(): ViewAction {
        return actionWithAssertions(ScrollToIfPossibleAction())
    }

    /**
     * Hack: waits for main thread to be idle
     */
    fun waitForIdle(): ViewAction {
        return actionWithAssertions(WaitForIdleAction())
    }

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
}