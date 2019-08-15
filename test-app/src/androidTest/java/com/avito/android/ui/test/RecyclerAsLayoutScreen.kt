package com.avito.android.ui.test

import android.support.test.espresso.matcher.ViewMatchers.withId
import com.avito.android.test.InteractionContext
import com.avito.android.test.page_object.ListElement
import com.avito.android.test.page_object.PageObject
import com.avito.android.test.element.field.TextFieldElement
import com.avito.android.test.page_object.TextInputElement
import com.avito.android.test.page_object.ViewElement
import com.avito.android.ui.R

class RecyclerAsLayoutScreen : PageObject() {

    val list: List = element(withId(R.id.recycler))

    class List(interactionContext: InteractionContext) : ListElement(interactionContext) {
        val inputField: TextInputElement = listElement(withId(R.id.input_layout))
        val editText: TextFieldElement = listElement(withId(R.id.edit_text))
        val label: ViewElement = listElement(withId(R.id.title))
    }
}
