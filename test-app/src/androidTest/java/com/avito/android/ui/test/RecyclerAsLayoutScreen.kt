package com.avito.android.ui.test

import android.support.test.espresso.matcher.ViewMatchers.withId
import com.avito.android.test.page_object.ListElement
import com.avito.android.test.page_object.PageObjectElement
import com.avito.android.test.page_object.TextField
import com.avito.android.test.page_object.TextInputElement
import com.avito.android.test.page_object.ViewElement
import com.avito.android.ui.R

class RecyclerAsLayoutScreen : ListElement(withId(R.id.recycler)) {

    val inputField: TextInputElement
        get() = typedItemByMatcher(withId(R.id.input_layout))

    val editText: TextField
        get() = typedItemByMatcher(withId(R.id.edit_text))

    val label: PageObjectElement
        get() = typedItemByMatcher<ViewElement>(withId(R.id.title))
}
