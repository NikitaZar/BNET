package ru.nikitazar.domain.models

import ru.nikitazar.data.models.Flags

data class Field(
    var typesId: Int = 0,
    var type: FieldType = FieldType.text,
    var name: String = "",
    var value: String = "",
    var image: String = "",
    var flags: Flags? = Flags(),
    var show: Int = 0,
    var group: Int = 0
)

enum class FieldType {
    text, image, gallery, text_block, list, button
}
