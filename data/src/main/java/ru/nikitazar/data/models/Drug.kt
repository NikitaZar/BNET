package ru.nikitazar.data.models

import com.google.gson.annotations.SerializedName
import ru.nikitazar.domain.models.DrugDomain

data class Drug(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("image") val image: String? = null,
    @SerializedName("categories") val categories: Categorie = Categorie(),
    @SerializedName("name") val name: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("documentation") val documentation: String = "",
    @SerializedName("fields") val fields: List<Field>? = null
) {
    fun toDomain() = DrugDomain(
        id = id,
        image = image,
        categories = categories.toDomain(),
        name = name,
        description = description,
        documentation = documentation,
        fields = fields?.map { it.toDomain() }
    )
}
