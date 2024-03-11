package org.gdsc.domain.usecase

import org.gdsc.domain.repository.GroupRepository
import javax.inject.Inject

class GetMyGroupUseCase @Inject constructor(
    private val groupRepository: GroupRepository
){
    suspend operator fun invoke() = groupRepository.getMyGroups()
}