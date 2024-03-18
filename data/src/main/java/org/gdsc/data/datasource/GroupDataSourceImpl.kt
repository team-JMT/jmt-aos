package org.gdsc.data.datasource

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import org.gdsc.data.database.GroupBySearchPagingSource
import org.gdsc.data.network.GroupAPI
import org.gdsc.domain.model.GroupPreview
import org.gdsc.domain.model.request.GroupSearchRequest
import org.gdsc.domain.model.response.Group
import javax.inject.Inject

class GroupDataSourceImpl @Inject constructor(
    private val groupAPI: GroupAPI,
) : GroupDataSource {
    override suspend fun getMyGroups(): List<Group> {
        runCatching {
            groupAPI.getMyGroups()
        }.onSuccess {
            return it.data
        }.onFailure {
            return emptyList()
        }
        return emptyList()
    }

    override suspend fun selectGroup(groupId: Int): String {
        runCatching {
            groupAPI.selectGroup(groupId)
        }.onSuccess {
            return it.data
        }.onFailure {
            return ""
        }
        return ""
    }

    override suspend fun searchGroup(keyword: String, limitCount: Int): List<GroupPreview> {
        return groupAPI.searchGroup(GroupSearchRequest(keyword)).data.groupList.take(limitCount)
    }

    override suspend fun searchPagingGroup(
        keyword: String,
    ): Flow<PagingData<GroupPreview>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true
            )
        ) {
            GroupBySearchPagingSource(
                groupAPI,
                GroupSearchRequest(keyword))
        }.flow.cachedIn(CoroutineScope(Dispatchers.IO))

    }
}