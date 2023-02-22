package ru.nikitazar.domain.models

data class DrugDomain(
    val id: Int = 0,
    val image: String? = null,
    val name: String = "",
    val description: String = "",
)
