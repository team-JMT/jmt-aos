package org.gdsc.domain.usecase

import org.gdsc.domain.repository.GroupRepository
import javax.inject.Inject

class GetGroupBySearchWithLimitCountUseCase @Inject constructor(
    private val groupRepository: GroupRepository
) {
    suspend operator fun invoke(keyword: String, limitCount: Int) = groupRepository.searchGroup(keyword, limitCount)
}