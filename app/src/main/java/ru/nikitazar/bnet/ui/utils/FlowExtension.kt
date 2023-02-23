package ru.nikitazar.bnet.ui.utils

import kotlinx.coroutines.flow.*

fun <T> Flow<T>.simpleScan(count: Int): Flow<List<T?>> {
    val items = List<T?>(count) { null }
    return this.scan(items) { previous, value -> previous.drop(1) + value }
}