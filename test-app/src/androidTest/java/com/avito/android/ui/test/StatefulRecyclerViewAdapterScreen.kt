package com.avito.android.ui.test

import android.support.test.espresso.matcher.ViewMatchers.hasDescendant
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import com.avito.android.test.InteractionContext
import com.avito.android.test.page_object.ListElement
import com.avito.android.test.page_object.PageObject
import com.avito.android.test.page_object.ViewElement
import com.avito.android.ui.R

class StatefulRecyclerViewAdapterScreen : PageObject() {

    val list: List = element(withId(R.id.recycler))

    class List(override val interactionContext: InteractionContext) : ListElement(interactionContext) {

        fun cellWithTitle(title: String): Cell = typedItemByMatcher(hasDescendant(withText(title)))

        fun cellWithTitleCreatedByRecyclerViewInteractionContext(title: String): ViewElement =
            typedItemByMatcher(
                hasDescendant(withText(title))
            )

        class Cell(interactionContext: InteractionContext) : ViewElement(interactionContext) {
            val title: ViewElement = element(withId(R.id.title))
            val title2: ViewElement = element(withId(R.id.title2))
        }
    }
}