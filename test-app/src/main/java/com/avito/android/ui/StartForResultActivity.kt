package com.avito.android.ui

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class StartForResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_for_result)

        val textView = findViewById<View>(R.id.text)

        textView.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
