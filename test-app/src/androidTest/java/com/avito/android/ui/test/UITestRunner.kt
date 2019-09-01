package com.avito.android.ui.test

import android.content.Intent
import androidx.test.runner.AndroidJUnitRunner

class UITestRunner : AndroidJUnitRunner() {

    override fun onStart() {
        super.onStart()
        targetContext.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
    }
}
