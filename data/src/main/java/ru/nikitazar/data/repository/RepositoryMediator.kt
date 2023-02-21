package ru.nikitazar.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import ru.nikitazar.data.api.ApiService
import ru.nikitazar.data.errors.ApiError
import ru.nikitazar.data.models.Drug
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class RepositoryMediator @Inject constructor(
    private val api: ApiService,
    private val search: String
) : RemoteMediator<Int, Drug>() {

    private var index = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Drug>
    ): MediatorResult {

        index = getIndex(loadType) ?: return MediatorResult.Success(endOfPaginationReached = false)
        val limit = state.config.pageSize
        val offset = index * limit

        try {
            val response = when (loadType) {
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND, LoadType.REFRESH -> api.getPage(offset, limit, search)
            }

            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            return MediatorResult.Success(endOfPaginationReached = body.isEmpty())
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getIndex(loadType: LoadType) = when (loadType) {
        LoadType.REFRESH -> 0
        LoadType.PREPEND -> null
        LoadType.APPEND -> ++index
    }
}