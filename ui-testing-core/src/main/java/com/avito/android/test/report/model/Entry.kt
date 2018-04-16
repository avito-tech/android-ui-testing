package com.avito.android.test.report.model

import com.google.gson.annotations.SerializedName

sealed class Entry(
    @SerializedName("type") val type: String,
    @SerializedName("timestamp") open val timestamp: Long
) {

    data class File(
        @SerializedName("comment") val comment: String,
        @SerializedName("file_address") val fileAddress: String,
        @Transient override val timestamp: Long,
        @Transient val fileType: Type
    ) : Entry(type = fileType.name, timestamp = timestamp) {

        enum class Type {
            @SerializedName("html")
            html,
            @SerializedName("img_png")
            imgPng,
            @SerializedName("plain_text")
            plainText
        }
    }

    data class Comment(
        @SerializedName("title") val title: String,
        @Transient override val timestamp: Long
    ) : Entry(type = "comment", timestamp = timestamp)

    data class Field(
        @SerializedName("comment") val comment: String,
        @SerializedName("value") val value: String,
        @Transient override val timestamp: Long
    ) : Entry(type = "field", timestamp = timestamp)
}