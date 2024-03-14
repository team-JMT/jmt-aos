package org.gdsc.data.datasource

import android.util.Log
import org.gdsc.data.network.GroupAPI
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
}