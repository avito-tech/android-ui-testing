package com.avito.android.test.action

import android.support.test.espresso.action.PrecisionDescriber
import android.support.test.espresso.action.Press
import android.support.test.espresso.action.Swipe
import android.support.test.espresso.action.SwipeDirection
import android.support.test.espresso.action.Swiper

interface Actions {

    fun click()

    fun longClick()

    fun scrollTo()

    fun swipe(direction: SwipeDirection, speed: Swiper = Swipe.FAST, precision: PrecisionDescriber = Press.FINGER)

    fun read(): String
}