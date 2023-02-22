package ru.nikitazar.bnet.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import ru.nikitazar.domain.models.DrugDomain
import ru.nikitazar.domain.usecase.GetById
import ru.nikitazar.domain.usecase.GetPage
import javax.inject.Inject

private const val DEBOUNCE = 500L

class DrugsViewModel @Inject constructor(
    private val getById: GetById,
    private val getPage: GetPage
) : ViewModel() {

    private val search = MutableLiveData("")
    val pageData: Flow<PagingData<DrugDomain>> = search.asFlow()
        .debounce(DEBOUNCE)
        .flatMapLatest { getPage.execute(it) }
        .cachedIn(viewModelScope)

    fun setSearch(value: String) {
        if (search.value == value) return
        search.value = value
    }

    fun refresh() {
        search.postValue(search.value)
    }
}