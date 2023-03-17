package org.gdsc.jmt.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class JsonPlaceDTO(
    @Expose
    @SerializedName("token")
    var token: String
)

data class JsonPlaceDTO2(
    @Expose
    @SerializedName("email")
    var email: String,
    @SerializedName("clientId")
    var clientId: String
)
