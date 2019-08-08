package com.avito.android.ui.test

import android.support.test.espresso.matcher.ViewMatchers.withId
import com.avito.android.test.page_object.PageObject
import com.avito.android.test.page_object.ViewElement
import com.avito.android.ui.R

class ButtonsScreen : PageObject() {
    val enabledButton: ViewElement = element(withId(R.id.button_enabled))
    val enabledButtonClickIndicatorView: ViewElement = element(withId(R.id.button_enabled_clicked_text_view))
    val enabledButtonLongClickIndicatorView: ViewElement = element(withId(R.id.button_enabled_long_clicked_text_view))
    val disabledButton: ViewElement = element(withId(R.id.button_disabled))
}