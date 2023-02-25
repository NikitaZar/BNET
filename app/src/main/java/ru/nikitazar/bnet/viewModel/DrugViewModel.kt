package ru.nikitazar.bnet.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.nikitazar.data.errors.ApiError
import ru.nikitazar.data.errors.AppError
import ru.nikitazar.data.errors.NetworkError
import ru.nikitazar.data.errors.UnknownError
import ru.nikitazar.domain.models.DrugDomain
import ru.nikitazar.domain.usecase.GetByIdUseCase
import javax.inject.Inject

const val REQ_ERR = 400
const val NW_ERR = 1
const val NO_ERR = 0

@HiltViewModel
class DrugViewModel @Inject constructor(
    private val getByIdUseCase: GetByIdUseCase,
) : ViewModel() {

    val data: LiveData<DrugDomain>
        get() = _data
    private val _data = MutableLiveData(DrugDomain())

    val errState: LiveData<Int>
        get() = _errState
    private val _errState = MutableLiveData(NO_ERR)

    fun getById(id: Int) = viewModelScope.launch {
        try {
            _data.value = getByIdUseCase.execute(id)
        } catch (e: AppError) {
            when (e) {
                is NetworkError -> _errState.postValue(NW_ERR)
                is ApiError -> _errState.postValue(REQ_ERR)
                is UnknownError -> _errState.postValue(REQ_ERR)
            }
        }
    }

    val id: LiveData<Int>
        get() = _id
    private val _id = MutableLiveData(0)

    fun saveId(id: Int) {
        _id.value = id
    }

    fun refresh() {
        id.value?.let { getById(it) }
    }
}