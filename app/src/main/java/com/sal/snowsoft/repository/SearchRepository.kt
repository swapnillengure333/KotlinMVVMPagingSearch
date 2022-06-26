package com.sal.snowsoft.repository

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sal.snowsoft.model.ContentData
import com.sal.snowsoft.api.DataService

class SearchRepository(val dataService: DataService, val query:String): PagingSource<Int, ContentData>() {
    private val DEFAULT_PAGE_INDEX= 1
    override fun getRefreshKey(state: PagingState<Int, ContentData>): Int? {
        return state.anchorPosition
    }
    //call data with query
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ContentData> {
        return try {
            val nextPage: Int = params.key ?: DEFAULT_PAGE_INDEX
            val response = dataService.loadJsonFromAssetsWithSearch(nextPage,query)
            LoadResult.Page(
                response.page.content_items.content,
                prevKey = if(nextPage == DEFAULT_PAGE_INDEX) null else nextPage-1,
                nextKey = if(response.page.page_size.toInt()<20) null else nextPage+1
            )
        }
        catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}