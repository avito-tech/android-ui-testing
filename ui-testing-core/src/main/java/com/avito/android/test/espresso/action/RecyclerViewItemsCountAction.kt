package com.avito.android.test.espresso.action

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers
import android.support.v7.widget.RecyclerView
import android.view.View
import org.hamcrest.Matcher

class RecyclerViewItemsCountAction : ViewAction {

    var result: Int = Int.MIN_VALUE
        get() {
            if (field == Int.MIN_VALUE) {
                throw UninitializedPropertyAccessException()
            }
            return field
        }
        private set

    override fun getDescription() = "getting items count from recycler view adapter"

    override fun getConstraints(): Matcher<View> =
        ViewMatchers.isAssignableFrom(RecyclerView::class.java)

    override fun perform(uiController: UiController, view: View) {
        result = ((view as RecyclerView).adapter!!.itemCount)
    }
}
