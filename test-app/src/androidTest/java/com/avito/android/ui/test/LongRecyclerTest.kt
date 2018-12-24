package com.avito.android.ui.test

import com.avito.android.ui.LongRecyclerActivity
import com.avito.android.ui.RecyclerAsLayoutActivity
import org.hamcrest.Matchers.greaterThan
import org.junit.Rule
import org.junit.Test

class LongRecyclerTest {

    @get:Rule
    val rule = screenRule<LongRecyclerActivity>()

    private fun givenItemsList(n: Int) = (0..n).asSequence().map { "label_$it" }.toList()

    @Test
    fun textInput_isAccessible() {

        val itemsCount = 500

        rule.launchActivity(
            RecyclerAsLayoutActivity.intent(
                arrayListOf<String>().apply { addAll(givenItemsList(itemsCount)) }
            )
        )

        Screen.longRecycler.actions.smoothScrollToPosition(itemsCount)
        Thread.sleep(1000L)

        Screen.longRecycler.checks.firstVisiblePosition(greaterThan(400))
    }
}
