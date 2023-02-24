package ru.nikitazar.data.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.nikitazar.data.api.ApiService
import ru.nikitazar.data.errors.ApiError
import ru.nikitazar.data.models.Drug
import javax.inject.Inject

class DrugSource @Inject constructor(
    private val api: ApiService,
    private val search: String,
) : PagingSource<Int, Drug>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Drug> =
        try {
            val index = params.key ?: 0
            Log.i("observeDrugs", params.key.toString())
            val limit = params.loadSize
            val offset = index * limit

            val response = api.getPage(offset, limit, search)

            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val list = response.body() ?: throw ApiError(response.code(), response.message())

            LoadResult.Page(
                data = list,
                prevKey = if (index == 0) null else index - 1,
                nextKey = if (list.isEmpty()) null else index + 1,
            )
        } catch (e: Exception) {
            LoadResult.Error(throwable = e)
        }

    override fun getRefreshKey(state: PagingState<Int, Drug>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }
}