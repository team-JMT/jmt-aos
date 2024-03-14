package org.gdsc.data.network

import org.gdsc.data.model.Response
import org.gdsc.domain.model.response.Group
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface GroupAPI {

    @GET("api/v1/group/my")
    suspend fun getMyGroups(): Response<List<Group>>

    @POST("api/v1/group/{groupId}/select")
    suspend fun selectGroup(
        @Path("groupId") groupId: Int
    ): Response<String>

}