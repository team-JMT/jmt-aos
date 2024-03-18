package org.gdsc.data.database

import androidx.paging.PagingSource
import androidx.paging.PagingState
import org.gdsc.data.network.GroupAPI
import org.gdsc.domain.model.GroupPreview
import org.gdsc.domain.model.request.GroupSearchRequest

class GroupBySearchPagingSource(
    private val api: GroupAPI,
    private val groupSearchRequest: GroupSearchRequest,
): PagingSource<Int, GroupPreview>()  {
    override fun getRefreshKey(state: PagingState<Int, GroupPreview>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GroupPreview> {
        val page = params.key ?: 1
        return try {
            val items = api.searchGroup(groupSearchRequest)
            LoadResult.Page(
                data = items.data.groupList,
                prevKey = null,
                nextKey = if (items.data.groupList.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}