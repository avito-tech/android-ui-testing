package com.avito.android.ui.test

import android.support.test.espresso.matcher.ViewMatchers.withId
import com.avito.android.test.page_object.PageObject
import com.avito.android.test.page_object.TextField
import com.avito.android.ui.R

class EditTextScreen : PageObject() {
    val editText: TextField = element(withId(R.id.edit_text))
}
