package com.avito.android.ui.test

import android.support.test.espresso.matcher.ViewMatchers.withId
import com.avito.android.test.page_object.PageObject
import com.avito.android.test.element.field.TextFieldElement
import com.avito.android.ui.R

class EditTextScreen : PageObject() {
    val editText: TextFieldElement = element(withId(R.id.edit_text))
    val editText1: TextFieldElement = element(withId(R.id.edit_text1))
    val phoneNumberText: TextFieldElement = element(withId(R.id.phone_number_edit_text1))
}
