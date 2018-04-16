package com.avito.android.test.action

interface FieldActions {

    fun write(text: String)

    //todo move to keyboard actors
    fun writeAndPressImeAction(text: String)

    fun pressImeAction()

    fun clear()

    fun append(text: String)
}