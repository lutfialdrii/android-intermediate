package com.lutfi.storykuy.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.lutfi.storykuy.data.models.ListStoryItem
import com.lutfi.storykuy.data.remote.retrofit.ApiService
import retrofit2.HttpException
import java.io.IOException

class StoryPagingSource(private val apiService: ApiService, private val token: String) :
    PagingSource<Int, ListStoryItem>() {
    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val pos = params.key ?: INITIAL_PAGE_INDEX
            val data = (apiService.getStories(token = "Bearer $token")).listStory
            LoadResult.Page(
                data = data!!,
                prevKey = if (pos == INITIAL_PAGE_INDEX) null else pos - 1,
                nextKey = if (data.isEmpty()) null else pos + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            return LoadResult.Error(e)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}