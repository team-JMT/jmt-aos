package org.gdsc.data.repository

import org.gdsc.data.datasource.GroupDataSource
import org.gdsc.domain.model.GroupPreview
import org.gdsc.domain.model.response.Group
import org.gdsc.domain.repository.GroupRepository
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val groupDataSource: GroupDataSource
): GroupRepository {
    override suspend fun getMyGroups(): List<Group> {
        return groupDataSource.getMyGroups()
    }

    override suspend fun selectGroup(groupId: Int): String {
        return groupDataSource.selectGroup(groupId)
    }

    override suspend fun searchGroup(keyword: String, limitCount: Int): List<GroupPreview> {
        return groupDataSource.searchGroup(keyword, limitCount)
    }
}