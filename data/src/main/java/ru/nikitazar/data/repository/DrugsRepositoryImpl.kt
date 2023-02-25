package ru.nikitazar.data.repository

import android.util.Log
import androidx.paging.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import ru.nikitazar.data.api.ApiService
import ru.nikitazar.data.errors.ApiError
import ru.nikitazar.data.errors.AppError
import ru.nikitazar.data.models.Drug
import ru.nikitazar.domain.models.DrugDomain
import ru.nikitazar.domain.repository.DrugsRepository
import javax.inject.Inject
import javax.inject.Singleton

private const val PAGE_SIZE = 5
const val ENABLE_PLACE_HOLDERS = false

@Singleton
class DrugsRepositoryImpl @Inject constructor(
    private val api: ApiService,
) : DrugsRepository {

    override suspend fun getPage(search: String): Flow<PagingData<DrugDomain>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = ENABLE_PLACE_HOLDERS,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = { DrugSource(api, search) }
        ).flow
            .map { it.map(Drug::toDomain) }
            .flowOn(Dispatchers.IO)

    override suspend fun getById(id: Int): DrugDomain {
        try {
            val response = api.getById(id)

            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val drug = response.body() ?: throw ApiError(response.code(), response.message())
            return drug.toDomain()
        } catch (e: Exception) {
            throw AppError.from(e)
        }
    }
}