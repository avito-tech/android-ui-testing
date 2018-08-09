package com.avito.android.test.util

fun <T> Any.getFieldByReflection(fieldName: String): T {
    return this::class.java.getDeclaredField(fieldName)
        .also { it.isAccessible = true }
        .get(this) as T
}
