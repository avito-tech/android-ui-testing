package com.avito.android.test.espresso.action

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.matcher.ViewMatchers
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import org.hamcrest.Matcher

class RecyclerSpanCountAction : ViewAction {

    var result: Int = Int.MIN_VALUE
        get() {
            if (field == Int.MIN_VALUE) {
                throw UninitializedPropertyAccessException()
            }
            return field
        }
        private set

    override fun getDescription() = "getting grid layout manager span count"

    override fun getConstraints(): Matcher<View> = ViewMatchers.isAssignableFrom(RecyclerView::class.java)

    override fun perform(uiController: UiController, view: View) {
        result = ((view as RecyclerView).layoutManager as GridLayoutManager).spanCount
    }

}