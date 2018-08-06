package com.avito.android.test.matcher

import android.annotation.SuppressLint
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v7.view.menu.ActionMenuItemView

@SuppressLint("RestrictedApi")
class DrawableMatcherActionIcon(
    @DrawableRes private val src: Int? = null,
    @ColorRes private val tint: Int? = null
) : DrawableMatcher<ActionMenuItemView>(
    { it.itemData.icon },
    src,
    tint,
    ActionMenuItemView::class.java
)
