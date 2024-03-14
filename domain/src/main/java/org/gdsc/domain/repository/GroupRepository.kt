package org.gdsc.domain.repository

import org.gdsc.domain.model.response.Group

interface GroupRepository {
    suspend fun getMyGroups(): List<Group>
}