package com.avito.android.ui.test

import com.avito.android.test.espresso.action.ScrollViewScrollToEndAction
import com.avito.android.ui.DistantViewOnScrollActivity
import org.junit.Rule
import org.junit.Test

class ScrollViewScrollToEndTest {

    @get:Rule
    val rule = screenRule<DistantViewOnScrollActivity>(launchActivity = true)

    @Test
    fun scrollToEnd_viewIsDisplayed_scrollToEndOfScreen() {
        Screen.distantViewOnScroll.scroll.interaction.perform(ScrollViewScrollToEndAction())

        Screen.distantViewOnScroll.view.checks.isDisplayed()
    }

    @Test
    fun isVisible_viewIsNotDisplayed() {
        Screen.distantViewOnScroll.view.checks.isNotDisplayed()
    }
}
