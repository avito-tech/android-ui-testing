package com.avito.android.test.espresso.assertion

import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.ViewAssertion
import android.support.test.espresso.matcher.ViewMatchers.assertThat
import android.view.View
import org.hamcrest.Matchers.`is`

class ViewExistsAssertion : ViewAssertion {

    override fun check(view: View?, noView: NoMatchingViewException?) {
        if (view == null) {
            assertThat("View is not present in the hierarchy: ${noView?.viewMatcherDescription}", false, `is`(true))
        }
    }

}