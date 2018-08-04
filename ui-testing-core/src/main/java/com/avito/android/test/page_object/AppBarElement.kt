package com.avito.android.test.page_object

import android.support.design.widget.AppBarLayout
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers
import android.view.View
import com.avito.android.test.InteractionContext
import com.avito.android.test.SimpleInteractionContext
import com.avito.android.test.action.Actions
import com.avito.android.test.action.ActionsDriver
import com.avito.android.test.action.ActionsImpl
import org.hamcrest.Matcher

class AppBarElement(interactionContext: InteractionContext) :
    PageObjectElement(interactionContext) {

    constructor(matcher: Matcher<View>) : this(SimpleInteractionContext(matcher))

    override val actions: AppBarActions = AppBarActionsImpl(interactionContext)
}

interface AppBarActions : Actions {
    fun collapse()
    fun expand()
}

class AppBarActionsImpl(private val driver: ActionsDriver) : AppBarActions,
    Actions by ActionsImpl(driver) {

    override fun collapse() {
        driver.perform(SetExpandedAction(isExpanded = false))
    }

    override fun expand() {
        driver.perform(SetExpandedAction(isExpanded = true))
    }
}

private class SetExpandedAction(private val isExpanded: Boolean) : ViewAction {

    override fun getConstraints(): Matcher<View> =
        ViewMatchers.isAssignableFrom(AppBarLayout::class.java)

    override fun getDescription(): String = "Set expanded property of AppBarLayout to $isExpanded"

    override fun perform(uiController: UiController, view: View) {
        (view as AppBarLayout).setExpanded(isExpanded)
    }
}
