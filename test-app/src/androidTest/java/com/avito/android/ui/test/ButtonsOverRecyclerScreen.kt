package com.avito.android.ui.test

import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.widget.FrameLayout
import com.avito.android.test.InteractionContext
import com.avito.android.test.page_object.ListElement
import com.avito.android.test.page_object.PageObject
import com.avito.android.test.page_object.ViewElement
import com.avito.android.ui.R

class ButtonsOverRecyclerScreen : PageObject() {

    val list: List = element(withId(R.id.recycler))

    class List(interactionContext: InteractionContext) : ListElement(interactionContext) {

        fun cellWithTitle(title: String): Cell = typedItemByMatcher(
            ViewMatchers.hasDescendant(ViewMatchers.withText(title))
        )

        fun cellAt(position: Int): Cell = typedItemByMatcher(
            ViewMatchers.isAssignableFrom(FrameLayout::class.java),
            position
        )

        class Cell(interactionContext: InteractionContext) : ViewElement(interactionContext) {
            val title: ViewElement = element(withId(R.id.title))
        }
    }
}
