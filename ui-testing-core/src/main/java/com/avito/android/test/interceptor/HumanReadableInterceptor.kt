package com.avito.android.test.interceptor

import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.ViewAction
import android.support.test.espresso.ViewAssertion
import android.support.test.espresso.assertion.describe
import android.view.View
import com.avito.android.test.util.describe

class HumanReadableActionInterceptor(private val consumer: (String) -> Unit) :
    ActionInterceptor {

    override fun intercept(action: ViewAction, description: String, view: View) {
        consumer.invoke("$description on ${view.describe()}")
    }
}

class HumanReadableAssertionInterceptor(private val consumer: (String) -> Unit) :
    AssertionInterceptor {

    override fun intercept(
        assertion: ViewAssertion,
        view: View?,
        noViewFoundException: NoMatchingViewException?
    ) {
        consumer.invoke("${assertion.describe()} on ${view.describe()}")
    }
}
