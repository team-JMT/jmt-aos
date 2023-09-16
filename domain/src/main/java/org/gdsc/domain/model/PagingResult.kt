package org.gdsc.domain.model

import androidx.paging.PagingData

data class PagingResult<T : Any>(
    val data: PagingData<T>,
    val totalElementsCount: Int,
)
