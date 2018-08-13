package com.avito.android.ui

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

class TabLayoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_layout)

        findViewById<TabLayout>(R.id.tabs).apply {
            tabMode = TabLayout.MODE_SCROLLABLE
            (1..1000).forEach {
                addTab(newTab().setText("Tab $it"))
            }
        }
    }
}
