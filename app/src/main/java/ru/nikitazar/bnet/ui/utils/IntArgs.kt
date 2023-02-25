package ru.nikitazar.bnet.ui.utils

import android.os.Bundle
import kotlin.reflect.KProperty
import kotlin.properties.ReadWriteProperty

object IntArg : ReadWriteProperty<Bundle, Int?> {

    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: Int?) {
        value?.let { thisRef.putInt(property.name, value) }
    }

    override fun getValue(thisRef: Bundle, property: KProperty<*>): Int =
        thisRef.getInt(property.name)
}