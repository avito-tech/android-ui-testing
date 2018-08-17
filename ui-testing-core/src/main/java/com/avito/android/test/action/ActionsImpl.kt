package com.avito.android.test.action

import android.support.test.espresso.action.PrecisionDescriber
import android.support.test.espresso.action.SwipeDirection
import android.support.test.espresso.action.Swiper
import android.support.test.espresso.action.ViewActions
import com.avito.android.test.espresso.EspressoActions
import com.avito.android.test.espresso.action.TextViewReadAction

class ActionsImpl(private val driver: ActionsDriver) : Actions {

    override fun click() {
        driver.perform(
            EspressoActions.scrollIfPossible(),
            ViewActions.click()
        )
    }

    override fun longClick() {
        driver.perform(
            EspressoActions.scrollIfPossible(),
            ViewActions.longClick()
        )
    }

    override fun scrollTo() {
        driver.perform(EspressoActions.scrollIfPossible())
    }

    override fun swipe(direction: SwipeDirection, speed: Swiper, precision: PrecisionDescriber) {
        driver.perform(EspressoActions.swipe(direction, speed, precision))

        // FIXME
        Thread.sleep(1000)
    }

    override fun read(allowBlank: Boolean): String =
        TextViewReadAction.getResult(allowBlank) { action -> driver.perform(action) }
}
