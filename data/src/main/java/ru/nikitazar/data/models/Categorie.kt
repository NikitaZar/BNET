package ru.nikitazar.data.models

import com.google.gson.annotations.SerializedName

data class Categorie(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("icon") val icon: String? = null,
    @SerializedName("image") val image: String? = null,
    @SerializedName("name") val name: String = ""
)