package com.avito.android.ui

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity

class SwipeRefreshActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    lateinit var recycler: SwipeRefreshLayout
    var refreshedTimes = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe_refresh)

        recycler = findViewById<SwipeRefreshLayout>(R.id.swipe_refresh) as SwipeRefreshLayout
        recycler.setOnRefreshListener(this)
    }

    override fun onRefresh() {
        refreshedTimes += 1
    }

    fun postAndStopRefreshing() {
        recycler.handler.postDelayed({ recycler.isRefreshing = false }, 50)
    }
}
