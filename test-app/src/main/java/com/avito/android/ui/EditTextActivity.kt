package com.avito.android.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.telephony.PhoneNumberFormattingTextWatcher
import android.widget.EditText

class EditTextActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edittext)

        findViewById<EditText>(R.id.phone_number_edit_text1).addTextChangedListener(
            PhoneNumberFormattingTextWatcher()
        )
    }
}
