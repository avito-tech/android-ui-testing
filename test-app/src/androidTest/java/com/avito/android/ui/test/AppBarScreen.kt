package com.avito.android.ui.test

import android.support.test.espresso.matcher.ViewMatchers
import com.avito.android.test.page_object.AppBarElement
import com.avito.android.test.page_object.ToolbarElement
import com.avito.android.test.page_object.ViewElement
import com.avito.android.ui.R

class AppBarScreen {

    val appBar = AppBarElement(ViewMatchers.withId(R.id.appbar))

    val toolbar = ToolbarElement(ViewMatchers.withId(R.id.toolbar))

    val testView = ViewElement(ViewMatchers.withId(R.id.view_in_collapsing_toolbar))
}
