package ru.nikitazar.bnet.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.nikitazar.domain.models.DrugDomain
import ru.nikitazar.domain.usecase.GetByIdUseCase
import javax.inject.Inject

@HiltViewModel
class DrugViewModel @Inject constructor(
    private val getByIdUseCase: GetByIdUseCase,
) : ViewModel() {

    val data: LiveData<DrugDomain>
        get() = _data
    private val _data = MutableLiveData(DrugDomain())

    fun getById(id: Int) = viewModelScope.launch {
        _data.value = getByIdUseCase.execute(id)
    }
}