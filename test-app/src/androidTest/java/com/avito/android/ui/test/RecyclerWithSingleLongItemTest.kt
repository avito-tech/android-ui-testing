package com.avito.android.ui.test

import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import com.avito.android.ui.RecyclerWithLongItemsActivity
import org.junit.Rule
import org.junit.Test
import com.avito.android.ui.R
import org.hamcrest.Matchers.allOf

class RecyclerWithSingleLongItemTest {

    @get:Rule
    val rule = screenRule<RecyclerWithLongItemsActivity>()

    @Test
    fun single_reversed_top() {
        test(
            DataSet(
                reversed = true,
                targetViews = listOf(R.id.target_view_top)
            )
        )
    }

    @Test
    fun single_top() {
        test(
            DataSet(
                reversed = false,
                targetViews = listOf(R.id.target_view_top)
            )
        )
    }

    @Test
    fun single_reversed_bottom() {
        test(
            DataSet(
                reversed = true,
                targetViews = listOf(R.id.target_view_bottom)
            )
        )
    }

    @Test
    fun single_bottom() {
        test(
            DataSet(
                reversed = false,
                targetViews = listOf(R.id.target_view_bottom)
            )
        )
    }

    @Test
    fun multiple() {
        test(
            DataSet(
                reversed = false,
                targetViews = listOf(
                    R.id.target_view_top,
                    R.id.target_view_center,
                    R.id.target_view_bottom
                )
            )
        )
    }

    @Test
    fun multiple_reversed() {
        test(
            DataSet(
                reversed = true,
                targetViews = listOf(
                    R.id.target_view_top,
                    R.id.target_view_center,
                    R.id.target_view_bottom
                )
            )
        )
    }

    @Test
    fun multiple_kek() {
        test(
            DataSet(
                reversed = false,
                targetViews = listOf(
                    R.id.target_view_center,
                    R.id.target_view_bottom,
                    R.id.target_view_top
                )
            )
        )
    }

    private fun test(dataSet: DataSet) {
        rule.launchActivity(RecyclerWithLongItemsActivity.intent(dataSet.reversed))

        with(Screen.recyclerWithSingleLongItemScreen.list) {
            for (targetView in dataSet.targetViews.shuffled()) {
                actions.actionOnChild(
                    position = 0,
                    targetChildViewId = targetView,
                    childMatcher = allOf(withId(targetView), isDisplayed()),
                    action = ViewActions.click()
                )
            }
        }
    }

    data class DataSet(
        val reversed: Boolean,
        val targetViews: List<Int>
    )
}
