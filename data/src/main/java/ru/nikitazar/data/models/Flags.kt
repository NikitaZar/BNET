package ru.nikitazar.data.models

import com.google.gson.annotations.SerializedName

data class Flags(
    @SerializedName("html") val html: Int = 0,
    @SerializedName("no_value") val noValue: Int = 0,
    @SerializedName("no_name") val noName: Int = 0,
    @SerializedName("no_image") val noImage: Int = 0,
    @SerializedName("no_wrap") val noWrap: Int = 0,
    @SerializedName("no_wrap_name") val noWrapName: Int = 0,
    @SerializedName("system") val system: Int? = 0
)