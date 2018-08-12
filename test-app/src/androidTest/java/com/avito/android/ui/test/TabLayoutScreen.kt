package com.avito.android.ui.test

import android.support.test.espresso.matcher.ViewMatchers
import com.avito.android.test.page_object.TabLayoutElement
import com.avito.android.ui.R

class TabLayoutScreen {

    val tabs = TabLayoutElement(ViewMatchers.withId(R.id.tabs))
}
