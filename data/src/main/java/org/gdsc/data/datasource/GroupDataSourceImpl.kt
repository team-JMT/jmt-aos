package org.gdsc.data.datasource

import android.util.Log
import org.gdsc.data.database.GroupPaging
import org.gdsc.data.network.GroupAPI
import org.gdsc.domain.model.GroupPreview
import org.gdsc.domain.model.request.GroupSearchRequest
import org.gdsc.domain.model.response.Group
import javax.inject.Inject

class GroupDataSourceImpl @Inject constructor(
    private val groupAPI: GroupAPI,
): GroupDataSource {
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
}