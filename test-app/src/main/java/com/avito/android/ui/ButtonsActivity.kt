package com.avito.android.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast

class ButtonsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buttons)

        findViewById<Button>(R.id.button_enabled).apply {
            setOnClickListener {
                Toast.makeText(context, "Enabled clicked", Toast.LENGTH_LONG).show()
            }

            setOnLongClickListener {
                Toast.makeText(context, "Enabled long clicked", Toast.LENGTH_LONG).show()
                true
            }
        }

        findViewById<Button>(R.id.button_disabled).apply {
            setOnClickListener {
                Toast.makeText(context, "Disabled clicked", Toast.LENGTH_LONG).show()
            }

            setOnLongClickListener {
                Toast.makeText(context, "Disabled long clicked", Toast.LENGTH_LONG).show()
                true
            }
        }
    }
}