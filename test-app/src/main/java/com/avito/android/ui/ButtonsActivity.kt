package com.avito.android.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.util.concurrent.TimeUnit

class ButtonsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buttons)

        val buttonEnabledClickedTextView = findViewById<TextView>(R.id.button_enabled_clicked_text_view)
        val buttonEnabledLongClickedTextView = findViewById<TextView>(R.id.button_enabled_long_clicked_text_view)

        findViewById<Button>(R.id.button_enabled).apply {
            setOnClickListener {
                buttonEnabledClickedTextView.toggleVisibility()

                postDelayed(
                    {
                        runOnUiThread {
                            buttonEnabledClickedTextView.toggleVisibility()
                        }
                    },
                    TimeUnit.SECONDS.toMillis(3)
                )
            }

            setOnLongClickListener {
                buttonEnabledLongClickedTextView.toggleVisibility()

                postDelayed(
                    {
                        runOnUiThread {
                            buttonEnabledLongClickedTextView.toggleVisibility()
                        }
                    },
                    TimeUnit.SECONDS.toMillis(3)
                )

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

private fun View.toggleVisibility() {
    visibility = if (visibility == View.GONE) {
        View.VISIBLE
    } else {
        View.GONE
    }
}