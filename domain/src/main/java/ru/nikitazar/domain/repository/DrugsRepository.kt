package ru.nikitazar.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.nikitazar.domain.models.DrugDomain

interface DrugsRepository {
    suspend fun getPage(search: String): Flow<PagingData<DrugDomain>>
    suspend fun getById(id: Int): DrugDomain
}