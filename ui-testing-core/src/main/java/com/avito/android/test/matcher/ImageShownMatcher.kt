package com.avito.android.test.matcher

import androidx.test.espresso.matcher.BoundedMatcher
import android.view.View
import android.widget.ImageView
import org.hamcrest.Description

class ImageShownMatcher :
    BoundedMatcher<View, ImageView>(ImageView::class.java) {

    override fun describeTo(description: Description) {
        description.appendText("with shown image: ")
    }

    override fun matchesSafely(layout: ImageView): Boolean {
        layout.isDrawingCacheEnabled = true

        layout.drawingCache.apply {
            for (i in 0 until height) {
                for (j in 0 until width) {
                    if (getPixel(i, j) != 0) {
                        return true
                    }
                }
            }
        }

        layout.destroyDrawingCache()
        return false
    }
}
