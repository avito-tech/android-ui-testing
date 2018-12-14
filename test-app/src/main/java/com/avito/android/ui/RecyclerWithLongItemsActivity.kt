package com.avito.android.ui

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.lang.IllegalStateException

class RecyclerWithLongItemsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)

        findViewById<RecyclerView>(R.id.recycler).apply {
            layoutManager = LinearLayoutManager(
                this@RecyclerWithLongItemsActivity,
                LinearLayoutManager.VERTICAL,
                intent.getBooleanExtra(EXTRA_REVERSE_LAYOUT, false)
            )
            adapter = Adapter()
        }
    }

    companion object {
        private const val EXTRA_REVERSE_LAYOUT = "EXTRA_REVERSE_LAYOUT"

        fun intent(
            reverseLayout: Boolean
        ): (Intent) -> Intent = {
            it.putExtra(EXTRA_REVERSE_LAYOUT, reverseLayout)
        }
    }

    private class Adapter :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder?, position: Int) {
            viewHolder?.let { holder ->
                val longItemHolder = holder as LongItemHolder
                longItemHolder.targetViewTop.setAction()
                longItemHolder.targetViewCenter.setAction()
                longItemHolder.targetViewBottom.setAction()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.long_item, parent, false)

            return LongItemHolder(view)
        }

        override fun getItemCount() = 15

        override fun getItemViewType(position: Int): Int = 0

        private fun View.setAction() {
            val resources = context.resources
            val green = resources.getColor(R.color.green)
            val red = resources.getColor(R.color.red)

            setOnClickListener { clicked ->
                if ((clicked.background as ColorDrawable).color == green) {
                    clicked.setBackgroundColor(red)
                } else {
                    clicked.setBackgroundColor(green)
                }
            }
        }
    }

    private class LongItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val targetViewTop = itemView.findViewById<View>(R.id.target_view_top)
            ?: throw IllegalStateException("R.id.target_view_top not found")

        val targetViewCenter = itemView.findViewById<View>(R.id.target_view_center)
            ?: throw IllegalStateException("R.id.target_view_top not found")

        val targetViewBottom = itemView.findViewById<View>(R.id.target_view_bottom)
            ?: throw IllegalStateException("R.id.target_view_top not found")
    }
}
