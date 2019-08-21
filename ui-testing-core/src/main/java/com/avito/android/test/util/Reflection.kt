package com.avito.android.test.util

internal fun <T> Any.getFieldByReflection(fieldName: String): T =
    this::class.java.getDeclaredField(fieldName)
        .also { it.isAccessible = true }
        .get(this) as T

internal fun Any.getFieldByReflectionWithAnyField(fieldName: String): Any =
    this::class.java.getDeclaredField(fieldName)
        .also { it.isAccessible = true }
        .get(this)

internal fun Any.executeMethod(method: String, vararg arguments: Any?) =
    javaClass.methods.find { it.name == method }!!
        .apply { isAccessible = true }
        .let {
            it.invoke(
                this@executeMethod,
                *arguments
            )
        }
