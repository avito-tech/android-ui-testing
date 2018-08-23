package com.avito.android.test.page_object

import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.v7.widget.DialogTitle

class Alert : PageObject {

    val messageElement = BasePageObjectElement(withId(android.R.id.message))

    val title = BasePageObjectElement(ViewMatchers.isAssignableFrom(DialogTitle::class.java))

    val okButton = BasePageObjectElement(withId(android.R.id.button1))
}
