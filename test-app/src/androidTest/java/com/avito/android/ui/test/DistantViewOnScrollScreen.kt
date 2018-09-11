package com.avito.android.ui.test

import android.support.test.espresso.matcher.ViewMatchers
import com.avito.android.test.page_object.PageObject
import com.avito.android.test.page_object.ViewElement
import com.avito.android.ui.R

class DistantViewOnScrollScreen : PageObject {

    val scroll = ViewElement(ViewMatchers.withId(R.id.scroll))

    val view = ViewElement(ViewMatchers.withId(R.id.view))
}
