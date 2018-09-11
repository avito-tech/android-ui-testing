package com.avito.android.test.page_object

import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.v7.widget.DialogTitle

class Alert : PageObject {

    val messageElement = ViewElement(withId(android.R.id.message))

    val title = ViewElement(ViewMatchers.isAssignableFrom(DialogTitle::class.java))

    val okButton = ViewElement(withId(android.R.id.button1))
}
