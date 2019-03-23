package com.avito.android.ui.test

import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.view.View
import android.widget.FrameLayout
import com.avito.android.test.action.Actions
import com.avito.android.test.checks.Checks
import com.avito.android.test.page_object.ListElement
import com.avito.android.test.page_object.PageObjectElement
import com.avito.android.test.page_object.ViewElement
import com.avito.android.ui.R
import org.hamcrest.Matcher

class ButtonsOverRecyclerScreen {

    val list = List()

    class List : ListElement(withId(R.id.recycler)) {

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
        ) : ViewElement(matcher, actions = actions, checks = checks) {

            val title = childFactory(withId(R.id.title))
        }
    }
}
