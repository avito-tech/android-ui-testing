package com.avito.android.test.matcher

import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.widget.ImageView


class DrawableMatcherImageButton(
    @DrawableRes private val src: Int? = null,
    @ColorRes private val tint: Int? = null
) :
    DrawableMatcher<ImageView>({ it.drawable }, src, tint, ImageView::class.java)