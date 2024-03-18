package org.gdsc.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import org.gdsc.domain.model.GroupPreview
import org.gdsc.domain.repository.GroupRepository
import javax.inject.Inject

class GetGroupBySearchUseCase @Inject constructor(
    private val groupRepository: GroupRepository
) {
    suspend operator fun invoke(
        keyword: String,
    ): Flow<PagingData<GroupPreview>> {

        return groupRepository.searchPagingGroup(keyword)
    }
}