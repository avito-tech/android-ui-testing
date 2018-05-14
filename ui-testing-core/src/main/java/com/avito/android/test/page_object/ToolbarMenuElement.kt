package com.avito.android.test.page_object

import android.annotation.SuppressLint
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.BoundedMatcher
import android.support.v7.view.menu.MenuView
import android.view.View
import com.avito.android.test.InteractionContext
import com.avito.android.test.SimpleInteractionContext
import com.avito.android.test.checks.Checks
import com.avito.android.test.checks.ChecksDriver
import com.avito.android.test.checks.ChecksImpl
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`

class ToolbarMenuElement(interactionContext: InteractionContext) :
    PageObjectElement(interactionContext) {

    constructor(matcher: Matcher<View>) : this(SimpleInteractionContext(matcher))

    override val checks: ToolbarMenuElementChecks = ToolbarMenuElementChecksImpl(interactionContext)
}

interface ToolbarMenuElementChecks : Checks

class ToolbarMenuElementChecksImpl(private val driver: ChecksDriver) : ToolbarMenuElementChecks,
    Checks by ChecksImpl(driver) {

    override fun isChecked() {
        driver.check(matches(ToolbarMenuElementCheckedMatcher(`is`(true))))
    }

    override fun isNotChecked() {
        driver.check(matches(ToolbarMenuElementCheckedMatcher(`is`(false))))
    }
}

class ToolbarMenuElementCheckedMatcher(private val checkedMatcher: Matcher<Boolean>) :
    BoundedMatcher<View, View>(View::class.java, MenuView.ItemView::class.java) {

    @SuppressLint("RestrictedApi")
    override fun matchesSafely(item: View): Boolean {
        val itemView = item as? MenuView.ItemView ?: return false
        return checkedMatcher.matches(itemView.itemData.isChecked)
    }

    override fun describeTo(description: Description) {
        description.appendText("with checkable state: ")
        checkedMatcher.describeTo(description)
    }
}