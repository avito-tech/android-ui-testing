package com.avito.android.test.checks

import android.support.test.espresso.ViewAssertion

interface ChecksDriver {

    fun check(assertion: ViewAssertion)
}