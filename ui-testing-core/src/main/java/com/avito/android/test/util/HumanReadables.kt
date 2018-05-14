package com.avito.android.test.util

import android.view.View
import android.widget.TextView

/**
 * Dead simple string representation of any [View]
 */
fun View?.describe(): String {
    if (this == null) return "null"

    val result = StringBuilder()
    result.append(this::class.java.simpleName)
    result.append('(')
    result.append("id=${resources?.getResourceEntryName(id)}")
    if (contentDescription != null) {
        result.append(";desc=$contentDescription")
    }
    if (this is TextView) {
        if (!text.isNullOrBlank()) {
            result.append(";text=$text")
        }
        if (!hint.isNullOrBlank()) {
            result.append(";hint=$hint")
        }
    }
    result.append(')')

    return result.toString()
}