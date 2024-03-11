package org.gdsc.domain.repository

import org.gdsc.domain.model.response.GroupResponse

interface GroupRepository {
    suspend fun getMyGroups(): List<GroupResponse>
}