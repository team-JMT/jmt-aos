package org.gdsc.data.network

import org.gdsc.data.database.GroupPaging
import org.gdsc.data.model.Response
import org.gdsc.domain.model.request.GroupSearchRequest
import org.gdsc.domain.model.response.Group
import retrofit2.http.Body
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

    @POST("api/v1/group/search")
    suspend fun searchGroup(
        @Body groupSearchRequest: GroupSearchRequest
    ): Response<GroupPaging>
}