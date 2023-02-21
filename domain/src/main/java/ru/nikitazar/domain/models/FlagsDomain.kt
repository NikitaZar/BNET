package ru.nikitazar.domain.models

data class FlagsDomain(
    val html: Int = 0,
    val noValue: Boolean = false,
    val noName: Boolean = false,
    val noImage: Boolean = false,
    val noWrap: Boolean = false,
    val noWrapName: Boolean = false,
    val system: Boolean? = false
)