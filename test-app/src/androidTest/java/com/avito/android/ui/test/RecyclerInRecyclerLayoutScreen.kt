package com.avito.android.ui.test

import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.view.View
import android.widget.FrameLayout
import com.avito.android.test.InteractionContext
import com.avito.android.test.action.Actions
import com.avito.android.test.checks.Checks
import com.avito.android.test.page_object.ListElement
import com.avito.android.test.page_object.PageObjectElement
import com.avito.android.ui.R
import org.hamcrest.Matcher

class RecyclerInRecyclerLayoutScreen : ListElement(ViewMatchers.withId(R.id.recycler)) {

    val horizontalList: InnerList
        get() = typedItemByMatcher((withId(R.id.inner_recycler)))

    class InnerList(interactionContext: InteractionContext) : ListElement(interactionContext) {

        fun cellWithTitle(title: String) =
            typedItemByMatcher(ViewMatchers.hasDescendant(ViewMatchers.withText(title)), ::Cell)

        fun cellAt(position: Int) = typedItemAtPosition(
            ViewMatchers.isAssignableFrom(FrameLayout::class.java),
            position
        ) { matcher: Matcher<View>, actions: Actions, checks: Checks, childFactory ->
            Cell(matcher, actions, checks, childFactory)
        }

        class Cell(
            matcher: Matcher<View>,
            actions: Actions,
            checks: Checks,
            childFactory: (Matcher<View>) -> PageObjectElement
        ) : PageObjectElement(matcher, actions = actions, checks = checks) {

            val title = childFactory(withId(R.id.title))
        }
    }
}