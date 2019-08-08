package com.avito.android.ui.test

import com.avito.android.ui.RecyclerInRecyclerActivity
import org.junit.Rule
import org.junit.Test

class RecyclerInRecyclerTest {

    @get:Rule
    val rule = screenRule<RecyclerInRecyclerActivity>()

    @Test
    fun typedItemAtPosition_foundFirstValue() {
        rule.launchActivity(RecyclerInRecyclerActivity.intent(arrayListOf("0", "1", "2")))

        Screen.recyclerInRecycler.list.horizontalList.cellAt(position = 0)
            .title.checks.displayedWithText("0")
    }

    @Test
    fun typedItemWithMatcher_foundFirstValue() {
        rule.launchActivity(RecyclerInRecyclerActivity.intent(arrayListOf("0", "1", "2")))

        Screen.recyclerInRecycler.list.horizontalList.cellWithTitle("0")
            .title.checks.displayedWithText("0")
    }

    @Test
    fun typedItemAtPosition_foundThirdValue() {
        rule.launchActivity(RecyclerInRecyclerActivity.intent(arrayListOf("0", "1", "2")))

        Screen.recyclerInRecycler.list.horizontalList.cellAt(position = 2)
            .title.checks.displayedWithText("2")
    }

    @Test
    fun typedItemWithMatcher_foundThirdValue() {
        rule.launchActivity(RecyclerInRecyclerActivity.intent(arrayListOf("0", "1", "2")))

        Screen.recyclerInRecycler.list.horizontalList.cellWithTitle("2")
            .title.checks.displayedWithText("2")
    }
}
