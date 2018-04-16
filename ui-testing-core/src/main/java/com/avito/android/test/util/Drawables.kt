package com.avito.android.test.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat


internal fun Drawable.isSame(context: Context, @DrawableRes resId: Int, @ColorInt tint: Int? = null): Boolean {
    return if (tint != null) {
        isSame(ContextCompat.getDrawable(context, resId)?.wrapForTinting(tint))
    } else {
        isSame(ContextCompat.getDrawable(context, resId))
    }
}

fun Drawable.wrapForTinting(@ColorInt color: Int): Drawable {
    val drawable = DrawableCompat.wrap(this)
    DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP)
    DrawableCompat.setTint(drawable.mutate(), color)
    return drawable
}

internal fun Drawable.isSame(other: Drawable?): Boolean {
    other ?: return false
    if (this is StateListDrawable && other is StateListDrawable) {
        return current.isSame(other.current)
    }
    if (this is BitmapDrawable && other is BitmapDrawable) {
        return bitmap.sameAs(other.bitmap)
    }
    return this.toBitmap().sameAs(other.toBitmap())
}

internal fun Int?.getResourceName(resources: Resources): String {
    if (this == null) return ""
    if (this == 0) return "empty"
    return resources.getResourceName(this)
}

internal fun Int?.matchDrawable(context: Context, drawable: Drawable?, @ColorInt tint: Int? = null): Boolean {
    if (this == null) return true
    if (this == 0 && drawable == null) return true
    return drawable?.isSame(context, this, tint) ?: false
}

fun Drawable.toBitmap(): Bitmap {
    val drawable = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        DrawableCompat.wrap(this).mutate()
    } else this

    val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return bitmap
}