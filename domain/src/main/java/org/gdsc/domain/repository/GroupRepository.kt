package org.gdsc.domain.repository

import org.gdsc.domain.model.GroupPreview
import org.gdsc.domain.model.response.Group

interface GroupRepository {
    suspend fun getMyGroups(): List<Group>

    suspend fun selectGroup(groupId: Int): String

    suspend fun searchGroup(keyword: String, limitCount: Int): List<GroupPreview>
}