package com.avito.android.test

import android.content.res.Resources
import android.support.test.espresso.core.internal.deps.guava.base.MoreObjects
import android.util.Log
import android.view.View
import android.widget.Checkable
import android.widget.TextView
import com.avito.android.test.report.model.Entry
import java.util.concurrent.TimeUnit

object ReportLogger {

    private var listener: LogListener? = null

    fun log(tag: String, message: String) {
        listener?.log(Entry.Comment(title = "$tag: $message", timestamp = nowInSeconds()))
        Log.i(tag, message)
    }

    class Pack(val tag: String, val view: View?) {
        private var events = sortedMapOf<Long, String>()
        private var debugEvents = sortedMapOf<Long, String>()

        fun event(text: String) {
            events.put(nowInSeconds(), text)
        }

        fun eventDebug(text: String) {
            debugEvents.put(nowInSeconds(), text)
        }

        fun send() {
            log(tag, describe(view) + events.values.joinToString(prefix = "\n", separator = "\n"))
        }
    }

    fun describe(view: View?): String {
        if (view == null) return "View is null"

        return MoreObjects.toStringHelper(view).apply {

            add("id", view.getHumanReadableId())

            if (view.contentDescription.isNullOrBlank().not()) {
                add("contentDescription", view.contentDescription)
            }

            when (view.visibility) {
                View.GONE -> add("visibility", "GONE")
                View.INVISIBLE -> add("visibility", "INVISIBLE")
                View.VISIBLE -> add("visibility", "VISIBLE")
                else -> add("visibility", view.visibility)
            }

            if (view is TextView) {
                if (view.text.isNullOrBlank().not()) {
                    add("text", view.text)
                }

                if (view.error.isNullOrBlank().not()) {
                    add("error-text", view.error)
                }

                if (view.hint.isNullOrBlank().not()) {
                    add("hint", view.hint)
                }
            }

            if (view is Checkable) {
                add("is-checked", view.isChecked)
            }

        }.toString()
    }

    private fun View.getHumanReadableId(): String {
        return if (id != -1 && resources != null) {
            try {
                resources.getResourceEntryName(id)
            } catch (ignore: Resources.NotFoundException) {
                id.toString()
            }
        } else "Can't find view id"
    }

    fun setListener(listener: LogListener) {
        ReportLogger.listener = listener
    }

    fun removeListener() {
        listener = null
    }

    private fun nowInSeconds(): Long = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())

    interface LogListener {

        fun log(entry: Entry)
    }
}