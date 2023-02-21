package ru.nikitazar.data.repository

import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.nikitazar.data.api.ApiService
import ru.nikitazar.data.errors.ApiError
import ru.nikitazar.data.errors.AppError
import ru.nikitazar.domain.models.DrugDomain
import ru.nikitazar.domain.repository.Repository
import javax.inject.Inject
import javax.inject.Singleton

private const val PAGE_SIZE = 5
const val ENABLE_PLACE_HOLDERS = false

@Singleton
class RepositoryImpl @Inject constructor(
    private val api: ApiService,
) : Repository {

    override suspend fun getPage(search: String): Flow<PagingData<DrugDomain>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = ENABLE_PLACE_HOLDERS,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = { DrugSource(api, search) }
        ).flow.map { pagingData -> pagingData.map { drug -> drug.toDomain() } }

    override suspend fun getById(id: Int): DrugDomain {
        try {
            val response = api.getById(id)

            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val drug = response.body() ?: throw ApiError(response.code(), response.message())
            return drug.toDomain()
        } catch (e: AppError) {
            throw AppError.from(e)
        }
    }
}