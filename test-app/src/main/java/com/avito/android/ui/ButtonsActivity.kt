package com.avito.android.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.TimeUnit

class ButtonsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buttons)

        setupEnabledButton()
        setupDisabledButton()
        setupNonClickableButton()
        setupNonLongClickableButton()
    }

    private fun setupDisabledButton() {
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

    private fun setupEnabledButton() {
        val buttonEnabledClickedTextView = findViewById<View>(
            R.id.button_enabled_clicked_text_view
        )
        val buttonEnabledLongClickedTextView = findViewById<View>(
            R.id.button_enabled_long_clicked_text_view
        )

        findViewById<Button>(R.id.button_enabled).apply {
            setOnClickListener {
                buttonEnabledClickedTextView.visibility = View.VISIBLE
            }

            setOnLongClickListener {
                buttonEnabledLongClickedTextView.visibility = View.VISIBLE
                true
            }
        }
    }

    private fun setupNonClickableButton() {
        findViewById<Button>(R.id.button_non_clickable).apply {
            setOnClickListener {
                Toast.makeText(context, "Non clickable clicked", Toast.LENGTH_LONG).show()
            }

            setOnLongClickListener {
                Toast.makeText(context, "Non clickable long clicked", Toast.LENGTH_LONG).show()
                true
            }
            isClickable = false
        }
    }

    private fun setupNonLongClickableButton() {
        val clickIndicatorView = findViewById<View>(
            R.id.button_non_long_clickable_clicked_text_view
        )
        findViewById<Button>(R.id.button_non_long_clickable).apply {
            setOnClickListener {
                clickIndicatorView.visibility = View.VISIBLE
            }

            setOnLongClickListener {
                Toast.makeText(context, "Non long-clickable long clicked", Toast.LENGTH_LONG).show()
                true
            }
            isLongClickable = false
        }
    }
}