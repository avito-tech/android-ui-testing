package com.avito.android.ui.test

import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import android.widget.FrameLayout
import com.avito.android.test.InteractionContext
import com.avito.android.test.page_object.ListElement
import com.avito.android.test.page_object.PageObject
import com.avito.android.test.page_object.ViewElement
import com.avito.android.ui.R

class ButtonsOverRecyclerScreen : PageObject() {

    val list: List = element(withId(R.id.recycler))

    class List(interactionContext: InteractionContext) : ListElement(interactionContext) {

        fun cellWithTitle(title: String): Cell = listElement(hasDescendant(withText(title)))
        fun cellAt(position: Int): Cell =
            listElement(isAssignableFrom(FrameLayout::class.java), position)

        class Cell(interactionContext: InteractionContext) : ViewElement(interactionContext) {
            val title: ViewElement = element(withId(R.id.title))
        }
    }
}
