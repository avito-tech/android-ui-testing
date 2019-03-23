package com.avito.android.ui.test

import android.support.test.espresso.matcher.ViewMatchers
import com.avito.android.test.page_object.ViewElement
import com.avito.android.ui.R

class BackgroundDrawableScreen {
    val viewWithBackgroundRedColor = ViewElement(ViewMatchers.withId(R.id.background_view_color))
    val viewWithBackgroundCheckIcon = ViewElement(ViewMatchers.withId(R.id.background_view_image))
}
