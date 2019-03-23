package com.avito.android.ui.test

import android.support.test.espresso.action.SwipeDirections
import com.avito.android.ui.ButtonsOverRecyclerActivity
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.greaterThan
import org.junit.Rule
import org.junit.Test

class ButtonsOverRecyclerTest {

    @get:Rule
    val rule = screenRule<ButtonsOverRecyclerActivity>(launchActivity = true)

    @Test
    fun listElement_swipe_RecyclerViewBehindButtons() {
        with(Screen.buttonsOverRecycler.list) {
            actions.swipe(SwipeDirections.BOTTOM_TO_TOP)
            checks.firstVisiblePosition(greaterThan(0))
            // on some devices swipe to top may end on half way
            repeat(3) { actions.swipe(SwipeDirections.TOP_TO_BOTTOM) }
            checks.firstVisiblePosition(equalTo(0))
        }
    }

    @Test
    fun listElement_elementClicked_whenThereIsOverlappedButton() {
        Screen.buttonsOverRecycler.list.cellAt(60).click()
    }
}
