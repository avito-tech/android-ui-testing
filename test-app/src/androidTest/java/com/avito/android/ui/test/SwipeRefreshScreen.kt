package com.avito.android.ui.test

import android.support.test.espresso.matcher.ViewMatchers.withId
import com.avito.android.test.page_object.ListElement
import com.avito.android.test.page_object.SwipeRefreshElement
import com.avito.android.ui.R

class SwipeRefreshScreen {

    val list = ListElement(withId(R.id.swipe_refresh))

    val swipeRefreshElement = SwipeRefreshElement(withId(R.id.swipe_refresh))
}
