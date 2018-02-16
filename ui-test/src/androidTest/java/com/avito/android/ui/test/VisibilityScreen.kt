package com.avito.android.ui.test

import android.support.test.espresso.matcher.ViewMatchers
import com.avito.android.test.page_object.PageObject
import com.avito.android.test.page_object.PageObjectElement
import com.avito.android.ui.R

class VisibilityScreen : PageObject() {

    val label = PageObjectElement(ViewMatchers.withId(R.id.text))
}