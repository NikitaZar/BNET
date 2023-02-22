package ru.nikitazar.data.models

import com.google.gson.annotations.SerializedName

data class Flags(
    @SerializedName("html") val html: Int = 0,
    @SerializedName("no_value") val noValue: Boolean = false,
    @SerializedName("no_name") val noName: Boolean = false,
    @SerializedName("no_image") val noImage: Boolean = false,
    @SerializedName("no_wrap") val noWrap: Boolean = false,
    @SerializedName("no_wrap_name") val noWrapName: Boolean = false,
    @SerializedName("system") val system: Boolean? = false
)