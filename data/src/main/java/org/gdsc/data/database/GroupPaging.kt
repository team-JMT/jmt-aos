package org.gdsc.data.database

import org.gdsc.domain.model.GroupPreview

data class GroupPaging(
    val groupList: List<GroupPreview>,
    val page: Page
)