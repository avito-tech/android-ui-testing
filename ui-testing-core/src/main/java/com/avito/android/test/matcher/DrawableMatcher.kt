package com.avito.android.test.matcher

import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.test.espresso.matcher.BoundedMatcher
import android.support.v4.content.ContextCompat
import android.view.View
import com.avito.android.test.util.getResourceName
import com.avito.android.test.util.matchDrawable
import org.hamcrest.Description

open class DrawableMatcher<T : View>(
    private val drawableSupplier: (T) -> Drawable,
    @DrawableRes private val src: Int? = null,
    @ColorRes private val tint: Int? = null,
    clazz: Class<out T>
) : BoundedMatcher<View, T>(clazz) {

    private var description: String? = null

    override fun describeTo(description: Description) {
        description.appendText("has compound drawables: ${this.description}")
    }

    override fun matchesSafely(item: T): Boolean {
        val context = item.context
        val drawable: Drawable = drawableSupplier.invoke(item)
        this.description = getDescription(context.resources)

        val tintColor = if (tint == null) {
            null
        } else {
            ContextCompat.getColor(context, tint)
        }

        return src.matchDrawable(context, drawable, tintColor)
    }

    private fun getDescription(resources: Resources): String {
        return "drawable=${src.getResourceName(resources)}"
    }
}
