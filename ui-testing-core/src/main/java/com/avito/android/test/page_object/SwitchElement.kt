package com.avito.android.test.page_object

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers
import android.support.v7.widget.SwitchCompat
import android.view.View
import com.avito.android.test.InteractionContext
import com.avito.android.test.SimpleInteractionContext
import com.avito.android.test.action.ActionsDriver
import org.hamcrest.Matcher

class SwitchElement(interactionContext: InteractionContext) : PageObjectElement(interactionContext),
    SwitchElementActions by SwitchElementActionsImpl(interactionContext) {

    constructor(matcher: Matcher<View>) : this(SimpleInteractionContext(matcher))
}

interface SwitchElementActions {

    fun setIsChecked(isChecked: Boolean)
}

class SwitchElementActionsImpl(private val driver: ActionsDriver) : SwitchElementActions {

    override fun setIsChecked(isChecked: Boolean) {
        driver.perform(SetSwitchIsCheckedAction(isChecked))
    }
}

class SetSwitchIsCheckedAction(private val isChecked: Boolean) : ViewAction {

    override fun getConstraints(): Matcher<View> =
        ViewMatchers.isAssignableFrom(SwitchCompat::class.java)

    override fun getDescription(): String = "Switch state to $isChecked"

    override fun perform(uiController: UiController, view: View) {
        (view as SwitchCompat).isChecked = isChecked
    }
}
