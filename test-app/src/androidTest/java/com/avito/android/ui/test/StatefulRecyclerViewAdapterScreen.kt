package com.avito.android.ui.test

import android.support.test.espresso.matcher.ViewMatchers
import android.view.View
import android.widget.LinearLayout
import com.avito.android.test.action.Actions
import com.avito.android.test.checks.Checks
import com.avito.android.test.page_object.ListElement
import com.avito.android.test.page_object.PageObjectElement
import com.avito.android.test.page_object.ViewElement
import com.avito.android.ui.R
import org.hamcrest.Matcher

class StatefulRecyclerViewAdapterScreen {

    val list = List()

    class List : ListElement(ViewMatchers.withId(R.id.recycler)) {

        fun cellWithTitle(title: String) =
            typedItemByMatcher(ViewMatchers.hasDescendant(ViewMatchers.withText(title)), ::Cell)

        fun cellWithTitleCreatedByRecyclerViewInteractionContext(title: String) =
            typedItemByMatcher<ViewElement>(
                ViewMatchers.hasDescendant(ViewMatchers.withText(title))
            )

        class Cell(
            matcher: Matcher<View>,
            actions: Actions,
            checks: Checks,
            childFactory: (Matcher<View>) -> PageObjectElement
        ) : ViewElement(matcher, actions = actions, checks = checks) {

            val title = childFactory(ViewMatchers.withId(R.id.title))
            val title2 = childFactory(ViewMatchers.withId(R.id.title2))
        }
    }
}