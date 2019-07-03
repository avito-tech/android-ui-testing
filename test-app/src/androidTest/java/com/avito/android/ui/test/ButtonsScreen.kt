package com.avito.android.ui.test

import android.support.test.espresso.matcher.ViewMatchers
import com.avito.android.test.page_object.PageObject
import com.avito.android.test.page_object.ViewElement
import com.avito.android.ui.R

class ButtonsScreen : PageObject {

    val enabledButton = ViewElement(ViewMatchers.withId(R.id.button_enabled))

    val disabledButton = ViewElement(ViewMatchers.withId(R.id.button_disabled))
}