package org.gdsc.data.datasource

import org.gdsc.domain.model.response.GroupResponse


interface GroupDataSource {
    suspend fun getMyGroups(): List<GroupResponse>
}