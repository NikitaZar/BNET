package ru.nikitazar.domain.models

data class DrugDomain(
    val id: Int = 0,
    val image: String? = null,
    val categories: CategorieDomain = CategorieDomain(),
    val name: String = "",
    val description: String = "",
    val documentation: String = "",
    val fields: List<FieldDomain>? = null
)
