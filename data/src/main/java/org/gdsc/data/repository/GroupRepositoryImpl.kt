package org.gdsc.data.repository

import org.gdsc.data.datasource.GroupDataSource
import org.gdsc.domain.model.response.GroupResponse
import org.gdsc.domain.repository.GroupRepository
import javax.inject.Inject

class GroupRepositoryImpl @Inject constructor(
    private val groupDataSource: GroupDataSource
): GroupRepository {
    override suspend fun getMyGroups(): List<GroupResponse> {
        return groupDataSource.getMyGroups()
    }

}