package com.avito.android.test.action

import android.support.test.espresso.action.PrecisionDescriber
import android.support.test.espresso.action.SwipeDirection
import android.support.test.espresso.action.Swiper
import android.support.test.espresso.web.sugar.Web
import android.support.test.espresso.web.webdriver.DriverAtoms

class WebElementActions(private val interaction: Web.WebInteraction<Void>) : Actions {

    override fun click() {
        interaction.perform(DriverAtoms.webScrollIntoView())
        interaction.perform(DriverAtoms.webClick())
    }

    @Deprecated("Not supported on the Web View", replaceWith = ReplaceWith("click()"))
    override fun longClick() {
        throw UnsupportedOperationException("Long click is not supported on the Web View")
    }

    override fun scrollTo() {
        interaction.perform(DriverAtoms.webScrollIntoView())
    }

    @Deprecated("Not supported on the Web View")
    override fun swipe(direction: SwipeDirection, speed: Swiper, precision: PrecisionDescriber) {
        throw UnsupportedOperationException("Swipe is not supported on the Web View")
    }

    override fun read(): String {
        return interaction.perform(DriverAtoms.getText()).get()
    }

    fun write(text: String) {
        interaction.perform(DriverAtoms.webKeys(text))
    }

}