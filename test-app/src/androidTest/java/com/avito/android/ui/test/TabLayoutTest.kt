package com.avito.android.ui.test

import com.avito.android.ui.TabLayoutActivity
import org.junit.Rule
import org.junit.Test


class TabLayoutTest {

    @get:Rule
    val rule = screenRule<TabLayoutActivity>(launchActivity = true)

    @Test
    fun tabsCountIsFour() {
        Screen.tabLayoutScreen.tabs.checks.withTabsCount(4)
    }

    @Test
    fun selectTab0_tabIsDisplayed() {
        Screen.tabLayoutScreen.tabs.select(0)
        Screen.tabLayoutScreen.tabs.checks.withSelectedPosition(0)
    }
}