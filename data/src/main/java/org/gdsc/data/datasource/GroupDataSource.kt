package org.gdsc.data.datasource

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.gdsc.domain.model.GroupPreview
import org.gdsc.domain.model.response.Group


interface GroupDataSource {
    suspend fun getMyGroups(): List<Group>

    suspend fun selectGroup(groupId: Int): String

    suspend fun searchGroup(keyword: String, limitCount: Int): List<GroupPreview>

    suspend fun searchPagingGroup(keyword: String): Flow<PagingData<GroupPreview>>
}