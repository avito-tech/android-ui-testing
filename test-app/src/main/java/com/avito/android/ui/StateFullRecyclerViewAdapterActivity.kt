package com.avito.android.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class StateFullRecyclerViewAdapterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)

        val data = (1..99)
            .map {
                StateFullItem(
                    title = it.toString(),
                    viewedCount = 0
                )
            }
            .toList()

        findViewById<RecyclerView>(R.id.recycler).apply {
            layoutManager = LinearLayoutManager(this@StateFullRecyclerViewAdapterActivity)
            adapter = StateFullAdapter(data.toMutableList())
        }
    }

    private class StateFullAdapter(
        private val items: MutableList<StateFullItem>
    ) : RecyclerView.Adapter<ViewHolder>() {

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if (!holder.isFakeHolderForTests()) {
                items[position].viewedCount++
            }
            holder.title.text = items[position].title
            holder.title2.text = items[position].viewedCount.toString()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cell_with_multiple_text_views, parent, false))

        override fun getItemCount() = items.size

        /**
         * See method itemsMatching inside RecyclerViewActions.kt for understanding what happens here
         */
        private fun RecyclerView.ViewHolder.isFakeHolderForTests(): Boolean
            = itemView.getTag(Integer.MAX_VALUE - 228) != null
    }

    private class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val title2: TextView = itemView.findViewById(R.id.title2)
    }

    private data class StateFullItem(
        val title: String,
        var viewedCount: Int = 0
    )
}
