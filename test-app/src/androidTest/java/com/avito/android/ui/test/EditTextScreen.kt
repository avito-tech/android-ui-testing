package com.avito.android.ui.test

import android.support.test.espresso.matcher.ViewMatchers
import com.avito.android.test.page_object.TextField
import com.avito.android.ui.R

class EditTextScreen {

    val editText = TextField(ViewMatchers.withId(R.id.edit_text))
}
