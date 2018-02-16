package ru.avito.services.component.connection_state_layout

import android.support.test.espresso.matcher.BoundedMatcher
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
            for (i in 0..height - 1) {
                for (j in 0..width - 1) {
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