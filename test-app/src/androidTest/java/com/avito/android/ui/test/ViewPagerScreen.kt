package com.avito.android.ui.test

import android.support.test.espresso.matcher.ViewMatchers.withId
import com.avito.android.test.InteractionContext
import com.avito.android.test.page_object.PageObject
import com.avito.android.test.page_object.ViewElement
import com.avito.android.test.page_object.ViewPagerElement
import com.avito.android.ui.R

class ViewPagerScreen : PageObject() {

    val pager: Pager = element(withId(R.id.view_pager))

    class Pager(interactionContext: InteractionContext) : ViewPagerElement(interactionContext) {
        val currentEvenPage: EvenPage = currentPageElement(withId(R.id.even_page_root))
        val currentOddPage: OddPage = currentPageElement(withId(R.id.odd_page_root))
    }

    class EvenPage(interactionContext: InteractionContext) : ViewElement(interactionContext) {
        val label: ViewElement = element(withId(R.id.view_pager_even_label))
    }

    class OddPage(interactionContext: InteractionContext) : ViewElement(interactionContext) {
        val label: ViewElement = element(withId(R.id.view_pager_odd_label))
    }
}