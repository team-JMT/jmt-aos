package org.gdsc.data.network

import org.gdsc.data.model.Response
import org.gdsc.domain.model.response.GroupResponse
import retrofit2.http.GET

interface GroupAPI {

    @GET("api/v1/group/my")
    suspend fun getMyGroups(): Response<List<GroupResponse>>
}