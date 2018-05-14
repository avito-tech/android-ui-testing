package com.avito.android.test.matcher

import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.design.widget.CheckableImageButton

class DrawableMatcherCheckableImageView(
    @DrawableRes private val src: Int? = null,
    @ColorRes private val tint: Int? = null
) :
    DrawableMatcher<CheckableImageButton>(
        { it.drawable },
        src,
        tint,
        CheckableImageButton::class.java
    )