package com.avito.android.ui

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

class AppBarActivity : AppCompatActivity() {

    private lateinit var appBarLayout: AppBarLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_bar)
        appBarLayout = findViewById(R.id.appbar)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.title = "Title"
        toolbar.subtitle = "Subtitle"
        setSupportActionBar(toolbar)
    }

    fun setExpanded(isExpanded: Boolean) {
        appBarLayout.handler.post {
            appBarLayout.setExpanded(isExpanded)
        }
    }
}
