package com.avito.android.test.action

import android.support.test.espresso.action.PrecisionDescriber
import android.support.test.espresso.action.SwipeDirection
import android.support.test.espresso.action.Swiper
import com.avito.android.test.espresso.LibraryViewActions
import com.avito.android.test.espresso.action.TextViewReadAction

class ActionsImpl(private val driver: ActionsDriver) : Actions {

    override fun click() {
        driver.perform(
            LibraryViewActions.scrollIfPossible(),
            LibraryViewActions.click()
        )
    }

    override fun longClick() {
        driver.perform(
            LibraryViewActions.scrollIfPossible(),
            LibraryViewActions.longClick()
        )
    }

    override fun scrollTo() {
        driver.perform(LibraryViewActions.scrollIfPossible())
    }

    override fun swipe(direction: SwipeDirection, speed: Swiper, precision: PrecisionDescriber) {
        driver.perform(LibraryViewActions.swipe(direction, speed, precision))

        // FIXME
        Thread.sleep(1000)
    }

    override fun read(allowBlank: Boolean): String =
        TextViewReadAction.getResult(allowBlank) { action -> driver.perform(action) }
}
