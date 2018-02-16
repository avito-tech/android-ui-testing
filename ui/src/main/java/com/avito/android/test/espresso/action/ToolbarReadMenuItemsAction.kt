package com.avito.android.test.espresso.action

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.support.v7.widget.Toolbar
import android.view.View
import org.hamcrest.Matcher
import java.util.ArrayList

class ToolbarReadMenuItemsAction : ViewAction {

    private lateinit var hiddenItems: List<String>

    override fun getDescription() = "reading toolbar overflow menu items"

    override fun getConstraints(): Matcher<View> = isAssignableFrom(Toolbar::class.java)

    override fun perform(uiController: UiController, view: View) {
        val toolbar = view as Toolbar
        val mExpandedMenuPresenter =
            Toolbar::class.java.getDeclaredField("mExpandedMenuPresenter").also { it.isAccessible = true }.get(toolbar)
        val mMenu = mExpandedMenuPresenter.javaClass.getDeclaredField("mMenu").also { it.isAccessible = true }
            .get(mExpandedMenuPresenter)
        val mNonActionItems = mMenu!!.javaClass.getDeclaredField("mNonActionItems").also {
            it.isAccessible = true
        }.get(mMenu) as ArrayList<*>
        hiddenItems = mNonActionItems.map { it.toString() }
    }

    fun hasHiddenItem(itemMatcher: Matcher<String>): Boolean {
        try {
            return hiddenItems.any { itemMatcher.matches(it) }
        } catch (e: UninitializedPropertyAccessException) {
            throw ToolbarReadActionUsageException()
        }
    }

    class ToolbarReadActionUsageException : RuntimeException(
        """
        First of all, you should perform action, like
        "ToolbarReadMenuItemsAction().apply { onView(isAssignableFrom(Toolbar::class.java)).perform(this) }"""".trimIndent()
    )

}