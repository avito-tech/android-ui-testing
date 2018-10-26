package com.avito.android.test.matcher

import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.view.View

class DrawableBackgroundMatcher(
    @DrawableRes private val src: Int? = null,
    @ColorRes private val tint: Int? = null
) :
    DrawableMatcher<View>(
        { it.background },
        src,
        tint,
        View::class.java
    )
