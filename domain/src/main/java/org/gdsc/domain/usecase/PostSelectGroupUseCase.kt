package org.gdsc.domain.usecase

import org.gdsc.domain.repository.GroupRepository
import javax.inject.Inject

class PostSelectGroupUseCase @Inject constructor(
    private val groupRepository: GroupRepository
) {
    suspend operator fun invoke(groupId: Int): String {
        return groupRepository.selectGroup(groupId)
    }
}