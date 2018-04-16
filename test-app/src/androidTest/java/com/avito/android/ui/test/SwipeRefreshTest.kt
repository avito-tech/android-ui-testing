package com.avito.android.ui.test

import com.avito.android.ui.SwipeRefreshActivity
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test

class SwipeRefreshTest {

    @Rule @JvmField
    val rule = screenRule<SwipeRefreshActivity>()

    @Test
    fun recyclerView_refresh_onPullToRefresh() {
        rule.launchActivity(null)

        Screen.swipeRefresh.list.actions.pullToRefresh()

        assertThat(rule.activity.refreshedTimes, equalTo(1))
    }
}