package com.avito.android.ui.test

import android.support.test.espresso.matcher.ViewMatchers.withId
import com.avito.android.test.page_object.PageObject
import com.avito.android.test.element.field.TextFieldElement
import com.avito.android.ui.R

class EditTextScreen : PageObject() {
    val editText: TextFieldElement = element(withId(R.id.edit_text))
}
