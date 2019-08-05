package com.avito.android.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.math.cos
import kotlin.math.sin

class MovingButtonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moving_button)

        val deathZone = findViewById<View>(R.id.death_zone)
        val movedButton = findViewById<Button>(R.id.moved_button)
        val movedButtonClickedIndicator = findViewById<TextView>(R.id.moved_button_clicked_text_view)

        deathZone.setOnClickListener { throw RuntimeException("Clicked to a death zone") }

        thread {
            val fps = 30
            val iterationsCount = fps * 5 // 5 seconds (30 frames per every second)
            val radius = 50

            (0..iterationsCount)
                .forEach { iterationNumber ->
                    runOnUiThread {
                        val newX = (movedButton.x + cos(iterationNumber.toDouble()) * radius).toFloat()
                        val newY = (movedButton.y + sin(iterationNumber.toDouble()) * radius).toFloat()

                        movedButton.x = newX
                        movedButton.y = newY
                    }

                    try {
                        Thread.sleep((1000 / fps).toLong())
                    } catch (e: InterruptedException) {
                    }
                }
        }

        movedButton.setOnClickListener {
            movedButtonClickedIndicator.toggleVisibility()

            it.postDelayed(
                {
                    runOnUiThread {
                        movedButtonClickedIndicator.toggleVisibility()
                    }
                },
                TimeUnit.SECONDS.toMillis(3)
            )
        }
    }
}