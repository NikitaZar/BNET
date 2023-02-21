package ru.nikitazar.data.models

import com.google.gson.annotations.SerializedName
import ru.nikitazar.domain.models.FieldDomain
import ru.nikitazar.domain.models.FieldType

data class Field(
    @SerializedName("types_id") var typesId: Int = 0,
    @SerializedName("type") var type: FieldType = FieldType.text,
    @SerializedName("name") var name: String = "",
    @SerializedName("value") var value: String = "",
    @SerializedName("image") var image: String = "",
    @SerializedName("flags") var flags: Flags? = Flags(),
    @SerializedName("show") var show: Int = 0,
    @SerializedName("group") var group: Int = 0
){
   fun toDomain() = FieldDomain(
       typesId=typesId,
       type = type,
       name=name,
       value=value,
       image=image,
       flags = flags?.toDomain(),
       show = show,
       group=group,
   )
}
