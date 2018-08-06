package com.avito.android.test.action

import android.support.test.espresso.ViewAction

interface ActionsDriver {

    fun perform(vararg actions: ViewAction)
}
