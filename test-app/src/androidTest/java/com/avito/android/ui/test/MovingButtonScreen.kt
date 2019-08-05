package com.avito.android.ui.test

import android.support.test.espresso.matcher.ViewMatchers
import com.avito.android.test.page_object.PageObject
import com.avito.android.test.page_object.ViewElement
import com.avito.android.ui.R

class MovingButtonScreen : PageObject {

    val movedButton = ViewElement(ViewMatchers.withId(R.id.moved_button))

    val movedButtonClickIndicatorView =
        ViewElement(ViewMatchers.withId(R.id.moved_button_clicked_text_view))
}