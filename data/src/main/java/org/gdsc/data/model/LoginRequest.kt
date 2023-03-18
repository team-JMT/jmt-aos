package org.gdsc.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GoogleLoginRequest(
    @Expose
    @SerializedName("token")
    var token: String
)

data class AppleLoginRequest(
    @Expose
    @SerializedName("email")
    var email: String,
    @SerializedName("clientId")
    var clientId: String
)
