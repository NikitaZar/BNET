package ru.nikitazar.bnet.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import ru.nikitazar.domain.models.DrugDomain
import ru.nikitazar.domain.usecase.GetByIdUseCase
import ru.nikitazar.domain.usecase.GetPageUseCase
import javax.inject.Inject

private const val DEBOUNCE = 500L

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class DrugsViewModel @Inject constructor(
    private val getByIdUseCase: GetByIdUseCase,
    private val getPageUseCase: GetPageUseCase
) : ViewModel() {

    private val search = MutableLiveData("")

    val pageDataFlow: Flow<PagingData<DrugDomain>> = search.asFlow()
        .debounce(DEBOUNCE)
        .flatMapLatest {
            getPageUseCase.execute(it)
        }
        .cachedIn(viewModelScope)

    fun setSearch(value: String) {
        if (search.value == value) return
        search.value = value
    }

    fun refresh() {
        search.postValue(search.value)
    }
}