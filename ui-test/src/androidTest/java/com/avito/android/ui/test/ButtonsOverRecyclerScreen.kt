package com.avito.android.ui.test

import android.support.test.espresso.matcher.ViewMatchers.withId
import com.avito.android.test.page_object.ListElement
import com.avito.android.ui.R

class ButtonsOverRecyclerScreen {

    val list = ListElement(withId(R.id.recycler))
}