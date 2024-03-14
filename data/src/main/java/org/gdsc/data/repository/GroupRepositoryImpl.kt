package org.gdsc.data.repository

import org.gdsc.data.datasource.GroupDataSource
import org.gdsc.domain.model.response.Group
import org.gdsc.domain.repository.GroupRepository
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val groupDataSource: GroupDataSource
): GroupRepository {
    override suspend fun getMyGroups(): List<Group> {
        return groupDataSource.getMyGroups()
    }

}